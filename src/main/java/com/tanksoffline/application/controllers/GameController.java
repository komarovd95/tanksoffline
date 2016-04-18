package com.tanksoffline.application.controllers;

import com.tanksoffline.application.app.App;
import com.tanksoffline.application.utils.Direction;
import com.tanksoffline.application.models.core.game.GameModel;
import com.tanksoffline.application.models.core.game.TankModel;

public class GameController {
    private GameModel gameModel;

    public GameController() {
        this.gameModel = App.getInstance().getGameModel();
    }

    public void onMove(Direction direction) {
        TankModel player = gameModel.getPlayer();
        if (couldMove(player, direction) && !player.isMoving() && !player.isRotating()) {
            if (player.getDirection() == direction) {
                player.move();
            } else {
                player.setDirection(direction);
            }
        }
    }

    public void onShoot() {
        gameModel.getPlayer().shoot();
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    private boolean couldMove(TankModel tankModel, Direction direction) {
        return !gameModel.getField().hasBorder(tankModel.getXPosition(), tankModel.getYPosition(), direction);
    }
}
