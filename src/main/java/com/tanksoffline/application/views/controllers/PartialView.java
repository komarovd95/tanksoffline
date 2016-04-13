package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.App;

public interface PartialView {
    default void onBackClick() {
        App.getInstance().getNavigation().back();
    }
}
