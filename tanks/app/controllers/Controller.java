package tanks.app.controllers;

import tanks.app.models.Model;
import tanks.app.views.View;

public interface Controller<M extends Model, V extends View> {
    M getModel();
    V getView();
    void init();
}
