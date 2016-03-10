package tanks.users;

import tanks.utils.SecurityUtil;
import tanks.utils.persistence.DomainObject;

import javax.persistence.*;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Entity
@Inheritance
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("U")
@Table(name = "users")
public class User extends DomainObject implements Serializable {
    private static final Logger logger = Logger.getLogger("User");

    @Column(name = "login", unique = true, length = 20, nullable = false)
    private String login;

    @Column(name = "pass_digest", nullable = false)
    private String password;

    public User(String login, String password) {
        this.login = login;
        setPassword(password);
    }

    public User() {}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordDigest() {
        return password;
    }

    public void setPassword(String password) {
        try {
            this.password = SecurityUtil.getPasswordDigest(password);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.log(Level.SEVERE, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
