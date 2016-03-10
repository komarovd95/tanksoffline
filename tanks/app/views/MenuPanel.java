package tanks.app.views;

import tanks.app.models.Model;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

public class MenuPanel extends JPanel implements SwingView {
    private JPanel contentPane;
    private JButton startGameBtn;
    private JButton mapsBtn;
    private JButton usersBtn;
    private JButton logoutBtn;
    private JButton exitBtn;

    public MenuPanel() {
        init();
    }

    private void init() {
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(contentPane);
    }

    @Override
    public void brokeInterface(boolean isBroken) {

    }

    @Override
    public void setUpdateListener(ChangeListener listener) {

    }

    @Override
    public void flashMessage(String message) {

    }

    @Override
    public void flashError(String error) {

    }

    @Override
    public void fireUpdate(Model model) {

    }

    @Override
    public void fireError(Model model) {

    }
}
