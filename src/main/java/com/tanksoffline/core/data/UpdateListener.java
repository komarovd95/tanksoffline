package com.tanksoffline.core.data;

import org.hibernate.HibernateException;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.event.spi.SaveOrUpdateEventListener;

public class UpdateListener implements SaveOrUpdateEventListener {
    @Override
    public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
        ActiveRecord ar = (ActiveRecord) event.getEntity();
        ar.preUpdate();
    }
}
