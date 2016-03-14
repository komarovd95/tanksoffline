package com.tanksoffline.application.models.core;

import com.tanksoffline.application.data.users.User;
import javafx.beans.property.ObjectProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface UserModel {
    void login(@NotNull(message = "Логин не может быть пустым")
               @Size(min = 4, max = 20, message = "Длина логина должна быть в пределах от {min} до {max}")
               String login,
               @NotNull(message = "Пароль не может быть пустым")
               @Size(min = 6, max = 20, message = "Длина пароля должна быть в пределах от {min} до {max}")
               String password);

    void register(@NotNull(message = "Логин не может быть пустым")
                  @Size(min = 4, max = 20, message = "Длина логина должна быть в пределах от {min} до {max}")
                  String login,
                  @NotNull(message = "Пароль не может быть пустым")
                  @Size(min = 6, max = 20, message = "Длина пароля должна быть в пределах от {min} до {max}")
                  String password,
                  boolean asManager);

    ObjectProperty<User> getLoggedUserProperty();

    enum EM {
        NE("Hello");

        private String s;

        EM(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }
    }

    class ErrorMessages {
        private static final String NOT_EMPTY_FORMAT = "%s не может быть пустым";
        private static final String SIZE_EXCEEDED_FORMAT = "Длина %s должна быть в пределах от {min} до {max}";

        public static final String LOGIN_NOT_EMPTY = String.format(NOT_EMPTY_FORMAT, "Логин");
        public static final String LOGIN_SIZE_EXCEEDED = String.format(SIZE_EXCEEDED_FORMAT, "логина");

        public static final String PASS_NOT_EMPTY = String.format(NOT_EMPTY_FORMAT, "Пароль");
        public static final String PASS_SIZE_EXCEEDED = String.format(NOT_EMPTY_FORMAT, "пароля");

    }
}
