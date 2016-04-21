package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.app.App;
import com.tanksoffline.application.data.Field;
import com.tanksoffline.core.mvc.ActionController;
import com.tanksoffline.application.controllers.FieldActionController;
import com.tanksoffline.application.utils.TaskFactory;
import com.tanksoffline.core.services.DIService;
import com.tanksoffline.core.validation.ValidationContextException;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FieldCreateViewController implements Initializable {
    private Service<Field> fieldCreationService;
    private ActionController<Field> actionController;

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
        fieldCreationService = new Service<Field>() {
            @Override
            protected Task<Field> createTask() {
                Map<String, Object> params = new HashMap<>();
                params.put("name", nameValue.getText().trim());
                params.put("width", (int) sizeSlider.getValue());
                params.put("height", (int) sizeSlider.getValue());
                return new TaskFactory<>(actionController.create(params)).create();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                App.getService(DIService.class).addComponent("new_field", this.getValue());

                cancelBtn.getScene().getWindow().getOnCloseRequest().handle(
                        new WindowEvent(cancelBtn.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
            }

            @Override
            protected void failed() {
                super.failed();
                Throwable t = this.exceptionProperty().get();
                if (t instanceof ValidationContextException) {
                    Map<String, String> errors = ((ValidationContextException) t).getErrors();
                    nameLabel.setText(errors.get("name"));
                } else {
                    throw new RuntimeException(t);
                }
            }
        };
    }

    public void onCancelClick() {
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }

    public void onAcceptClick() {
        if (fieldCreationService.getState() == Worker.State.READY) {
            fieldCreationService.start();
        } else {
            fieldCreationService.restart();
        }
    }
}
