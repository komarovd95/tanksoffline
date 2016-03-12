package persistence;

import com.tanksoffline.application.utils.UserType;
import com.tanksoffline.data.fields.Field;
import com.tanksoffline.data.fields.FieldCell;
import com.tanksoffline.data.users.User;
import com.tanksoffline.services.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.services.HibernateDataService;
import com.tanksoffline.services.core.DataService;
import com.tanksoffline.services.core.ServiceLocator;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldPersistenceTest {
    private User user;
    private DataService dataService;
    private Field field;

    @Before
    public void setUp() throws Exception {
        ServiceLocator.bind(new ApplicationServiceLocatorConfiguration());
        dataService = ServiceLocator.getInstance().getService(HibernateDataService.class);
        dataService.start();

        user = new User("Dave", "pass123", UserType.MANAGER);
        dataService.save(user);

        field = new Field(user, "Temple", 3, 2, 2);
        field.addCell(0, 0, new FieldCell(true, true, true, true));
        field.addCell(0, 1, new FieldCell(false, true, true, true, 1));
        field.addCell(1, 0, new FieldCell(true, false, true, true));
        field.addCell(1, 1, new FieldCell(true, true, false, true));
        field.addCell(2, 1, new FieldCell(true, true, true, false, 1));
    }

    @After
    public void tearDown() throws Exception {
        dataService.shutdown();
    }

    @Test(expected = ConstraintViolationException.class)
    public void testNameUniqueConstraint() throws Throwable {
        try {
            dataService.save(field);
            dataService.save(new Field(user, "Temple"));
        } catch (Throwable t) {
            throw t.getCause();
        }
    }

    @Test(expected = DataException.class)
    public void testNameLengthConstraint() throws Throwable {
        try {
            dataService.save(new Field(user, "David Mark SpiderManJ")); // string length == 21
        } catch (Throwable t) {
            throw t.getCause();
        }
    }

    @Test
    public void testBlobSerialization() {
        dataService.save(field);
        Field foundObject = dataService.find(Field.class, 2L);

        assertEquals(field.getWidth(), foundObject.getWidth());
        assertEquals(field.getHeight(), foundObject.getHeight());

        for (int i = 0; i < field.getWidth(); i++) {
            for (int j = 0; j < field.getHeight(); j++) {
                assertEquals(field.getCell(i, j), foundObject.getCell(i, j));
            }
        }
    }
}