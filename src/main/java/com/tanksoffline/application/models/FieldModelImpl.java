package com.tanksoffline.application.models;

import com.tanksoffline.application.data.fields.Field;
import com.tanksoffline.application.models.core.FieldModel;
import com.tanksoffline.application.models.core.UserModel;
import com.tanksoffline.core.services.DIService;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;
import com.tanksoffline.core.utils.validation.Login;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FieldModelImpl implements FieldModel {
    private static final DataService dataService = ServiceLocator.getInstance().getService(DataService.class);

    @Override
    public Field findOne(Object value) {
        return null;
    }

    @Override
    public List<Field> findAll() {
        return dataService.findAll(Field.class);
    }

    @Override
    public List<Field> findBy(Map<String, Object> params) {
        return Collections.emptyList();
    }

    @Override
    public Field update(Field field, Map<String, Object> values) {
        field.update();
        return field;
    }

    @Override
    public void delete(Field field) {
        field.remove();
    }

    @Override
    public Field create(@Login String fieldName, int fieldSize) {
        Field field = new Field(ServiceLocator.getInstance().getService(DIService.class)
                .getComponent(UserModel.class).getLoggedUser(), fieldName, fieldSize, fieldSize);
        field.save();
        return field;
    }
}
