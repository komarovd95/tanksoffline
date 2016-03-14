package models;

import com.tanksoffline.application.configuration.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.application.models.core.UserModel;
import com.tanksoffline.core.services.DIService;
import com.tanksoffline.core.services.ServiceLocator;
import com.tanksoffline.core.services.ValidationService;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ValidationException;

public class UserModelTest {
    private UserModel userModel;

    @Before
    public void setUp() throws Exception {
        ServiceLocator.bind(new ApplicationServiceLocatorConfiguration());
        userModel = ServiceLocator.getInstance().getService(DIService.class).getComponent(UserModel.class);
        ServiceLocator.getInstance().getService(ValidationService.class).start();
    }

    @Test
    public void testLoginMethodValidation() throws Exception {
        try {
            userModel.login("", null);
        } catch (ValidationException e) {
            System.out.println(ServiceLocator.getInstance().getService(ValidationService.class).getErrorMessages());
        }
    }
}