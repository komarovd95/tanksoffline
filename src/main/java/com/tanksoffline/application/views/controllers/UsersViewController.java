package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.app.App;
import com.tanksoffline.application.controllers.UserActionController;
import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.application.utils.TableDataBuilder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class UsersViewController implements Initializable, PartialView {
    @FXML
    private TableView<UserEntity> table;

    @FXML
    private TableColumn<UserEntity, Long> idColumn;

    @FXML
    private TableColumn<UserEntity, String> nameColumn;

    @FXML
    private TableColumn<UserEntity, String> passColumn;

    @FXML
    private TableColumn<UserEntity, String> typeColumn;

    @FXML
    private TableColumn<UserEntity, Date> createColumn;

    @FXML
    private TableColumn<UserEntity, Date> updateColumn;

    @FXML
    private TextField filterField;

    @FXML
    private CheckBox userShown;

    @FXML
    private CheckBox managerShown;

    @FXML
    private Button updateBtn;

    @FXML
    private Button removeBtn;

    @FXML
    private Button backBtn;

    private ObservableList<UserEntity> userEntities;

    private UserActionController actionController;

    private App app;

    public UsersViewController() {
        this.actionController = new UserActionController();
        this.app = App.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getId()));
        nameColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getLogin()));
        passColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getPassword()));
        typeColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().isManager() ? "Manager" : "UserEntity"));
        createColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getCreatedAt()));
        updateColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getUpdatedAt()));

        TableDataBuilder<UserEntity> tableDataBuilder = new TableDataBuilder<>();
        tableDataBuilder
                .setBuiltData(actionController.onFindAll())
                .setFilter(filterField.textProperty(),
                        (u, s) -> s == null || "".equals(s) || u.getLogin().contains(s))
                .setFilter(userShown.selectedProperty(),
                        (u, b) -> b && u.isManager() || !b)
                .setFilter(managerShown.selectedProperty(),
                        (u, b) -> b && !u.isManager() || !b)
                .setSorted(table.comparatorProperty());

        table.setItems(tableDataBuilder.build());
        table.setPlaceholder(new Label("Нет данных"));
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean willDisabled = newValue == null;
            updateBtn.setDisable(willDisabled);
            removeBtn.setDisable(willDisabled);
        });

        userEntities = tableDataBuilder.getBuiltData();

        backBtn.setOnAction(event -> UsersViewController.this.onBackClick());
    }

    public void onRemove() {
        UserEntity currentUserEntity = table.getSelectionModel().selectedItemProperty().get();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Вы уверены, что хотите удалить пользователя " + currentUserEntity.getLogin() + "?");
        alert.showAndWait().filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    try {
                        if (currentUserEntity.getLogin().equals(
                                app.getUserModel().getLoggedUser().getLogin())) {
                            actionController.onDestroy().call();
                        }
                        actionController.onRemove(currentUserEntity).call();
                        userEntities.remove(currentUserEntity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public void onChangeUser() {
        UserEntity currentUserEntity = table.getSelectionModel().selectedItemProperty().get();
        app.getApplicationController().onUserChange(currentUserEntity, () ->
                userEntities.set(table.getSelectionModel().getSelectedIndex(),
                        app.getUserModel().findOne(currentUserEntity.getId())));
    }

    public void onBackClick() {
        app.getNavigation().back();
    }
}
