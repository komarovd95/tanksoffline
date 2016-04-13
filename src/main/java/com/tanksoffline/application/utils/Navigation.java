package com.tanksoffline.application.utils;

public interface Navigation {
    void forward(String stageName);
    void back();

    <T> T getNavigationInfo();
    <T> void setNavigationInfo(T info);
}
