package tanks.app.views;

import java.awt.event.ActionListener;

public interface LoginView extends SwingView {
    String getLogin();
    String getPassword();
    void addLoginAction(ActionListener listener);
}
