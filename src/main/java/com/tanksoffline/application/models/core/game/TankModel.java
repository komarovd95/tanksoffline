package com.tanksoffline.application.models.core.game;

import com.tanksoffline.application.data.fields.Direction;
import com.tanksoffline.core.utils.obs.Observable;

public interface TankModel extends Movable, Rotatable {
    Observable<Direction> getDirectionProperty();
    Direction getDirection();
    void setDirection(Direction direction);

    Observable<Integer> getXPositionProperty();
    int getXPosition();
    void setXPosition(int x);

    Observable<Integer> getYPositionProperty();
    int getYPosition();
    void setYPosition(int y);

    Observable<Integer> getHealthProperty();
    int getHealth();

    int getDamage();

    String getIcon();

    Bomb shoot();
    void dealDamage(int dealtDamage);

    class Bomb implements Movable {
        private int damage;

        public Bomb(int damage) {
            this.damage = damage;
        }


        @Override
        public Observable<Boolean> getIsMovingProperty() {
            return null;
        }

        @Override
        public boolean isMoving() {
            return false;
        }

        @Override
        public void move() {

        }

        @Override
        public void stopMoving() {

        }

        @Override
        public double getMovingSpeed() {
            return 0;
        }

        public int getDamage() {
            return damage;
        }
    }
}
