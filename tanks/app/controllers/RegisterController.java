package tanks.app.controllers;

import tanks.app.models.UserModel;
import tanks.app.views.RegisterView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

public class RegisterController extends UserActionController<RegisterView> implements ActionListener {

    public RegisterController(UserModel model, RegisterView view) {
        super(model, view);
    }

    @Override
    public void init() {
        view.addRegisterAction(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e,
                () -> model.register(view.getLogin(), view.getPassword()),
                () -> {
                    logger.log(Level.INFO, "Registering user with name " + model.getCurrentUser().getLogin());

                });
    }
}
