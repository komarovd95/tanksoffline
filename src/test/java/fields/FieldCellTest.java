package fields;

import com.tanksoffline.data.fields.Direction;
import com.tanksoffline.data.fields.FieldCell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.junit.Assert.*;

public class FieldCellTest {
    private static final Path RESOURCE_PATH = Paths.get("src/test/resources/tmp.data");
    private FieldCell fieldCell;

    @Before
    public void setUp() throws Exception {
        fieldCell = new FieldCell(true, false, true, true);
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(RESOURCE_PATH);
    }

    @Test
    public void testHasBorder() throws Exception {
        assertTrue(fieldCell.hasBorder(Direction.TOP) &&
                !fieldCell.hasBorder(Direction.LEFT) &&
                fieldCell.hasBorder(Direction.RIGHT) &&
                fieldCell.hasBorder(Direction.BOTTOM));
    }

    @Test
    public void testSetBorder() throws Exception {
        fieldCell.setBorder(Direction.LEFT);
        assertTrue(fieldCell.hasBorder(Direction.LEFT));
    }

    @Test
    public void testIsSpawnCell() throws Exception {
        assertFalse(fieldCell.isSpawnCell());
        fieldCell.setSpawnGroup(1);
        assertTrue(fieldCell.isSpawnCell());
    }

    @Test
    public void testGetSpawnGroup() throws Exception {
        assertEquals(0, fieldCell.getSpawnGroup());
    }

    @Test
    public void testReadWriteToFile() throws Exception {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(
                RESOURCE_PATH, StandardOpenOption.CREATE));
             ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(
                RESOURCE_PATH, StandardOpenOption.READ))) {
            outputStream.writeObject(fieldCell);
            FieldCell readObject = (FieldCell) inputStream.readObject();
            assertEquals(fieldCell, readObject);
            assertEquals(fieldCell.hashCode(), readObject.hashCode());
            assertEquals(fieldCell.toString(), readObject.toString());
        }
    }
}