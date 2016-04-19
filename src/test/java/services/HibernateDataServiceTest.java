package services;

import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.application.configuration.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class HibernateDataServiceTest {
    private DataService dataService;
    private List<UserEntity> userEntityList;

    @Before
    public void setUp() throws Exception {
        ServiceLocator.bind(new ApplicationServiceLocatorConfiguration());
        dataService = ServiceLocator.getInstance().getService(HibernateDataService.class);
        dataService.start();

        userEntityList = new ArrayList<>();
        userEntityList.add(new UserEntity("Dave", "pass123"));
        userEntityList.add(new UserEntity("Garry", "pls123"));
        userEntityList.add(new UserEntity("Viktor", "fst23", UserEntity.UserType.MANAGER));
        userEntityList.add(new UserEntity("Mark", "123", UserEntity.UserType.MANAGER));

        for (UserEntity userEntity : userEntityList) {
            dataService.save(userEntity);
        }
    }

    @After
    public void tearDown() throws Exception {
        dataService.shutdown();
    }

    @Test
    public void testWhereMap() throws Exception {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userType", UserEntity.UserType.MANAGER);
        assertEquals(userEntityList.subList(2, 4), dataService.findBy(UserEntity.class, queryMap));
    }

    @Test
    public void testWhereSingleParam() throws Exception {
        assertEquals(userEntityList.subList(2, 4),
                dataService.findBy(UserEntity.class, "userType", UserEntity.UserType.MANAGER));
    }

    @Test
    public void testWhereQuery() throws Exception {
        String queryString = "SELECT user FROM UserEntity user WHERE user.userType = ?";
        assertEquals(userEntityList.subList(2, 4), dataService.findBy(queryString, UserEntity.UserType.MANAGER));
    }
}