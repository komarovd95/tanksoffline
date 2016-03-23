package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.App;
import com.tanksoffline.application.controllers.UserActionController;
import com.tanksoffline.application.data.users.User;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class UsersViewController implements Initializable {
    @FXML
    private TableView<User> table;

    @FXML
    private TableColumn<User, Long> idColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> passColumn;

    @FXML
    private TableColumn<User, String> typeColumn;

    @FXML
    private TableColumn<User, Date> createColumn;

    @FXML
    private TableColumn<User, Date> updateColumn;

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

    private ObservableList<User> users;

    private UserActionController actionController;

    public UsersViewController() {
        this.actionController = new UserActionController();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getId()));
        nameColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getLogin()));
        passColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPassword()));
        typeColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().isManager() ? "Manager" : "User"));
        createColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getCreatedAt()));
        updateColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getUpdatedAt()));

        try {
            users = new Task<ObservableList<User>>() {
                @Override
                protected ObservableList<User> call() throws Exception {
                    return FXCollections.observableArrayList(actionController.onFindAll().call());
                }
            }.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        FilteredList<User> filteredList = new FilteredList<>(users, u -> true);

        ObservableList<Predicate<? super User>> predicates = FXCollections.observableArrayList();
        predicates.add(user -> true);
        predicates.add(user -> true);
        predicates.add(user -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            predicates.set(0, user -> newValue == null || "".equals(newValue) || user.getLogin().contains(newValue));
        });

        userShown.selectedProperty().addListener((observable, oldValue, newValue) -> {
            predicates.set(1, user -> newValue && user.isManager() || oldValue);
        });

        managerShown.selectedProperty().addListener((observable, oldValue, newValue) -> {
            predicates.set(2, user -> newValue && !user.isManager() || oldValue);
        });

        predicates.addListener((ListChangeListener<Predicate<? super User>>) c -> {
            Predicate<User> superPredicate = u -> true;
            for (Predicate<? super User> p : predicates) {
                superPredicate = superPredicate.and(p);
            }
            filteredList.setPredicate(superPredicate);
        });

        SortedList<User> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedList);

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean willDisabled = newValue == null;
            updateBtn.setDisable(willDisabled);
            removeBtn.setDisable(willDisabled);
        });
    }

    public void onRemove() {
        User currentUser = table.getSelectionModel().selectedItemProperty().get();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы уверены, что хотите удалить пользователя " +
            currentUser.getLogin());
        alert.showAndWait().filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    try {
                        if (currentUser.getLogin().equals(
                                App.getInstance().getUserModel().getLoggedUser().getLogin())) {
                            actionController.onLogout().call();
                        }
                        actionController.onRemove(currentUser).call();
                        users.remove(currentUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public void onChangeUser() {
        User currentUser = table.getSelectionModel().selectedItemProperty().get();
        Stage changeStage = new Stage();
        changeStage.setTitle("Change user");
        changeStage.setResizable(false);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource("/views/change_user.fxml"));

        Parent changeRoot;
        try {
            changeRoot = loader.load();
            changeStage.setScene(new Scene(changeRoot));
            ((ChangeUserViewController) loader.getController()).setCurrentUser(currentUser);
            ((ChangeUserViewController) loader.getController()).setStage(changeStage);

            changeStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        changeStage.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && oldValue) {
                users.set(table.getSelectionModel().getSelectedIndex(), currentUser);
            }
        });
    }

    public void onBack() {
        App.getInstance().getApplicationController().back();
    }
}
