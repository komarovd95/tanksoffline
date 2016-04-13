package com.tanksoffline.application.utils;

import javafx.scene.Node;

import java.util.List;

public class ApplicationStage {
    private String stageName;
    private List<? extends Node> children;
    private String style;

    public ApplicationStage(String stageName) {
        this.stageName = stageName;
    }

    public String getStageName() {
        return stageName;
    }

    public List<? extends Node> getChildren() {
        return children;
    }

    public void setChildren(List<? extends Node> children) {
        this.children = children;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof ApplicationStage
                && this.stageName.equals(((ApplicationStage) o).getStageName());
    }
}
