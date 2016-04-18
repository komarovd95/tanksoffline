package com.tanksoffline.application.models;

import com.tanksoffline.application.entities.FieldEntity;
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
    public FieldEntity findOne(Object value) {
        return null;
    }

    @Override
    public List<FieldEntity> findAll() {
        return dataService.findAll(FieldEntity.class);
    }

    @Override
    public List<FieldEntity> findBy(Map<String, Object> params) {
        return Collections.emptyList();
    }

    @Override
    public FieldEntity update(FieldEntity field, Map<String, Object> values) {
        field.update();
        return field;
    }

    @Override
    public void delete(FieldEntity field) {
        field.remove();
    }

    @Override
    public FieldEntity create(@Login String fieldName, int fieldSize) {
        FieldEntity field = new FieldEntity(ServiceLocator.getInstance().getService(DIService.class)
                .getComponent(UserModel.class).getLoggedUser(), fieldName, fieldSize, fieldSize);
        field.save();
        return field;
    }
}
