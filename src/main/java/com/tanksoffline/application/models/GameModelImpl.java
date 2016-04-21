package com.tanksoffline.application.models;

import com.tanksoffline.application.utils.Direction;
import com.tanksoffline.application.entities.FieldEntity;
import com.tanksoffline.application.models.game.GameModel;
import com.tanksoffline.application.models.game.TankModel;
import com.tanksoffline.core.utils.observer.Observable;
import com.tanksoffline.core.utils.observer.Property;
import com.tanksoffline.core.utils.observer.SimpleProperty;

public class GameModelImpl implements GameModel {
    private FieldEntity field;
    private TankModel playerModel;
    private TankModel enemyModel;
    private Property<TankModel> winnerProperty;

    public GameModelImpl(FieldEntity field, int x1, int y1, int x2, int y2) {
        this.field = field;
        this.winnerProperty = new SimpleProperty<>();

        this.playerModel = new TankModelImpl(x1, y1, 100, 10, "/images/player.png");
        setStartDirection(playerModel);

        this.enemyModel = new TankModelImpl(x2, y2, 100, 20, "/images/enemy.png");
        setStartDirection(enemyModel);

        this.playerModel.getHealthProperty().addObserver((observable, oldValue, newValue) -> {
            if (newValue <= 0) {
                this.winnerProperty.set(enemyModel);
            }
        });

        this.enemyModel.getHealthProperty().addObserver((observable, oldValue, newValue) -> {
            if (newValue <= 0) {
                this.winnerProperty.set(playerModel);
            }
        });
    }

    private void setStartDirection(TankModel model) {
        for (Direction dir : Direction.values()) {
            if (!field.hasBorder(model.getXPosition(), model.getYPosition(), dir)) {
                model.setDirection(dir);
                return;
            }
        }
    }

    @Override
    public FieldEntity getField() {
        return field;
    }

    @Override
    public TankModel getPlayer() {
        return playerModel;
    }

    @Override
    public TankModel getEnemy() {
        return enemyModel;
    }

    @Override
    public Observable<TankModel> getWinnerProperty() {
        return winnerProperty;
    }

    @Override
    public TankModel getWinner() {
        return winnerProperty.get();
    }

    @Override
    public boolean couldMove(int x, int y, Direction direction) {
        return !field.hasBorder(x, y, direction);
    }

    @Override
    public TankModel getTank(int x, int y) {
        if (x == playerModel.getXPosition() && y == playerModel.getYPosition()) {
            return playerModel;
        } else if (x == enemyModel.getXPosition() && y == enemyModel.getYPosition()) {
            return enemyModel;
        } else {
            return null;
        }
    }
}
