package com.tanksoffline.application.models.core;

import com.tanksoffline.application.entities.FieldEntity;
import com.tanksoffline.core.utils.validation.Login;

public interface FieldModel extends DataModel<FieldEntity> {
    FieldEntity create(@Login String fieldName, int fieldSize);
}
