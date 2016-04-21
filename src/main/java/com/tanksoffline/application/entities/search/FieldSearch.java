package com.tanksoffline.application.entities.search;

import com.tanksoffline.application.entities.FieldEntity;
import com.tanksoffline.core.data.Search;
import com.tanksoffline.core.services.DataService;
import com.tanksoffline.core.services.ServiceLocator;

import java.util.List;
import java.util.Map;

public class FieldSearch implements Search<FieldEntity> {
    private static final DataService dataService = ServiceLocator.getInstance().getService(DataService.class);

    @Override
    public FieldEntity findOne(Object id) {
        return dataService.findById(FieldEntity.class, id);
    }

    @Override
    public List<FieldEntity> findAll() {
        return dataService.findAll(FieldEntity.class);
    }

    @Override
    public List<FieldEntity> findBy(String query, Object... args) {
        return dataService.findBy(query, args);
    }

    @Override
    public List<FieldEntity> findBy(Map<String, Object> args) {
        return dataService.findBy(FieldEntity.class, args);
    }
}
