package tanks.services;

import tanks.utils.persistence.ResultList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class SerialDataService implements DataService {
    private static final Logger logger = Logger.getLogger(SerialDataService.class.getName());
    private Map<Object, String> identityMap;

    SerialDataService() {
        startUp();
    }

    @Override
    public void startUp() {
        identityMap = new HashMap<>();
    }

    @Override
    public void shutDown() {

    }

    @Override
    public <T> T save(T item) {
        Path filePath = Paths.get(item.getClass().getSimpleName() + "_" + new Date().toString());
        return save(item, filePath);
    }

    public <T> T save(T item, Path filePath) {
        try (ObjectOutputStream outputStream =
                     new ObjectOutputStream(Files.newOutputStream(filePath, StandardOpenOption.CREATE))) {
            outputStream.writeObject(item);
            identityMap.put(item, filePath.toAbsolutePath().toString());
            logger.log(Level.INFO, "New item " + item.getClass().getSimpleName() +  " saved");
            return item;
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T remove(T item) {
        if (identityMap.containsKey(item)) {
            try {
                remove(Paths.get(identityMap.get(item)));
                identityMap.remove(item);
                return item;
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return item;
    }

    public boolean remove(Path filePath) throws IOException {
        return Files.deleteIfExists(filePath);
    }

    @Override
    public <T> T update(T item) {
        return save(item, Paths.get(identityMap.get(item)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T refresh(T item) {
        if (identityMap.containsKey(item)) {
            return find((Class<T>) item.getClass(), Paths.get(identityMap.get(item)));
        }
        return item;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T find(Class<T> itemClass, Object id) {
        if (id != null && id instanceof Path) {
            Path filePath = (Path) id;
            try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(filePath))) {
                Object obj = inputStream.readObject();
                identityMap.put(obj, filePath.toAbsolutePath().toString());
                if (obj.getClass().isAssignableFrom(itemClass)) {
                    return (T) obj;
                } else {
                    throw new ClassNotFoundException();
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.SEVERE, e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("Id must be Path object");
        }
    }

    @Override
    public <T> T fetch(T item, String... fetchedFields) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ResultList<T> findAll(Class<T> itemClass) {
        List<T> list = identityMap.keySet().stream().filter(item -> item.getClass().isAssignableFrom(itemClass))
                .map(item -> (T) item).collect(Collectors.toList());
        return new ResultList<>(list);
    }
}
