package tanks.app.controllers;

import tanks.app.Application;
import tanks.app.models.Model;
import tanks.app.models.UserModel;
import tanks.app.services.ApplicationService;
import tanks.app.services.Services;
import tanks.app.views.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ApplicationController implements Controller<UserModel, View>, ChangeListener {
    private ApplicationView applicationView;
    private View currentView;
    private View previousView;
    private UserModel model;

    @Override
    public UserModel getModel() {
        return model;
    }

    @Override
    public View getView() {
        return currentView;
    }

    @Override
    public void init() {
        applicationView = new ApplicationView();
        model = new UserModel();


        LoginView loginView = new LoginPanel();
        setState(loginView);
        new LoginController(model, (LoginView) currentView);
        new RegisterController(model, (RegisterView) currentView);
//        List<Controller<? super UserModel, ? extends LoginView>> controllerList = new ArrayList<>();
//        controllerList.add(new LoginController(model, loginView));
//        controllerList.add(new RegisterController(model, loginView));
//        LoginController controller = Services.getService(ApplicationService.class).createController(LoginController.class, model, loginView);
//        setState(loginView, controllerList, new ArrayList<UserModel>());
    }

    public void setState(View view) {
        previousView = currentView;
        currentView = view;
        if (view instanceof SwingView) {
            ((SwingView) currentView).setUpdateListener(this);
        }
        applicationView.setView(currentView);
        stateChanged(null);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        applicationView.pack();
    }
}
