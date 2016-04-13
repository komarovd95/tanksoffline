package com.tanksoffline.application.models.core;

import com.tanksoffline.application.data.fields.Field;
import com.tanksoffline.core.utils.validation.Login;

public interface FieldModel extends DataModel<Field> {
    Field create(@Login String fieldName, int fieldSize);
}
