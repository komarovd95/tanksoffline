package com.tanksoffline.application.controllers;

import com.tanksoffline.application.data.Field;
import com.tanksoffline.application.data.User;
import com.tanksoffline.application.entities.FieldEntity;
import com.tanksoffline.application.entities.search.FieldSearch;
import com.tanksoffline.application.models.FieldActiveModel;
import com.tanksoffline.core.data.Search;
import com.tanksoffline.core.mvc.ActionController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class FieldActionController implements ActionController<Field> {
    private FieldActiveModel fieldActiveModel;
    private Search<FieldEntity> fieldSearch;

    public FieldActionController() {
        this.fieldSearch = new FieldSearch();
    }

    public FieldActionController(Field field) {
        this();
        this.fieldActiveModel = new FieldActiveModel(field);
    }

    @Override
    public Callable<Field> create(Map<String, Object> values) {
        return () -> {
            if (fieldActiveModel == null) {
                fieldActiveModel = new FieldActiveModel((String) values.get("name"),
                        (Integer) values.get("width"), (Integer) values.get("height"));
            }
            return fieldActiveModel.save();
        };
    }

    @Override
    public Callable<? extends Field> findBy(Map<String, Object> values) {
        return () -> {
            List<? extends Field> fields = fieldSearch.findBy(values);
            return (fields.size() == 0) ? null : fields.get(0);
        };
    }

    @Override
    public Callable<Field> update(Map<String, Object> values) {
        return () -> fieldActiveModel.update();
    }

    @Override
    public Callable<Field> remove() {
        return () -> fieldActiveModel.remove();
    }

    @Override
    public Callable<List<? extends Field>> list() {
        return () -> fieldSearch.findAll();
    }

    @Override
    public Callable<Field> construct(Field field) {
        return () -> fieldActiveModel = new FieldActiveModel(field);
    }

    @Override
    public Callable<Field> destroy() {
        fieldSearch = null;
        fieldActiveModel = null;
        return null;
    }
}
