package com.tanksoffline.application.views.sprites;

import com.tanksoffline.application.models.game.TankModel;
import com.tanksoffline.application.utils.Direction;
import com.tanksoffline.core.utils.observer.Property;
import com.tanksoffline.core.utils.observer.SimpleProperty;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class TransiteTankSprite implements Sprite {
    private RotateTransition rotateTransition;
    private TranslateTransition translateTransition;
    private ImageView imageView;
    private TankModel tankModel;
    private Property<Boolean> readyProperty;
    private double cellSize;

    public TransiteTankSprite(TankModel tankModel, String imageUrl, double cellSize) {
        this.tankModel = tankModel;
        this.cellSize = cellSize;
        this.imageView = new ImageView(imageUrl);

        this.rotateTransition = new RotateTransition();
        this.translateTransition = new TranslateTransition(Duration.seconds(1), imageView);

        this.readyProperty = new SimpleProperty<>(true);

        initialize();
    }

    private void initialize() {
        double w = cellSize / 2.0 - 2.5;
        w = Math.sqrt(w * w * 2);
        imageView.setFitWidth(w);
        imageView.setFitHeight(w);
        w = (cellSize - w) / 2.0;
        imageView.setX(tankModel.getXPosition() * cellSize + w);
        imageView.setY(tankModel.getYPosition() * cellSize + w);
        switch (tankModel.getDirection()) {
            case TOP:
                imageView.setRotate(0.0);
                break;
            case LEFT:
                imageView.setRotate(270.0);
                break;
            case RIGHT:
                imageView.setRotate(90.0);
                break;
            case BOTTOM:
                imageView.setRotate(180.0);
                break;
        }

        rotateTransition.statusProperty().addListener((observable1, oldValue1, newValue1) -> {
            readyProperty.set(newValue1 == Animation.Status.STOPPED
                    && translateTransition.getStatus() == Animation.Status.STOPPED);
        });
        translateTransition.statusProperty().addListener((observable1, oldValue1, newValue1) -> {
            readyProperty.set(newValue1 == Animation.Status.STOPPED
                    && rotateTransition.getStatus() == Animation.Status.STOPPED);
        });


        rotateTransition.setNode(imageView);

        tankModel.getDirectionProperty().addObserver((observable, oldValue, newValue) -> {
            if (oldValue == newValue) return;

            double angle = getAngleDiff(oldValue, newValue);

            scheduleAnimation(() -> {
                rotateTransition.setDuration(Duration.seconds(Math.abs(angle) / 90.0));
                rotateTransition.setByAngle(angle);
                rotateTransition.play();
            });
        });

        tankModel.getXPositionProperty().addObserver((observable, oldValue, newValue) -> {
            if (oldValue == null || newValue == null) return;

            scheduleAnimation(() -> {
                translateTransition.setByX((newValue - oldValue) * cellSize);
                translateTransition.setByY(0.0);
                translateTransition.play();
            });
        });

        tankModel.getYPositionProperty().addObserver((observable, oldValue, newValue) -> {
            if (oldValue == null || newValue == null) return;

            scheduleAnimation(() -> {
                translateTransition.setByX(0.0);
                translateTransition.setByY((newValue - oldValue) * cellSize);
                translateTransition.play();
            });
        });
    }

    private void scheduleAnimation(Runnable play) {
        if (rotateTransition.getStatus() == Animation.Status.RUNNING) {
            rotateTransition.setOnFinished(event -> {
                play.run();
                rotateTransition.setOnFinished(null);
            });
        } else if (translateTransition.getStatus() == Animation.Status.RUNNING) {
            translateTransition.setOnFinished(event -> {
                play.run();
                translateTransition.setOnFinished(null);
            });
        } else {
            play.run();
        }
    }

    private double getAngleDiff(Direction from, Direction to) {
        switch (from) {
            case TOP:
                if (to == Direction.TOP) {
                    return 0.0;
                } else if (to == Direction.LEFT) {
                    return -90.0;
                } else if (to == Direction.RIGHT) {
                    return 90.0;
                } else if (to == Direction.BOTTOM) {
                    return 180.0;
                }
                break;
            case LEFT:
                if (to == Direction.LEFT) {
                    return 0.0;
                } else if (to == Direction.BOTTOM) {
                    return -90.0;
                } else if (to == Direction.TOP) {
                    return 90.0;
                } else if (to == Direction.RIGHT) {
                    return 180.0;
                }
                break;
            case RIGHT:
                if (to == Direction.RIGHT) {
                    return 0.0;
                } else if (to == Direction.TOP) {
                    return -90.0;
                } else if (to == Direction.BOTTOM) {
                    return 90.0;
                } else if (to == Direction.LEFT) {
                    return 180.0;
                }
                break;
            case BOTTOM:
                if (to == Direction.BOTTOM) {
                    return 0.0;
                } else if (to == Direction.RIGHT) {
                    return -90.0;
                } else if (to == Direction.LEFT) {
                    return 90.0;
                } else if (to == Direction.TOP) {
                    return 180.0;
                }
                break;
        }
        return 0.0;
    }

    @Override
    public Node getNode() {
        return imageView;
    }

    @Override
    public Property<Boolean> readyProperty() {
        return readyProperty;
    }

    @Override
    public double getCellSize() {
        return cellSize;
    }
}
