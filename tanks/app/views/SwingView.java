package tanks.app.views;

import javax.swing.event.ChangeListener;

public interface SwingView extends View {
    void brokeInterface(boolean isBroken);
    void setUpdateListener(ChangeListener listener);
}
