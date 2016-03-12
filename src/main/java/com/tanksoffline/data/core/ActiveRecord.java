package com.tanksoffline.data.core;

import com.tanksoffline.services.core.DataService;
import com.tanksoffline.services.core.ServiceLocator;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ActiveRecord implements Serializable {
    private static DataService dataService = ServiceLocator.getInstance().getService(DataService.class);

    protected Long id;
    protected Date createdAt;
    protected Date updatedAt;

    public Long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return (Date) createdAt.clone();
    }

    public Date getUpdatedAt() {
        return (Date) updatedAt.clone();
    }

    @PrePersist
    public void preConstruct() {
        createdAt = updatedAt = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = new Date();
    }

    public void save() {
        dataService.save(this);
    }

    public void update() {
        dataService.update(this);
    }

    public void remove() {
        dataService.remove(this);
    }

    public void fetch() {
        List<String> fieldsNames = Arrays.asList(this.getClass().getDeclaredFields()).stream()
                .map(Field::getName)
                .collect(Collectors.toList());
        dataService.fetch(this, fieldsNames.toArray(new String[fieldsNames.size()]));
    }
}
