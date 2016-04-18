package com.tanksoffline.core.data;

import java.util.Date;

public abstract class DomainObject implements DomainEntity<Long> {
    protected Long id;
    protected Long version;
    protected Date createdAt;
    protected Date updatedAt;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    protected void prePersist() {
        updatedAt = createdAt = new Date();
    }

    protected void preUpdate() {
        updatedAt = new Date();
    }
}
