package com.tanksoffline.application.data;

public interface Match {
    User getUser();
    Result getResult();
    Field getField();

    enum Result {
        WIN("ВЫИГРЫШ"), LOSE("ПРОИГРЫШ");

        private String message;

        Result(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}
