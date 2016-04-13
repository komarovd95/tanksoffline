package com.tanksoffline.application.views.controllers;


import javafx.fxml.Initializable;

public interface FieldView extends Initializable, PartialView {
    void onCreateClick();

    void onEditClick();

    void onSaveClick();

    void onRemoveClick();
}
