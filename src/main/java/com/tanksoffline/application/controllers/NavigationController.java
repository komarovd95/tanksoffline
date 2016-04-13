package com.tanksoffline.application.controllers;

import com.tanksoffline.application.App;
import com.tanksoffline.application.utils.ApplicationStage;
import com.tanksoffline.application.utils.Navigation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class NavigationController implements Navigation {
    private Stack<ApplicationStage> stageStack;
    private Queue<Object> messagesQueue;

    public NavigationController() {
        this.stageStack = new Stack<>();
        this.messagesQueue = new LinkedList<>();
    }

    @Override
    public void forward(String stageName) {
        if (!stageStack.empty()) {
            stageStack.peek().setChildren(new ArrayList<>(
                    App.getInstance().getContentPane().getChildren()));
            stageStack.peek().setStyle(App.getInstance().getContentPane().getStyle());
        }
        stageStack.push(new ApplicationStage(stageName));
    }

    @Override
    public void back() {
        try {
            stageStack.pop();
            App.getInstance().setContent(stageStack.peek().getChildren(), stageStack.peek().getStyle());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getNavigationInfo() {
        return (T) messagesQueue.poll();
    }

    @Override
    public <T> void setNavigationInfo(T info) {
        messagesQueue.offer(info);
    }
}
