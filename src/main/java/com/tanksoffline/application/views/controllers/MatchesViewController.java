package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.app.App;
import com.tanksoffline.application.data.Match;
import com.tanksoffline.application.data.User;
import com.tanksoffline.application.entities.MatchEntity;
import com.tanksoffline.application.entities.search.MatchSearch;
import com.tanksoffline.application.entities.search.UserSearch;
import com.tanksoffline.application.services.LoginService;
import com.tanksoffline.application.utils.TableDataBuilder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MatchesViewController implements Initializable, PartialView {
    @FXML
    public TableView<MatchEntity> table;

    @FXML
    public TableColumn<MatchEntity, Long> idColumn;

    @FXML
    public TableColumn<MatchEntity, String> nameColumn;

    @FXML
    public TableColumn<MatchEntity, String> fieldColumn;

    @FXML
    public TableColumn<MatchEntity, String> dateColumn;

    @FXML
    public TableColumn<MatchEntity, String> resultColumn;

    @FXML
    public CheckBox matchFilter;

    @FXML
    public Button backBtn;

    public MatchesViewController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getId()));
        nameColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getUser().getLogin()));
        fieldColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>((param.getValue().getField() == null)
                        ? "" : param.getValue().getField().getName()));
        dateColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(
                        new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(param.getValue().getCreatedAt())));
        resultColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getResult().toString()));

        User loggedUser = App.getService(LoginService.class).getLoggedUserProperty().get();

        if (!loggedUser.isManager()) {
            matchFilter.setSelected(true);
            matchFilter.setDisable(true);
        }

        TableDataBuilder<MatchEntity> tableDataBuilder = new TableDataBuilder<>();
        tableDataBuilder
                .setBuiltData(() -> {
                    List<? extends Match> matches = (loggedUser.isManager())
                            ? new MatchSearch().findAll()
                            : new MatchSearch().findBy(Collections.singletonMap("user",
                                    new UserSearch().findBy("login", loggedUser.getLogin())));
                    return matches.stream().map(m -> (MatchEntity) m).collect(Collectors.toList());
                })
                .setFilter(matchFilter.selectedProperty(),
                        (u, b) -> b && u.getUser().equals(loggedUser) || !b)
                .setSorted(table.comparatorProperty());

        table.setItems(tableDataBuilder.build());
        table.setPlaceholder(new Label("Нет данных"));

        backBtn.setOnAction(event -> MatchesViewController.this.onBackClick());
    }
}
