package tanks.app.views;

import tanks.app.models.Model;

public interface View {
    void flashMessage(String message);
    void flashError(String error);
    void fireUpdate(Model model);
    void fireError(Model model);
}
