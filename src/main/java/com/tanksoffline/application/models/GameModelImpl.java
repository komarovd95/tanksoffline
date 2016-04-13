package com.tanksoffline.application.models;

import com.tanksoffline.application.data.fields.Direction;
import com.tanksoffline.application.data.fields.Field;
import com.tanksoffline.application.models.core.game.GameModel;
import com.tanksoffline.application.models.core.game.TankModel;
import com.tanksoffline.core.utils.obs.Observable;
import com.tanksoffline.core.utils.obs.SimpleObservable;

public class GameModelImpl implements GameModel {
    private Field field;
    private TankModel playerModel;
    private TankModel enemyModel;
    private Observable<TankModel> winnerProperty;

    public GameModelImpl(Field field, int x1, int y1, int x2, int y2) {
        this.field = field;
        this.winnerProperty = new SimpleObservable<>();

        this.playerModel = new TankModelImpl(x1, y1, 100, 10, "/images/player.png");
        setStartDirection(playerModel);

        this.enemyModel = new TankModelImpl(x2, y2, 100, 10, "/images/enemy.png");
        setStartDirection(enemyModel);
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
    public Field getField() {
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
}
