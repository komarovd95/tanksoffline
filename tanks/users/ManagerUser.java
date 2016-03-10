package tanks.users;

import tanks.field.Field;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("M")
public class ManagerUser extends User {
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Field> fields;

    public ManagerUser(String login, String password) {
        super(login, password);
        fields = new ArrayList<>();
    }

    public ManagerUser() {
        fields = new ArrayList<>();
    }

    public Field getField(int index) {
        return fields.get(index);
    }

    public Field setField(int index, Field field) {
        return fields.set(index, field);
    }

    public boolean addField(Field field) {
        return fields.add(field);
    }
}
