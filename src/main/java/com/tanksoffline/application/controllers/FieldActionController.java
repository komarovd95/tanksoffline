package com.tanksoffline.application.controllers;

import com.tanksoffline.application.App;
import com.tanksoffline.application.data.fields.Field;
import com.tanksoffline.application.models.core.FieldModel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class FieldActionController implements ActionController<Field>{
    private FieldModel fieldModel;

    public FieldActionController() {
        this.fieldModel = App.getComponent(FieldModel.class);
    }

    @Override
    public Callable<Field> onCreate(Map<String, Object> values) {
        return () -> fieldModel.create((String) values.get("name"), (int) values.get("size"));
    }

    @Override
    public Callable<Field> onFind(Map<String, Object> values) {
        return null;
    }

    @Override
    public Callable<Field> onUpdate(Field field, Map<String, Object> values) {
        return () -> fieldModel.update(field, values);
    }

    @Override
    public Callable<Field> onRemove(Field field) {
        return () -> {
            fieldModel.delete(field);
            return field;
        };
    }

    @Override
    public Callable<Field> onFindOne(Object id) {
        return null;
    }

    @Override
    public Callable<List<Field>> onFindAll() {
        return () -> fieldModel.findAll();
    }

    @Override
    public Callable<Field> onConstruct() {
        return null;
    }

    @Override
    public Callable<Field> onDestroy() {
        return null;
    }
}
