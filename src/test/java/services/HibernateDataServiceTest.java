package services;

import com.tanksoffline.application.utils.UserType;
import com.tanksoffline.application.data.users.User;
import com.tanksoffline.application.configuration.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.application.services.HibernateDataService;
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
    private List<User> userList;

    @Before
    public void setUp() throws Exception {
        ServiceLocator.bind(new ApplicationServiceLocatorConfiguration());
        dataService = ServiceLocator.getInstance().getService(HibernateDataService.class);
        dataService.start();

        userList = new ArrayList<>();
        userList.add(new User("Dave", "pass123"));
        userList.add(new User("Garry", "pls123"));
        userList.add(new User("Viktor", "fst23", UserType.MANAGER));
        userList.add(new User("Mark", "123", UserType.MANAGER));

        for (User user : userList) {
            dataService.save(user);
        }
    }

    @After
    public void tearDown() throws Exception {
        dataService.shutdown();
    }

    @Test
    public void testWhereMap() throws Exception {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userType", UserType.MANAGER);
        assertEquals(userList.subList(2, 4), dataService.where(User.class, queryMap));
    }

    @Test
    public void testWhereSingleParam() throws Exception {
        assertEquals(userList.subList(2, 4),
                dataService.where(User.class, "userType", UserType.MANAGER));
    }

    @Test
    public void testWhereQuery() throws Exception {
        String queryString = "SELECT user FROM User user WHERE user.userType = ?";
        assertEquals(userList.subList(2, 4), dataService.where(queryString, UserType.MANAGER));
    }
}