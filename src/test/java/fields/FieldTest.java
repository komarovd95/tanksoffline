package fields;

import com.tanksoffline.application.utils.UserType;
import com.tanksoffline.application.data.fields.Field;
import com.tanksoffline.application.data.fields.FieldCell;
import com.tanksoffline.application.data.users.User;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ServiceLocator.class)
public class FieldTest {
    private static final Path RESOURCE_PATH = Paths.get("src/test/resources/tmp_field.data");

    private Field field;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(ServiceLocator.class);
        ServiceLocator serviceLocatorMock = PowerMockito.mock(ServiceLocator.class);

        PowerMockito.when(serviceLocatorMock.getService(DataService.class)).thenReturn(null);
        PowerMockito.when(ServiceLocator.getInstance()).thenReturn(serviceLocatorMock);

        field = new Field(new User("Dave", "pass123", UserType.MANAGER), "Temple", 3, 2);
        field.addCell(0, 0, new FieldCell(true, true, true, true));
        field.addCell(0, 1, new FieldCell(false, true, true, true, 1));
        field.addCell(1, 0, new FieldCell(true, false, true, true));
        field.addCell(1, 1, new FieldCell(true, true, false, true));
        field.addCell(2, 1, new FieldCell(true, true, true, false, 1));
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(RESOURCE_PATH);
    }

    @Test
    public void testSerialization() throws Exception {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(
                Files.newOutputStream(RESOURCE_PATH, StandardOpenOption.CREATE));
             ObjectInputStream inputStream = new ObjectInputStream(
                     Files.newInputStream(RESOURCE_PATH, StandardOpenOption.READ))) {
            outputStream.writeObject(field);
            Field readObject = (Field) inputStream.readObject();
            assertEquals(field.toString(), readObject.toString());
        }
    }
}