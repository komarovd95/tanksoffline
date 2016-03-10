package tanks.app.controllers;

import tanks.app.models.Model;
import tanks.app.views.View;

import java.util.logging.Logger;

public abstract class AbstractController<M extends Model, V extends View> implements Controller<M, V> {
    protected static final Logger logger = Logger.getLogger("Controller");

    protected M model;
    protected V view;

    public AbstractController(M model, V view) {
        this.model = model;
        this.view = view;
        init();
    }

    @Override
    public M getModel() {
        return model;
    }

    @Override
    public V getView() {
        return view;
    }
}
