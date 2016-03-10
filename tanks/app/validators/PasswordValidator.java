package tanks.app.validators;

public class PasswordValidator extends LoginValidator {
    @Override
    public String getValidationMessage() {
        return super.getValidationMessage().replace("Login", "Password").replace("login", "password");
    }
}
