package tanks.app.validators;

public class LoginValidator implements Validator<String> {
    private String validationMessage = "OK";

    @Override
    public boolean isValid(String s) {
        if (s != null && s.length() > 0 && s.length() <= 20) {
            return true;
        } else {
            validationMessage =  (s == null || s.length() == 0) ? "Login can't be blank" : "Too long login ( > 20 )";
            return false;
        }
    }

    @Override
    public String getValidationMessage() {
        return validationMessage;
    }
}
