package com.tanksoffline.application.models.core.game;

import com.tanksoffline.application.entities.FieldEntity;
import com.tanksoffline.core.utils.observer.Observable;

public interface GameModel {
    FieldEntity getField();
    TankModel getPlayer();
    TankModel getEnemy();

    Observable<TankModel> getWinnerProperty();
    TankModel getWinner();
}
