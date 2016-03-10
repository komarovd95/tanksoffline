package tanks.app.controllers;

import tanks.app.models.UserModel;
import tanks.app.validators.Validator;
import tanks.app.views.SwingView;
import tanks.users.User;
import tanks.utils.ExecutionUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;

public abstract class UserActionController<V extends SwingView> extends AbstractController<UserModel, V> {

    public UserActionController(UserModel model, V view) {
        super(model, view);
    }

    protected void actionPerformed(ActionEvent e, Callable<User> execCallback, Runnable doneCallback) {
        JButton btn = (JButton) e.getSource();
        btn.setText(btn.getText() + "...");
        view.brokeInterface(false);
        view.fireUpdate(model);

        SwingUtilities.invokeLater(() -> {
            try {
                FutureTask<User> task = new FutureTask<>(execCallback);
                ExecutionUtil.execute(task);
                task.get();
                if (task.isDone()) {
                    ExecutionUtil.execute(doneCallback);
                }
            } catch (InterruptedException | ExecutionException e1) {
                logger.log(Level.SEVERE, e1.getMessage());
                if (e1.getCause() instanceof RuntimeException) {
                    view.flashError(e1.getCause().getMessage());
                } else {
                    throw new RuntimeException(e1);
                }
            } finally {
                btn.setText(btn.getText().substring(0, btn.getText().length() - 3));
                view.brokeInterface(true);
                view.fireUpdate(model);
            }
        });
    }
}
