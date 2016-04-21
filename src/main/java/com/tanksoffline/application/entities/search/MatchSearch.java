package com.tanksoffline.application.entities.search;

import com.tanksoffline.application.entities.MatchEntity;
import com.tanksoffline.core.data.Search;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;

import java.util.List;
import java.util.Map;

public class MatchSearch implements Search<MatchEntity> {
    private static final DataService dataService = ServiceLocator.getInstance().getService(DataService.class);

    @Override
    public MatchEntity findOne(Object id) {
        return dataService.findById(MatchEntity.class, id);
    }

    @Override
    public List<MatchEntity> findAll() {
        return dataService.findAll(MatchEntity.class);
    }

    @Override
    public List<MatchEntity> findBy(String query, Object... args) {
        return dataService.findBy(query, args);
    }

    @Override
    public List<MatchEntity> findBy(Map<String, Object> args) {
        return dataService.findBy(MatchEntity.class, args);
    }
}
