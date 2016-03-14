package data;

import com.tanksoffline.application.utils.UserType;
import com.tanksoffline.application.data.users.User;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ServiceLocator.class)
public class UserTest {
    private User user;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(ServiceLocator.class);
        ServiceLocator serviceLocatorMock = PowerMockito.mock(ServiceLocator.class);

        PowerMockito.when(serviceLocatorMock.getService(DataService.class)).thenReturn(null);
        PowerMockito.when(ServiceLocator.getInstance()).thenReturn(serviceLocatorMock);

        user = new User("Dave", "pass123");
    }

    @Test
    public void testGetLogin() throws Exception {
        assertEquals("Dave", user.getLogin());
    }

    @Test
    public void testGetPassword() throws Exception {
        assertNotNull(user.getPassword());
    }

    @Test
    public void testSetLogin() throws Exception {
        user.setLogin("Mark");
        assertEquals("Mark", user.getLogin());
    }

    @Test(expected = RuntimeException.class)
    public void testSetPassword() throws Exception {
        user.setPassword(null);
    }

    @Test
    public void testIsManager() throws Exception {
        assertFalse(user.isManager());
    }

    @Test
    public void testGetUserType() throws Exception {
        assertEquals(UserType.USER, user.getUserType());
    }

    @Test(expected = RuntimeException.class)
    public void testSetUserType() throws Exception {
        user.setUserType(user, UserType.MANAGER);
    }
}