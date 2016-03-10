package tanks.app.controllers;

import tanks.app.models.UserModel;
import tanks.app.views.LoginView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController extends UserActionController<LoginView> implements ActionListener {

    public LoginController(UserModel model, LoginView view) {
        super(model, view);
    }

    @Override
    public void init() {
        view.addLoginAction(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e, () -> model.login(view.getLogin(), view.getPassword()), () -> view.flashMessage("Logged in"));
//        JButton btn = (JButton) e.getSource();
//        btn.setText("Log in...");
//        view.brokeInterface(false);
//
//        SwingUtilities.invokeLater(() -> {
//            try {
//                Validator<String> loginValidator = new LoginValidator();
//                Validator<String> passwordValidator = new PasswordValidator();
//                if (!loginValidator.isValid(view.getLogin())) {
//                    throw new RuntimeException(loginValidator.getValidationMessage());
//                } else if (!passwordValidator.isValid(view.getPassword())) {
//                    throw new RuntimeException(passwordValidator.getValidationMessage());
//                }
//                FutureTask<User> task = new FutureTask<>(() -> model.login(view.getLogin(), view.getPassword()));
//                ExecutionUtil.execute(task);
//                task.get();
//                if (task.isDone()) {
//                    view.flashMessage("User " + model.getCurrentUser().getLogin() + " logged in");
//                }
//            } catch (InterruptedException | ExecutionException | RuntimeException e1) {
//                logger.log(Level.SEVERE, e1.getMessage());
//                if (e1.getCause() instanceof RuntimeException) {
//                    view.flashError(e1.getCause().getMessage());
//                } else {
//                    throw new RuntimeException(e1);
//                }
//            } finally {
//                btn.setText("Log in");
//                view.brokeInterface(true);
//            }
//        });

    }
}
