package tanks.app.validators;

public interface Validator<T> {
    boolean isValid(T t);
    String getValidationMessage();
}
