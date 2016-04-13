package com.tanksoffline.application.views.sprites;

public abstract class AbstractSprite implements Sprite {
    protected double moveSpeed;
    protected double rotatingSpeed;
    protected String imageUrl;
    protected double x, y;
    protected double width, height;
    protected double cellSize;

    public AbstractSprite(String imageUrl, double moveSpeed, double rotatingSpeed, double cellSize,
                          double x, double y, double w, double h) {
        this.imageUrl = imageUrl;
        this.moveSpeed = moveSpeed;
        this.rotatingSpeed = rotatingSpeed;
        this.cellSize = cellSize;
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    @Override
    public double getMoveSpeed() {
        return moveSpeed;
    }

    @Override
    public double getRotatingSpeed() {
        return rotatingSpeed;
    }
}
