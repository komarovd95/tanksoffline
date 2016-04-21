package services;

import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.application.configuration.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class DataServiceTest {
//    private DataService dataService;
//    private UserEntity userEntity;
//
//    @Before
//    public void setUp() throws Exception {
//        ServiceLocator.bind(new ApplicationServiceLocatorConfiguration());
//        dataService = ServiceLocator.getInstance().getService(HibernateDataService.class);
//        dataService.start();
//
//        userEntity = new UserEntity("Dave", "pass123");
//    }
//
//    @After
//    public void tearDown() throws Exception{
//        dataService.shutdown();
//    }
//
//    @Test
//    public void testSave() throws Exception {
//        dataService.save(userEntity);
//        assertEquals(1L, userEntity.getId().longValue());
//        assertEquals(userEntity.getLogin(), dataService.findById(UserEntity.class, 1L).getLogin());
//    }
//
//    @Test
//    public void testRemove() throws Exception {
//        dataService.save(userEntity);
//        dataService.remove(userEntity);
//        assertNull(dataService.findById(UserEntity.class, 1L));
//    }
//
//    @Test
//    public void testUpdate() throws Exception {
//        dataService.save(userEntity);
//        userEntity.setLogin("Mark");
//        dataService.update(userEntity);
//        assertEquals(userEntity.getLogin(), dataService.findById(UserEntity.class, 1L).getLogin());
//    }
//
//    @Test
//    public void testRefresh() throws Exception {
//        dataService.save(userEntity);
//        userEntity.setLogin("Mark");
//        dataService.refresh(userEntity);
//        assertEquals(userEntity.getLogin(), "Dave");
//    }
//
//    @Test
//    public void testFind() throws Exception {
//        dataService.save(userEntity);
//        assertNotNull(dataService.findById(UserEntity.class, 1L));
//        assertNull(dataService.findById(UserEntity.class, 2L));
//    }
//
//    @Test
//    public void testFetch() throws Exception {
//
//    }
}