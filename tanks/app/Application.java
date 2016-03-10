package tanks.app;

import tanks.app.controllers.ApplicationController;
import tanks.app.services.Services;

import javax.swing.*;

public class Application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Services.load();

            ApplicationController app = new ApplicationController();
            app.init();
        });
    }
}
