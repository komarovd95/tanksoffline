package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.App;
import com.tanksoffline.application.controllers.ActionController;
import com.tanksoffline.application.controllers.FieldActionController;
import com.tanksoffline.application.data.fields.Direction;
import com.tanksoffline.application.data.fields.Field;
import com.tanksoffline.application.data.fields.FieldCell;
import com.tanksoffline.application.utils.BoundRenderer;
import com.tanksoffline.application.utils.Renderer;
import com.tanksoffline.application.utils.TableDataBuilder;
import com.tanksoffline.application.utils.TaskFactory;
import com.tanksoffline.application.views.drawing.DefaultCellRenderer;
import com.tanksoffline.application.views.drawing.DefaultFieldRenderer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;

public class FieldsViewController implements FieldView {
    private App app;
    private ActionController<Field> actionController;
    private ObservableList<Field> fields;
    private Renderer<Field> renderer;
    private ObjectProperty<Pair<Integer, Integer>> selectedCell;

    @FXML
    private ListView<Field> fieldList;

    @FXML
    private Button createBtn;

    @FXML
    private Button editBtn;

    @FXML
    private Button removeBtn;

    @FXML
    private Button backBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Canvas canvas;

    @FXML
    private ToggleButton topTgl;

    @FXML
    private ToggleButton botTgl;

    @FXML
    private ToggleButton leftTgl;

    @FXML
    private ToggleButton rightTgl;

    public FieldsViewController() {
        this.app = App.getInstance();
        this.actionController = new FieldActionController();
        this.selectedCell = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createBtn.setDisable(!app.getUserModel().getLoggedUser().isManager());
        editBtn.setDisable(true);
        removeBtn.setDisable(true);
        brokeInterface(true);

        backBtn.setOnAction(event -> FieldsViewController.this.onBackClick());

        TableDataBuilder<Field> dataBuilder = new TableDataBuilder<>();
        dataBuilder.setBuiltData(actionController.onFindAll());

        fieldList.setItems(dataBuilder.build());
        fieldList.setPlaceholder(new Label("Нет данных"));

        fields = dataBuilder.getBuiltData();

        BoundRenderer<FieldCell> fieldCellBoundRenderer = new DefaultCellRenderer() {
            @Override
            public void render(FieldCell fieldCell, GraphicsContext gc, Bounds bounds) {
                int x = (int) (bounds.getMinX() / bounds.getWidth());
                int y = (int) (bounds.getMinY() / bounds.getHeight());

                if (selectedCell.get() != null
                                && x == selectedCell.get().getKey()
                                && y == selectedCell.get().getValue()) {
                    gc.setFill(Color.LIGHTGREEN);
                } else {
                    gc.setFill(Color.BURLYWOOD);
                }

                super.render(fieldCell, gc, bounds);
            }
        };

        renderer = new DefaultFieldRenderer(fieldCellBoundRenderer);

        fieldList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean willDisabled = newValue == null;
            editBtn.setDisable(willDisabled);
            removeBtn.setDisable(willDisabled);

            renderer.render(newValue, canvas.getGraphicsContext2D());
        });

        canvas.setOnMouseClicked(event -> {
            Field field = getCurrentField();

            if (field == null) return;

            double cellSize = Math.floor(canvas.getWidth() / field.getWidth());
            int x = (int) (event.getX() / cellSize);
            int y = (int) (event.getY() / cellSize);

            selectedCell.setValue(new Pair<>(x, y));

            System.out.println(x + " " + y);
        });

        selectedCell.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                FieldCell cell = getCurrentField().getCell(newValue.getKey(), newValue.getValue());

                if (!topTgl.isDisabled()) topTgl.setSelected(cell.hasBorder(Direction.TOP));
                if (!botTgl.isDisabled()) botTgl.setSelected(cell.hasBorder(Direction.BOTTOM));
                if (!leftTgl.isDisabled()) leftTgl.setSelected(cell.hasBorder(Direction.LEFT));
                if (!rightTgl.isDisabled()) rightTgl.setSelected(cell.hasBorder(Direction.RIGHT));
            }

            renderer.render(getCurrentField(), canvas.getGraphicsContext2D());
        });

        ChangeListener<? super Boolean> toggleListener = (observable, oldValue, newValue) -> {
            ToggleButton source = (ToggleButton) ((BooleanProperty) observable).getBean();
            Direction direction = null;

            if (source == topTgl) direction = Direction.TOP;
            else if (source == botTgl) direction = Direction.BOTTOM;
            else if (source == leftTgl) direction = Direction.LEFT;
            else if (source == rightTgl) direction = Direction.RIGHT;
            else return;

            Field field = getCurrentField();

            if (newValue) {
                field.setBorder(selectedCell.get().getKey(), selectedCell.get().getValue(), direction);
            } else {
                field.removeBorder(selectedCell.get().getKey(), selectedCell.get().getValue(), direction);
            }

            renderer.render(getCurrentField(), canvas.getGraphicsContext2D());
        };

        topTgl.selectedProperty().addListener(toggleListener);
        botTgl.selectedProperty().addListener(toggleListener);
        leftTgl.selectedProperty().addListener(toggleListener);
        rightTgl.selectedProperty().addListener(toggleListener);
    }

    @Override
    public void onCreateClick() {
        app.getApplicationController().onFieldCreate(() -> {
            fields.add(app.getNavigation().getNavigationInfo());
            fieldList.getSelectionModel().select(fields.size() - 1);
        });
    }

    @Override
    public void onEditClick() {
        brokeInterface(false);
    }

    @Override
    public void onSaveClick() {
        Service<Field> saveService = new Service<Field>() {
            @Override
            protected Task<Field> createTask() {
                return new TaskFactory<>(
                        actionController.onUpdate(getCurrentField(), null))
                        .create();
            }

            @Override
            protected void succeeded() {
                selectedCell.set(null);
                brokeInterface(true);
            }

            @Override
            protected void failed() {
                this.reset();
            }
        };

        if (saveService.getState() == Worker.State.READY) {
            saveService.start();
        } else {
            saveService.restart();
        }
    }

    @Override
    public void onRemoveClick() {
        Service<Field> removeService = new Service<Field>() {
            @Override
            protected Task<Field> createTask() {
                return new TaskFactory<>(actionController.onRemove(getCurrentField())).create();
            }

            @Override
            protected void succeeded() {
                selectedCell.set(null);
                fields.remove(this.getValue());
            }
        };

        if (removeService.getState() == Worker.State.READY) {
            removeService.start();
        } else {
            removeService.restart();
        }
    }

    private Field getCurrentField() {
        return fieldList.getSelectionModel().getSelectedItem();
    }

    private void brokeInterface(boolean willBroken) {
        topTgl.setDisable(willBroken);
        botTgl.setDisable(willBroken);
        leftTgl.setDisable(willBroken);
        rightTgl.setDisable(willBroken);
        saveBtn.setDisable(willBroken);
    }
}
