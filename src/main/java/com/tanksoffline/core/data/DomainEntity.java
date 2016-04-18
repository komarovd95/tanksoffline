package com.tanksoffline.core.data;

import java.io.Serializable;
import java.util.Date;

public interface DomainEntity<ID extends Serializable> extends Serializable {
    ID getId();
    Long getVersion();
    Date getCreatedAt();
    Date getUpdatedAt();
}
