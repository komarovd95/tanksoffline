package persistence;

import com.tanksoffline.application.entities.FieldEntity;
import com.tanksoffline.application.data.FieldCell;
import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.application.configuration.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldPersistenceTest {
//    private UserEntity userEntity;
//    private DataService dataService;
//    private FieldEntity field;
//
//    @Before
//    public void setUp() throws Exception {
//        ServiceLocator.bind(new ApplicationServiceLocatorConfiguration());
//        dataService = ServiceLocator.getInstance().getService(HibernateDataService.class);
//        dataService.start();
//
//        userEntity = new UserEntity("Dave", "pass123", UserEntity.UserType.MANAGER);
//        dataService.save(userEntity);
//
//        field = new FieldEntity(userEntity, "Temple", 3, 2);
//        field.addCell(0, 0, new FieldCell(true, true, true, true));
//        field.addCell(0, 1, new FieldCell(false, true, true, true, 1));
//        field.addCell(1, 0, new FieldCell(true, false, true, true));
//        field.addCell(1, 1, new FieldCell(true, true, false, true));
//        field.addCell(2, 1, new FieldCell(true, true, true, false, 1));
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        dataService.shutdown();
//    }
//
//    @Test(expected = ConstraintViolationException.class)
//    public void testNameUniqueConstraint() throws Throwable {
//        try {
//            dataService.save(field);
//            dataService.save(new FieldEntity(userEntity, "Temple"));
//        } catch (Throwable t) {
//            throw t.getCause();
//        }
//    }
//
//    @Test(expected = DataException.class)
//    public void testNameLengthConstraint() throws Throwable {
//        try {
//            dataService.save(new FieldEntity(userEntity, "David Mark SpiderManJ")); // string length == 21
//        } catch (Throwable t) {
//            throw t.getCause();
//        }
//    }
//
//    @Test
//    public void testBlobSerialization() {
//        dataService.save(field);
//        FieldEntity foundObject = dataService.findById(FieldEntity.class, 2L);
//
//        assertEquals(field.getWidth(), foundObject.getWidth());
//        assertEquals(field.getHeight(), foundObject.getHeight());
//
//        for (int i = 0; i < field.getWidth(); i++) {
//            for (int j = 0; j < field.getHeight(); j++) {
//                assertEquals(field.getCell(i, j), foundObject.getCell(i, j));
//            }
//        }
//    }
}