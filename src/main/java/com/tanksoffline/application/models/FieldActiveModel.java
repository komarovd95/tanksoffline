package com.tanksoffline.application.models;

import com.tanksoffline.application.data.Field;
import com.tanksoffline.application.data.FieldCell;
import com.tanksoffline.application.entities.FieldEntity;
import com.tanksoffline.application.utils.Direction;
import com.tanksoffline.core.mvc.BaseModel;
import com.tanksoffline.core.utils.observer.SimpleProperty;
import com.tanksoffline.core.validation.ValidationContext;
import com.tanksoffline.core.validation.ValidationContextBuilder;
import com.tanksoffline.core.validation.ValidationContextException;

public class FieldActiveModel extends BaseModel<Field> implements Field {
    public FieldActiveModel(String name, int width, int height) {
        validation(name, width, height);
        this.modelProperty = new SimpleProperty<>(new FieldEntity(name, width, height));
    }

    public FieldActiveModel(Field field) {
        validation(field.getName(), field.getWidth(), field.getHeight());
        this.modelProperty = new SimpleProperty<>(field);
    }

    private static void validation(String name, int width, int height) {
        ValidationContext context = ValidationContextBuilder.create()
                .validate("fieldName", name)
                .validate("fieldSize", width)
                .validate("fieldSize", height)
                .build();
        if (!context.isValid()) {
            throw new ValidationContextException(context.getErrorMessages());
        }
    }

    @Override
    public boolean hasBorder(int i, int j, Direction direction) {
        return modelProperty.get().hasBorder(i, j, direction);
    }

    @Override
    public void setBorder(int i, int j, Direction direction) {
        modelProperty.get().setBorder(i, j, direction);
        modelProperty.set(modelProperty.get());
    }

    @Override
    public void removeBorder(int i, int j, Direction direction) {
        modelProperty.get().removeBorder(i, j, direction);
        modelProperty.set(modelProperty.get());
    }

    @Override
    public FieldCell[][] getFieldCells() {
        return modelProperty.get().getFieldCells();
    }

    @Override
    public FieldCell getFieldCell(int i, int j) {
        return modelProperty.get().getFieldCell(i, j);
    }

    @Override
    public int getWidth() {
        return modelProperty.get().getWidth();
    }

    @Override
    public int getHeight() {
        return modelProperty.get().getHeight();
    }

    @Override
    public String getName() {
        return modelProperty.get().getName();
    }

    @Override
    public void setName(String name) {
        modelProperty.get().setName(name);
    }
}
