package tanks.app.models;

import tanks.app.views.View;

public interface Model {
    void addObserver(View view);
    void fireUpdate();
    void fireError();
}
