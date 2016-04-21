package models;

import com.tanksoffline.application.configuration.ApplicationServiceLocatorConfiguration;
import com.tanksoffline.core.services.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ValidationException;

public class UserEntityModelTest {
//    private UserModel userModel;
//
//    @Before
//    public void setUp() throws Exception {
//        ServiceLocator.bind(new ApplicationServiceLocatorConfiguration());
//        userModel = ServiceLocator.getInstance().getService(DIService.class).getComponent(UserModel.class);
//        ServiceLocator.getInstance().getService(ValidationService.class).start();
//        ServiceLocator.getInstance().getService(DataService.class).start();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        ServiceLocator.getInstance().getService(ValidationService.class).shutdown();
//    }
//
//    @Test
//    public void testLoginMethodValidation() throws Exception {
//        try {
//            userModel.login("dave", "pass123");
//            System.out.println(userModel.getLoggedUserProperty().get().getLogin());
//        } catch (ValidationException e) {
//            System.out.println(ServiceLocator.getInstance().getService(ValidationService.class).getErrorMessages());
//        }
//    }
//
//    @Test
//    public void testRegisterMethodValidation() throws Exception {
//        try {
//            userModel.register("dave", "pass123", true);
//            System.out.println(userModel.getLoggedUserProperty().get().getLogin());
//        } catch (ValidationException e) {
//            System.out.println(ServiceLocator.getInstance().getService(ValidationService.class).getErrorClasses());
//        }
//    }
//
//    @Test
//    public void testBundle() throws Exception {
//        System.out.println(ServiceLocator.getInstance().getService(ValidationService.class).getErrorMessage("incorrect_login"));
//    }
}