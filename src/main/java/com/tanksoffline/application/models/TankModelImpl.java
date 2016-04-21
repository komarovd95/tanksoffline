package com.tanksoffline.application.models;

import com.tanksoffline.application.utils.Direction;
import com.tanksoffline.application.models.game.TankModel;
import com.tanksoffline.core.utils.observer.Observable;
import com.tanksoffline.core.utils.observer.Property;
import com.tanksoffline.core.utils.observer.SimpleProperty;

public class TankModelImpl implements TankModel {
    private Property<Direction> directionProperty;
    private Property<Integer> xPosition;
    private Property<Integer> yPosition;
    private Property<Integer> health;
    private int damage;
    private String iconUrl;

    private Property<Boolean> movingProperty;
    private Property<Boolean> rotatingProperty;

    public TankModelImpl(int x, int y, int initHealth, int damage, String iconUrl) {
        this.xPosition = new SimpleProperty<>(x);
        this.yPosition = new SimpleProperty<>(y);
        this.directionProperty = new SimpleProperty<>();
        this.health = new SimpleProperty<>(initHealth);
        this.damage = damage;
        this.iconUrl = iconUrl;
        this.movingProperty = new SimpleProperty<>(false);

        this.rotatingProperty = new SimpleProperty<>(false);

    }

    @Override
    public Observable<Direction> getDirectionProperty() {
        return directionProperty;
    }

    @Override
    public Direction getDirection() {
        return directionProperty.get();
    }

    @Override
    public void setDirection(Direction direction) {
        this.directionProperty.set(direction);
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
