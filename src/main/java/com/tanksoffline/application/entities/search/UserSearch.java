package com.tanksoffline.application.entities.search;

import com.tanksoffline.application.data.User;
import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.core.data.Search;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;

import java.util.List;
import java.util.Map;

public class UserSearch implements Search<User> {
    private static final DataService dataService = ServiceLocator.getInstance().getService(DataService.class);

    @Override
    public User findOne(Object id) {
        return dataService.findById(UserEntity.class, id);
    }

    @Override
    public List<? extends User> findAll() {
        return dataService.findAll(UserEntity.class);
    }

    @Override
    public List<? extends User> findBy(String query, Object... args) {
        return dataService.findBy(query, args);
    }

    @Override
    public List<? extends User> findBy(Map<String, Object> args) {
        return dataService.findBy(UserEntity.class, args);
    }
}
