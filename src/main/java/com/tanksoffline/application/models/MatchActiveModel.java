package com.tanksoffline.application.models;

import com.tanksoffline.application.data.Field;
import com.tanksoffline.application.data.Match;
import com.tanksoffline.application.data.User;
import com.tanksoffline.application.entities.FieldEntity;
import com.tanksoffline.application.entities.MatchEntity;
import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.core.mvc.BaseModel;
import com.tanksoffline.core.utils.observer.SimpleProperty;

public class MatchActiveModel extends BaseModel<Match> implements Match {
    public MatchActiveModel(UserEntity user, Result result, FieldEntity field) {
        this.modelProperty = new SimpleProperty<>(new MatchEntity(user, result, field));
    }

    public MatchActiveModel(Match match) {
        modelProperty.set(match);
    }

    @Override
    public User getUser() {
        return modelProperty.get().getUser();
    }

    @Override
    public Result getResult() {
        return modelProperty.get().getResult();
    }

    @Override
    public Field getField() {
        return modelProperty.get().getField();
    }
}
