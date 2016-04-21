package com.tanksoffline.application.controllers;

import com.tanksoffline.application.models.game.TankModel;
import com.tanksoffline.application.utils.Direction;
import com.tanksoffline.application.views.sprites.Sprite;
import com.tanksoffline.core.utils.observer.Property;
import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class TankController {
    protected GameController gameController;
    protected TankModel tankModel;
    protected Sprite sprite;

    public TankController(GameController gameController, TankModel tankModel, Sprite sprite) {
        this.gameController = gameController;
        this.tankModel = tankModel;
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void move(Direction direction) {
        if (gameController.couldMove(tankModel, direction) && sprite.isReady()) {
            if (direction == tankModel.getDirection()) {
                tankModel.move();
            } else {
                tankModel.setDirection(direction);
            }
        }
    }

    public Sprite shoot() {
        return new Sprite() {
            private Rectangle shell;
            private Direction direction;
            private int x;
            private int y;
            private double width;

            {
                direction = tankModel.getDirection();
                x = tankModel.getXPosition();
                y = tankModel.getYPosition();

                width = sprite.getCellSize() / 5.0;

                double cs = sprite.getCellSize();
                switch (direction) {
                    case TOP:
                        shell = new Rectangle(x * cs + cs / 2.0 - width / 2.0, y * cs, width, width);
                        y -= 1;
                        break;
                    case LEFT:
                        shell = new Rectangle(x * cs, y * cs + cs / 2.0 - width / 2.0, width, width);
                        x -= 1;
                        break;
                    case RIGHT:
                        shell = new Rectangle((x + 1) * cs, y * cs + cs / 2.0 - width / 2.0, width, width);
                        x += 1;
                        break;
                    case BOTTOM:
                        shell = new Rectangle(x * cs + cs / 2.0 - width / 2.0, (y + 1) * cs, width, width);
                        y += 1;
                        break;
                }
                shell.setFill(Color.RED);

                RotateTransition rotateTransition = new RotateTransition(Duration.millis(200));
                rotateTransition.setByAngle(30.0);

                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200));
                switch (direction) {
                    case TOP:
                        translateTransition.setByY(-cs);
                        break;
                    case LEFT:
                        translateTransition.setByX(-cs);
                        break;
                    case RIGHT:
                        translateTransition.setByX(cs);
                        break;
                    case BOTTOM:
                        translateTransition.setByY(cs);
                }

                Transition transition = new ParallelTransition(shell, rotateTransition, translateTransition);
                transition.setOnFinished(event -> {
                    if (gameController.couldMove(x, y, direction)) {
                        switch (direction) {
                            case TOP:
                                y -= 1;
                                break;
                            case LEFT:
                                x -= 1;
                                break;
                            case RIGHT:
                                x += 1;
                                break;
                            case BOTTOM:
                                y += 1;
                                break;
                        }
                        transition.play();
                    } else {
                        switch (direction) {
                            case TOP:
                                y -= 1;
                                break;
                            case LEFT:
                                x -= 1;
                                break;
                            case RIGHT:
                                x += 1;
                                break;
                            case BOTTOM:
                                y += 1;
                                break;
                        }
                        TankModel tm = gameController.getGameModel().getTank(x, y);
                        if (tm != null) {
                            tm.dealDamage(tankModel.getDamage());
                        }
                        shell.setWidth(0);
                        shell.setHeight(0);
                    }
                });

                shell.parentProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        transition.play();
                    }
                });

            }

            @Override
            public Node getNode() {
                return shell;
            }

            @Override
            public Property<Boolean> readyProperty() {
                return null;
            }

            @Override
            public double getCellSize() {
                return width;
            }
        };
    }
}
