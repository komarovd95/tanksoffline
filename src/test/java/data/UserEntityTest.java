package data;

import com.tanksoffline.application.entities.UserEntity;
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
public class UserEntityTest {
    private UserEntity userEntity;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(ServiceLocator.class);
        ServiceLocator serviceLocatorMock = PowerMockito.mock(ServiceLocator.class);

        PowerMockito.when(serviceLocatorMock.getService(DataService.class)).thenReturn(null);
        PowerMockito.when(ServiceLocator.getInstance()).thenReturn(serviceLocatorMock);

        userEntity = new UserEntity("Dave", "pass123");
    }

    @Test
    public void testGetLogin() throws Exception {
        assertEquals("Dave", userEntity.getLogin());
    }

    @Test
    public void testGetPassword() throws Exception {
        assertNotNull(userEntity.getPassword());
    }

    @Test
    public void testSetLogin() throws Exception {
        userEntity.setLogin("Mark");
        assertEquals("Mark", userEntity.getLogin());
    }

    @Test(expected = RuntimeException.class)
    public void testSetPassword() throws Exception {
        userEntity.setPassword(null);
    }

    @Test
    public void testIsManager() throws Exception {
        assertFalse(userEntity.isManager());
    }

    @Test
    public void testGetUserType() throws Exception {
        assertEquals(UserEntity.UserType.USER, userEntity.getUserType());
    }

    @Test(expected = RuntimeException.class)
    public void testSetUserType() throws Exception {
        userEntity.setUserType(userEntity, UserEntity.UserType.MANAGER);
    }
}