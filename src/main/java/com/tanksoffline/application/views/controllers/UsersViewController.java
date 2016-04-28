package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.app.App;
import com.tanksoffline.application.controllers.UserActionController;
import com.tanksoffline.application.data.User;
import com.tanksoffline.application.entities.UserEntity;
import com.tanksoffline.application.entities.search.UserSearch;
import com.tanksoffline.application.utils.TableDataBuilder;
import com.tanksoffline.core.mvc.ActionController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private TableColumn<UserEntity, String> createColumn;

    @FXML
    private TableColumn<UserEntity, String> updateColumn;

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

    private App app;

    public UsersViewController() {
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
                new SimpleObjectProperty<>(param.getValue().isManager() ? "Manager" : "User"));
        createColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(param.getValue().getCreatedAt())));
        updateColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(param.getValue().getCreatedAt())));

        TableDataBuilder<UserEntity> tableDataBuilder = new TableDataBuilder<>();
        tableDataBuilder
                .setBuiltData(() -> {
                    List<? extends User> users = new UserSearch().findAll();
                    return users.stream().map(u -> (UserEntity) u).collect(Collectors.toList());
                })
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
            boolean willDisabled = (newValue == null);
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
                        ActionController<User> actionController = new UserActionController(currentUserEntity);
                        actionController.remove().call();
                        User u = app.getLoggedUserProperty().get();
                        if (app.getLoggedUserProperty().get().equals(currentUserEntity)) {
                            actionController.destroy().call();
                        }
                        userEntities.remove(currentUserEntity);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public void onChangeUser() {
        UserEntity currentUserEntity = table.getSelectionModel().selectedItemProperty().get();
        app.getApplicationController().onUserChange(currentUserEntity, () ->
                userEntities.set(table.getSelectionModel().getSelectedIndex(),
                        new UserSearch().findOne(currentUserEntity.getId())));
    }
}
