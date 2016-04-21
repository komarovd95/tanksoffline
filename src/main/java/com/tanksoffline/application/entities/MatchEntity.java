package com.tanksoffline.application.entities;

import com.tanksoffline.application.data.Match;
import com.tanksoffline.application.data.User;
import com.tanksoffline.core.data.DomainObject;

public class MatchEntity extends DomainObject implements Match {
    private UserEntity user;
    private Result result;

    public MatchEntity() {}

    public MatchEntity(UserEntity user, Result result) {
        this.user = user;
        this.result = result;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Result getResult() {
        return result;
    }
}
