package com.tanksoffline.application.controllers;

import com.tanksoffline.application.app.App;
import com.tanksoffline.application.utils.Direction;
import com.tanksoffline.application.models.game.GameModel;
import com.tanksoffline.application.models.game.TankModel;

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

    public void onShoot(TankModel tankModel) {
        tankModel.shoot();
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public boolean couldMove(TankModel tankModel, Direction direction) {
        return couldMove(tankModel.getXPosition(), tankModel.getYPosition(), direction);
    }

    public boolean couldMove(int x, int y, Direction direction) {
        if (gameModel.getField().hasBorder(x, y, direction)) {
            return false;
        }

        TankModel tm;
        switch (direction) {
            case TOP:
                tm = gameModel.getTank(x, y - 1);
                break;
            case LEFT:
                tm = gameModel.getTank(x - 1, y);
                break;
            case RIGHT:
                tm = gameModel.getTank(x + 1, y);
                break;
            case BOTTOM:
                tm = gameModel.getTank(x, y + 1);
                break;
            default:
                tm = null;
                break;
        }

        return tm == null;
    }
}
