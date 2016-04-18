package persistence;

import com.tanksoffline.core.data.DomainEntity;

import java.util.Date;

public class InheritedEntity implements DomainEntity<Long> {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private String name;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Long getVersion() {
        return null;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
