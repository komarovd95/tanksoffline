package tanks.app.services;

import tanks.app.controllers.Controller;
import tanks.app.controllers.LoginController;
import tanks.app.controllers.RegisterController;
import tanks.app.models.Model;
import tanks.app.models.UserModel;
import tanks.app.views.LoginView;
import tanks.app.views.RegisterView;
import tanks.app.views.View;

import java.lang.reflect.InvocationTargetException;

public class ApplicationService implements Service {
    ApplicationService() {}

    public LoginController createLoginController(UserModel model, LoginView view) {
        return new LoginController(model, view);
    }

    public RegisterController createRegisterController(UserModel model, RegisterView view) {
        return new RegisterController(model, view);
    }

    public <M extends Model, V extends View, C extends Controller<? super M, ? extends V>> Controller<? super M, ? extends V>
    createController(Class<C> controllerClass, M model, V view) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return controllerClass.getConstructor(model.getClass(), view.getClass()).newInstance(model, view);
    }


}
