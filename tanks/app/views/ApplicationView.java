package tanks.app.views;

import tanks.app.models.Model;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationView extends JFrame implements View {
    private static final Logger logger = Logger.getLogger(ApplicationView.class.getName());

    public ApplicationView() {
        setTitle("TanksOffline");
        setResizable(false);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void setView(View view) {
        if (view instanceof Container) {
            setContentPane((Container) view);
            fireUpdate(null);
        } else {
            throw new RuntimeException("Can't set view " + view.getClass().getSimpleName() + " as main view");
        }
    }

    @Override
    public void flashMessage(String message) {
        logger.log(Level.INFO, message);
    }

    @Override
    public void flashError(String error) {
        logger.log(Level.SEVERE, error);
    }

    @Override
    public void fireUpdate(Model model) {
        pack();
    }

    @Override
    public void fireError(Model model) {
        pack();
    }
}
