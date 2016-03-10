package tanks.utils.persistence;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@MappedSuperclass
public abstract class DomainObject {
    private static final Logger logger = Logger.getLogger(DomainObject.class.getName());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updatedAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Long getId() {
        return id;
    }

    @PrePersist
    void onCreate() {
        createdAt = updatedAt = new Date();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = new Date();
    }

    public static Object getAttribute(Object obj, String attrName) {
        Class<?> clazz = obj.getClass();
        try {
            Field field = clazz.getDeclaredField(attrName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new RuntimeException("Cannot get attribute with name " + attrName, e);
        }
    }
}
