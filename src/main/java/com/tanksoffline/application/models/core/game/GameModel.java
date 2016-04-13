package com.tanksoffline.application.models.core.game;

import com.tanksoffline.application.data.fields.Field;
import com.tanksoffline.core.utils.obs.Observable;

public interface GameModel {
    Field getField();
    TankModel getPlayer();
    TankModel getEnemy();

    Observable<TankModel> getWinnerProperty();
    TankModel getWinner();
}
