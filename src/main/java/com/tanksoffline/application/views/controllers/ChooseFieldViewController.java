package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.app.App;
import com.tanksoffline.application.data.Field;
import com.tanksoffline.core.mvc.ActionController;
import com.tanksoffline.application.controllers.FieldActionController;
import com.tanksoffline.application.entities.FieldEntity;
import com.tanksoffline.application.data.FieldCell;
import com.tanksoffline.application.utils.BoundRenderer;
import com.tanksoffline.application.utils.Renderer;
import com.tanksoffline.application.utils.TableDataBuilder;
import com.tanksoffline.application.views.drawing.DefaultCellRenderer;
import com.tanksoffline.application.views.drawing.DefaultFieldRenderer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ChooseFieldViewController implements FieldView {
    private ActionController<Field> actionController;
    private Renderer<FieldEntity> fieldRenderer;
    private ObjectProperty<Pair<Integer, Integer>> playerSpawnCell;
    private ObjectProperty<Pair<Integer, Integer>> enemySpawnCell;

    @FXML
    private ListView<FieldEntity> fieldList;

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

    public ChooseFieldViewController() {
        this.actionController = new FieldActionController();
        this.playerSpawnCell = new SimpleObjectProperty<>();
        this.enemySpawnCell = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editBtn.setVisible(false);
        removeBtn.setVisible(false);
        saveBtn.setVisible(false);
        topTgl.setVisible(false);
        botTgl.setVisible(false);
        leftTgl.setVisible(false);
        rightTgl.setVisible(false);

        createBtn.setText("Начать");
        createBtn.setStyle("-fx-base: lightgreen");
        createBtn.setDisable(true);

        backBtn.setOnAction(event -> ChooseFieldViewController.this.onBackClick());

        TableDataBuilder<FieldEntity> dataBuilder = new TableDataBuilder<>();
        dataBuilder.setBuiltData(() -> {
            List<? extends Field> fields = actionController.list().call();
            return fields.stream().map(f -> (FieldEntity) f).collect(Collectors.toList());
        });

        fieldList.setItems(dataBuilder.build());

        BoundRenderer<FieldCell> fieldCellBoundRenderer = new DefaultCellRenderer() {
            @Override
            public void render(FieldCell fieldCell, GraphicsContext gc, Bounds bounds) {
                int x = (int) (bounds.getMinX() / bounds.getWidth());
                int y = (int) (bounds.getMinY() / bounds.getHeight());

                if (playerSpawnCell.get() != null && playerSpawnCell.get().getKey() == x
                        && playerSpawnCell.get().getValue() == y) {
                    gc.setFill(Color.LIGHTGREEN);
                } else if (enemySpawnCell.get() != null && enemySpawnCell.get().getKey() == x
                        && enemySpawnCell.get().getValue() == y) {
                    gc.setFill(Color.LIGHTCORAL);
                } else {
                    gc.setFill(Color.BURLYWOOD);
                }

                super.render(fieldCell, gc, bounds);
            }
        };
        fieldRenderer = new DefaultFieldRenderer(fieldCellBoundRenderer);

        fieldList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            fieldRenderer.render(newValue, canvas.getGraphicsContext2D());
        });

        canvas.setOnMouseClicked(event -> {
            FieldEntity field = getCurrentField();

            if (field == null) return;

            double cellSize = Math.floor(canvas.getWidth() / field.getWidth());
            int x = (int) (event.getX() / cellSize);
            int y = (int) (event.getY() / cellSize);

            System.out.println(x + " " + y);

            if (event.getButton() == MouseButton.PRIMARY) {
                setCell(playerSpawnCell, enemySpawnCell, x, y);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                setCell(enemySpawnCell, playerSpawnCell, x, y);
            }
        });

        ChangeListener<? super Pair<Integer, Integer>> cellChangeListener = (observable, oldValue, newValue) -> {
            createBtn.setDisable(playerSpawnCell.get() == null || enemySpawnCell.get() == null);

            fieldRenderer.render(getCurrentField(), canvas.getGraphicsContext2D());
        };

        playerSpawnCell.addListener(cellChangeListener);
        enemySpawnCell.addListener(cellChangeListener);
    }

    @Override
    public void onCreateClick() {
        App.getInstance().getApplicationController().onGameView(getCurrentField(),
                playerSpawnCell.get(), enemySpawnCell.get());
    }

    @Override
    public void onEditClick() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onSaveClick() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onRemoveClick() {
        throw new UnsupportedOperationException();
    }

    private FieldEntity getCurrentField() {
        return fieldList.getSelectionModel().getSelectedItem();
    }

    private static void setCell(ObjectProperty<Pair<Integer, Integer>> cell1,
                                ObjectProperty<Pair<Integer, Integer>> cell2, int x, int y) {
        if (cell1.get() != null && cell1.get().getKey() == x
                && cell1.get().getValue() == y) {
            cell1.set(null);
        } else {
            if (cell2.get() != null && cell2.get().getKey() == x
                    && cell2.get().getValue() == y) {
                cell2.set(null);
            }
            cell1.set(new Pair<>(x, y));
        }
    }
}
