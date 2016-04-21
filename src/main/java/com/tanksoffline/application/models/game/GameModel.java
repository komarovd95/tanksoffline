package com.tanksoffline.application.models.game;

import com.tanksoffline.application.entities.FieldEntity;
import com.tanksoffline.application.utils.Direction;
import com.tanksoffline.core.utils.observer.Observable;

public interface GameModel {
    FieldEntity getField();
    TankModel getPlayer();
    TankModel getEnemy();
    TankModel getTank(int x, int y);

    Observable<TankModel> getWinnerProperty();
    TankModel getWinner();

    boolean couldMove(int x, int y, Direction direction);
}
