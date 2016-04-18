package com.tanksoffline.application.models;

import com.tanksoffline.application.data.Field;
import com.tanksoffline.application.data.FieldCell;
import com.tanksoffline.application.data.User;
import com.tanksoffline.application.entities.FieldEntity;
import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.application.utils.Direction;
import com.tanksoffline.core.mvc.ActiveModel;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;
import com.tanksoffline.core.utils.observer.Observer;
import com.tanksoffline.core.utils.observer.Property;
import com.tanksoffline.core.utils.observer.SimpleProperty;
import com.tanksoffline.core.validation.ValidationContext;
import com.tanksoffline.core.validation.ValidationContextBuilder;
import com.tanksoffline.core.validation.ValidationContextException;

public class FieldActiveModel implements Field, ActiveModel<Field> {
    private static final DataService dataService = ServiceLocator.getInstance().getService(DataService.class);

    private Property<Field> fieldProperty;

    public FieldActiveModel(UserEntity owner, String name, int width, int height) {
        ValidationContext context = ValidationContextBuilder.create()
                .validate("fieldName", name)
                .validate("fieldSize", width)
                .validate("fieldSize", height)
                .build();
        if (!context.isValid()) {
            throw new ValidationContextException(context.getErrorMessages());
        }

        fieldProperty = new SimpleProperty<>(new FieldEntity(owner, name, width, height));
    }

    @Override
    public Field save() {
        fieldProperty.set(dataService.save(fieldProperty.get()));
        return fieldProperty.get();
    }

    @Override
    public Field update() {
        return null;
    }

    @Override
    public Field remove() {
        return null;
    }

    @Override
    public Field refresh() {
        return null;
    }

    @Override
    public boolean hasBorder(int i, int j, Direction direction) {
        return false;
    }

    @Override
    public void setBorder(int i, int j, Direction direction) {

    }

    @Override
    public void removeBorder(int i, int j, Direction direction) {

    }

    @Override
    public FieldCell[][] getFieldCells() {
        return new FieldCell[0][];
    }

    @Override
    public FieldCell getFieldCell(int i, int j) {
        return null;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public User getOwner() {
        return null;
    }

    @Override
    public void addObserver(Observer<? super Field> observer) {

    }

    @Override
    public void removeObserver(Observer<? super Field> observer) {

    }
}
