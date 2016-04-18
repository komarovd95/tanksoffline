package com.tanksoffline.application.controllers;

import com.tanksoffline.application.app.App;
import com.tanksoffline.application.entities.FieldEntity;
import com.tanksoffline.application.models.core.FieldModel;
import com.tanksoffline.core.mvc.ActionController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class FieldActionController implements ActionController<FieldEntity> {
    private FieldModel fieldModel;

    public FieldActionController() {
        this.fieldModel = App.getComponent(FieldModel.class);
    }

    @Override
    public Callable<FieldEntity> onCreate(Map<String, Object> values) {
        return () -> fieldModel.create((String) values.get("name"), (int) values.get("size"));
    }

    @Override
    public Callable<FieldEntity> onFind(Map<String, Object> values) {
        return null;
    }

    @Override
    public Callable<FieldEntity> onUpdate(FieldEntity field, Map<String, Object> values) {
        return () -> fieldModel.update(field, values);
    }

    @Override
    public Callable<FieldEntity> onRemove(FieldEntity field) {
        return () -> {
            fieldModel.delete(field);
            return field;
        };
    }

    @Override
    public Callable<FieldEntity> onFindOne(Object id) {
        return null;
    }

    @Override
    public Callable<List<FieldEntity>> onFindAll() {
        return () -> fieldModel.findAll();
    }

    @Override
    public Callable<FieldEntity> onConstruct() {
        return null;
    }

    @Override
    public Callable<FieldEntity> onDestroy() {
        return null;
    }
}
