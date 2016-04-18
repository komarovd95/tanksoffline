package com.tanksoffline.application.views.sprites;

import com.tanksoffline.application.utils.Direction;
import com.tanksoffline.application.models.core.game.TankModel;
import com.tanksoffline.core.utils.Factory;
import com.tanksoffline.core.utils.SingletonFactory;
import com.tanksoffline.core.utils.observer.Observer;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.concurrent.atomic.AtomicInteger;

public class TankSprite extends AbstractSprite {
    private TankModel tankModel;
    private Factory<Image> imageFactory;
    private AtomicInteger frameCounter;
    private double angle;

    public TankSprite(TankModel tankModel, double moveSpeed, double rotatingSpeed, double cellSize,
                      double x, double y, double w, double h) {
        super(tankModel.getIcon(), moveSpeed, rotatingSpeed, cellSize, x, y, w, h);
        this.tankModel = tankModel;
        this.imageFactory = new SingletonFactory<>(() -> new Image(imageUrl));
        this.frameCounter = new AtomicInteger(0);
        initialize();
    }

    public TankSprite(TankModel tankModel, double cellSize, double x, double y, double w, double h) {
        this(tankModel, cellSize, 90.0, cellSize, x, y, w, h);
    }

    private void initialize() {
        tankModel.getDirectionProperty().addObserver((observable, oldValue, newValue) -> {
            if (oldValue == null || newValue == null) return;
            double a = 0.0;
            switch (oldValue) {
                case TOP:
                    a = getAngleDiff(newValue, Direction.BOTTOM, Direction.LEFT, Direction.RIGHT);
                    break;
                case LEFT:
                    a = getAngleDiff(newValue, Direction.RIGHT, Direction.BOTTOM, Direction.TOP);
                    break;
                case RIGHT:
                    a = getAngleDiff(newValue, Direction.LEFT, Direction.TOP, Direction.BOTTOM);
                    break;
                case BOTTOM:
                    a = getAngleDiff(newValue, Direction.TOP, Direction.RIGHT, Direction.LEFT);
                    break;
            }
            frameCounter.addAndGet((int)(a / 90.0) * 5);
            //frameCounter.addAndGet((int) (a / rotatingSpeed));
        });

        Observer<Integer> coordinateObserver = (observable, oldValue, newValue) -> {
            if (oldValue == null || newValue == null) return;
            //frameCounter.addAndGet((int) (Math.abs(newValue - oldValue) * cellSize / moveSpeed));
            frameCounter.addAndGet(5);
        };

        tankModel.getXPositionProperty().addObserver(coordinateObserver);
        tankModel.getYPositionProperty().addObserver(coordinateObserver);
    }

    private void negateRotatingSpeed(boolean willNegated) {
        if (willNegated) {
            if (rotatingSpeed > 0.0) rotatingSpeed *= -1.0;
        } else if (rotatingSpeed < 0.0) rotatingSpeed *= -1.0;
    }

    private double getAngleDiff(Direction current, Direction opposite, Direction leftNeighbour, Direction rightNeighbour) {
        if (current == opposite) {
            return 180.0;
        } else if (current == leftNeighbour) {
            negateRotatingSpeed(true);
            return 90.0;
        } else if (current == rightNeighbour) {
            negateRotatingSpeed(false);
            return 90.0;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Image getImage() {
        ImageView iv = new ImageView(imageFactory.create());
        iv.setRotate(angle);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        return iv.snapshot(params, null);
    }

    @Override
    public Bounds update(double timeElapsed) {
        if (frameCounter.get() > 0) {
            frameCounter.decrementAndGet();
            if (tankModel.isMoving()) {
                switch (tankModel.getDirection()) {
                    case TOP:
                        y -= moveSpeed * timeElapsed;
                        break;
                    case LEFT:
                        x -= moveSpeed * timeElapsed;
                        break;
                    case RIGHT:
                        x += moveSpeed * timeElapsed;
                        break;
                    case BOTTOM:
                        y += moveSpeed * timeElapsed;
                }
            } else if (tankModel.isRotating()) {
                angle += rotatingSpeed * timeElapsed;
                if (angle < 0.0) {
                    angle += 360.0;
                }
            }
        } else {
            if (tankModel.isMoving()) tankModel.stopMoving();
            else if (tankModel.isRotating()) tankModel.stopRotating();
        }
        return new BoundingBox(x, y, width, height);
    }
}
