package com.tanksoffline.application.models;

import com.tanksoffline.application.data.fields.Direction;
import com.tanksoffline.application.models.core.game.TankModel;
import com.tanksoffline.core.utils.obs.Observable;
import com.tanksoffline.core.utils.obs.SimpleObservable;

import java.util.concurrent.atomic.AtomicInteger;

public class TankModelImpl implements TankModel {
    private Observable<Direction> currentDirection;
    private Observable<Integer> xPosition;
    private Observable<Integer> yPosition;
    private Observable<Integer> health;
    private AtomicInteger moveCounter;
    private int damage;
    private String iconUrl;

    private Observable<Boolean> movingProperty;
    private Observable<Boolean> rotatingProperty;

    public TankModelImpl(int x, int y, int initHealth, int damage, String iconUrl) {
        this.xPosition = new SimpleObservable<>(x);
        this.yPosition = new SimpleObservable<>(y);
        this.currentDirection = new SimpleObservable<>();
        this.health = new SimpleObservable<>(initHealth);
        this.damage = damage;
        this.iconUrl = iconUrl;
        this.movingProperty = new SimpleObservable<>(false);
        this.moveCounter = new AtomicInteger();

        this.rotatingProperty = new SimpleObservable<>(false);

    }

    @Override
    public Observable<Direction> getDirectionProperty() {
        return currentDirection;
    }

    @Override
    public Direction getDirection() {
        return currentDirection.get();
    }

    @Override
    public void setDirection(Direction direction) {
        this.currentDirection.set(direction);
        this.rotatingProperty.set(true);
    }

    @Override
    public Observable<Integer> getXPositionProperty() {
        return xPosition;
    }

    @Override
    public int getXPosition() {
        return xPosition.get();
    }

    @Override
    public void setXPosition(int x) {
        this.xPosition.set(x);
    }

    @Override
    public Observable<Integer> getYPositionProperty() {
        return yPosition;
    }

    @Override
    public int getYPosition() {
        return yPosition.get();
    }

    @Override
    public void setYPosition(int y) {
        this.yPosition.set(y);
    }

    @Override
    public Observable<Integer> getHealthProperty() {
        return health;
    }

    @Override
    public int getHealth() {
        return health.get();
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public String getIcon() {
        return iconUrl;
    }

    @Override
    public Bomb shoot() {
        return new Bomb(damage);
    }

    @Override
    public void dealDamage(int dealtDamage) {
        health.set(health.get() - dealtDamage);
    }

    @Override
    public Observable<Boolean> getIsMovingProperty() {
        return movingProperty;
    }

    @Override
    public boolean isMoving() {
        return movingProperty.get();
    }

    @Override
    public void move() {
        switch (getDirection()) {
            case TOP:
                yPosition.set(yPosition.get() - 1);
                break;
            case LEFT:
                xPosition.set(xPosition.get() - 1);
                break;
            case RIGHT:
                xPosition.set(xPosition.get() + 1);
                break;
            case BOTTOM:
                yPosition.set(yPosition.get() + 1);
                break;
        }
        movingProperty.set(true);
    }

    @Override
    public void stopMoving() {
        movingProperty.set(false);
    }

    @Override
    public double getMovingSpeed() {
        return 1.0;
    }

    @Override
    public Observable<Boolean> getIsRotatingProperty() {
        return rotatingProperty;
    }

    @Override
    public boolean isRotating() {
        return rotatingProperty.get();
    }

    @Override
    public void rotate(double degrees) {

    }

    @Override
    public void stopRotating() {
        this.rotatingProperty.set(false);
    }

    @Override
    public double getRotatingSpeed() {
        return 90.0;
    }
}
