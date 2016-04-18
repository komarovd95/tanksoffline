package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.app.App;
import com.tanksoffline.core.mvc.ActionController;
import com.tanksoffline.application.controllers.FieldActionController;
import com.tanksoffline.application.entities.FieldEntity;
import com.tanksoffline.application.utils.TaskFactory;
import com.tanksoffline.core.services.ValidationService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.validation.ValidationException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FieldCreateViewController implements Initializable {
    private Service<FieldEntity> fieldCreationService;
    private ActionController<FieldEntity> actionController;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField nameValue;

    @FXML
    private Slider sizeSlider;

    @FXML
    private Button createBtn;

    @FXML
    private Button cancelBtn;

    public FieldCreateViewController() {
        this.actionController = new FieldActionController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fieldCreationService = new Service<FieldEntity>() {
            @Override
            protected Task<FieldEntity> createTask() {
                Map<String, Object> params = new HashMap<>();
                params.put("name", nameValue.getText().trim());
                params.put("size", (int) sizeSlider.getValue());
                return new TaskFactory<>(actionController.onCreate(params)).create();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                App.getInstance().getNavigation().setNavigationInfo(this.getValue());
                cancelBtn.getScene().getWindow().getOnCloseRequest().handle(
                        new WindowEvent(cancelBtn.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
            }

            @Override
            protected void failed() {
                super.failed();
                Throwable t = this.exceptionProperty().get();
                ValidationService service = App.getService(ValidationService.class);
                if (t instanceof ValidationException) {
                    Map<String, String> errors = service.getErrorClasses();
                    nameLabel.setText(errors.get("Login").replace("Логин", "Название"));
                } else {
                    throw new RuntimeException(t);
                }
                this.reset();
            }
        };
    }

    public void onCancelClick() {
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }

    public void onAcceptClick() {
        fieldCreationService.start();
    }
}
