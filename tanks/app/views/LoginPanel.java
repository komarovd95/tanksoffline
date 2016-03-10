package tanks.app.views;

import tanks.app.models.Model;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel implements RegisterView {
    private ChangeListener changeListener;
    private JPasswordField passwordField;
    private JTextField loginField;
    private JButton registerBtn;
    private JButton loginBtn;
    private JPanel contentPane;
    private JLabel flashLabel;

    public LoginPanel() {
        init();
    }

    private void init() {
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(contentPane);
    }

    @Override
    public void flashMessage(String message) {
        flashLabel.setText("<html>" + message + "</html>");
        fireUpdate(null);
    }

    @Override
    public void flashError(String error) {
        flashMessage("<span style='color: red;'>" + error + "</span>");
    }

    @Override
    public void fireUpdate(Model model) {
        changeListener.stateChanged(null);
    }

    @Override
    public void fireError(Model model) {

    }

    @Override
    public String getLogin() {
        return loginField.getText();
    }

    @Override
    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    @Override
    public void addLoginAction(ActionListener actionListener) {
        loginBtn.addActionListener(actionListener);
    }

    @Override
    public void addRegisterAction(ActionListener listener) {
        registerBtn.addActionListener(listener);
    }

    @Override
    public void brokeInterface(boolean isBroken) {
        loginBtn.setEnabled(isBroken);
        registerBtn.setEnabled(isBroken);
    }

    @Override
    public void setUpdateListener(ChangeListener listener) {
        this.changeListener = listener;
    }
}
