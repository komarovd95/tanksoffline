package com.tanksoffline.application.controllers;

import com.tanksoffline.application.data.Field;
import com.tanksoffline.application.models.game.TankModel;
import com.tanksoffline.application.utils.Direction;
import javafx.scene.Group;

import java.util.*;

public class EnemyTankController extends TankController {
    private Group group;

    public EnemyTankController(TankController tankController, Group group) {
        super(tankController.gameController, tankController.tankModel, tankController.sprite);
        this.group = group;
        initialize();
    }

    private void initialize() {
//        gameController.getGameModel().getPlayer().getXPositionProperty().addObserver(
//                (observable, oldValue, newValue) -> attack());
//        gameController.getGameModel().getPlayer().getYPositionProperty().addObserver(
//                (observable, oldValue, newValue) -> attack());
//        sprite.readyProperty().addObserver((observable1, oldValue1, newValue1) -> {
//            if (newValue1) attack();
//        });

//        tankModel.getXPositionProperty().addObserver((observable, oldValue, newValue) -> attack());
//        tankModel.getYPositionProperty().addObserver((observable, oldValue, newValue) -> attack());
//        tankModel.getDirectionProperty().addObserver((observable, oldValue, newValue) -> attack());
    }

    private Set<Vertex> getTargetCells(TankModel tankModel) {
        Field field = gameController.getGameModel().getField();
        int xPos = tankModel.getXPosition();
        int yPos = tankModel.getYPosition();
        Set<Vertex> targetCells = new HashSet<>();

        for (int x = xPos - 1; x >= 0; x--) {
            if (gameController.couldMove(x, yPos, Direction.RIGHT)
                    || (gameController.getGameModel().getTank(x + 1, yPos) != null
                    && !gameController.getGameModel().getField().hasBorder(x, yPos, Direction.RIGHT))) {
                targetCells.add(new Vertex(x, yPos, Direction.RIGHT));
            } else {
                break;
            }
        }

        for (int x = xPos + 1; x < field.getWidth(); x++) {
            if (gameController.couldMove(x, yPos, Direction.LEFT)
                    || (gameController.getGameModel().getTank(x - 1, yPos) != null
                    && !gameController.getGameModel().getField().hasBorder(x, yPos, Direction.LEFT))) {
                targetCells.add(new Vertex(x, yPos, Direction.LEFT));
            } else {
                break;
            }
        }

        for (int y = yPos - 1; y >= 0; y--) {
            if (gameController.couldMove(xPos, y, Direction.BOTTOM)
                    || (gameController.getGameModel().getTank(xPos, y + 1) != null
                    && !gameController.getGameModel().getField().hasBorder(xPos, y, Direction.BOTTOM))) {
                targetCells.add(new Vertex(xPos, y, Direction.BOTTOM));
            } else {
                break;
            }
        }

        for (int y = yPos + 1; y < field.getHeight(); y++) {
            if (gameController.couldMove(xPos, y, Direction.TOP)
                    || (gameController.getGameModel().getTank(xPos, y + 1) != null
                    && !gameController.getGameModel().getField().hasBorder(xPos, y, Direction.TOP))) {
                targetCells.add(new Vertex(xPos, y, Direction.TOP));
            } else {
                break;
            }
        }

        return targetCells;
    }

    private static class Vertex implements Comparable<Vertex> {
        int dist = Integer.MAX_VALUE;
        Direction direction;
        Vertex prev;
        int x;
        int y;

        public Vertex(int x, int y, Direction direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        @Override
        public int compareTo(Vertex o) {
            return Integer.compare(dist, o.dist);
        }

        @Override
        public int hashCode() {
            return x * 31 + y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj != null && obj instanceof Vertex) {
                Vertex v = (Vertex) obj;
                return this.x == v.x && this.y == v.y && this.direction == v.direction;
            } else {
                return false;
            }
        }
    }

    public void attack() {
        Set<Vertex> targetCells = getTargetCells(gameController.getGameModel().getPlayer());
        Vertex source = new Vertex(tankModel.getXPosition(), tankModel.getYPosition(), tankModel.getDirection());
        if (targetCells.contains(source)) {
            group.getChildren().add(shoot().getNode());
            return;
//            Timer timer = new Timer();
//            timer.
//            return;
        }

        source.dist = 0;

        Queue<Vertex> queue = new LinkedList<>();
        Set<Vertex> visited = new HashSet<>();

        queue.add(source);

        while (!queue.isEmpty()) {
            Vertex v = queue.poll();

            if (visited.contains(v)) {
                continue;
            } else {
                visited.add(v);
            }

            if (gameController.couldMove(v.x, v.y, Direction.TOP)) {
                if (v.direction == Direction.TOP) {
                    if (v.y > 1) {
                        Vertex u = new Vertex(v.x, v.y - 1, Direction.TOP);
                        u.dist = v.dist + 1;
                        u.prev = v;
                        queue.add(u);
                    }
                } else {
                    Vertex u = new Vertex(v.x, v.y, Direction.TOP);
                    u.dist = v.dist + ((v.direction == Direction.BOTTOM) ? 2 : 1);
                    u.prev = v;
                    queue.add(u);
                }
            }

            if (gameController.couldMove(v.x, v.y, Direction.LEFT)) {
                if (v.direction == Direction.LEFT) {
                    if (v.x > 1) {
                        Vertex u = new Vertex(v.x - 1, v.y, Direction.LEFT);
                        u.dist = v.dist + 1;
                        u.prev = v;
                        queue.add(u);
                    }
                } else {
                    Vertex u = new Vertex(v.x, v.y, Direction.LEFT);
                    u.dist = v.dist + ((v.direction == Direction.RIGHT) ? 2 : 1);
                    u.prev = v;
                    queue.add(u);
                }
            }

            if (gameController.couldMove(v.x, v.y, Direction.RIGHT)) {
                if (v.direction == Direction.RIGHT) {
                    if (v.x < gameController.getGameModel().getField().getWidth() - 1) {
                        Vertex u = new Vertex(v.x + 1, v.y, Direction.RIGHT);
                        u.dist = v.dist + 1;
                        u.prev = v;
                        queue.add(u);
                    }
                } else {
                    Vertex u = new Vertex(v.x, v.y, Direction.RIGHT);
                    u.dist = v.dist + ((v.direction == Direction.LEFT) ? 2 : 1);
                    u.prev = v;
                    queue.add(u);
                }
            }

            if (gameController.couldMove(v.x, v.y, Direction.BOTTOM)) {
                if (v.direction == Direction.BOTTOM) {
                    if (v.y < gameController.getGameModel().getField().getHeight() - 1) {
                        Vertex u = new Vertex(v.x, v.y + 1, Direction.BOTTOM);
                        u.dist = v.dist + 1;
                        u.prev = v;
                        queue.add(u);
                    }
                } else {
                    Vertex u = new Vertex(v.x, v.y, Direction.BOTTOM);
                    u.dist = v.dist + ((v.direction == Direction.TOP) ? 2 : 1);
                    u.prev = v;
                    queue.add(u);
                }
            }
        }

        Optional<Vertex> target = visited.stream().filter(targetCells::contains).min(Vertex::compareTo);

        if (target.isPresent()) {
            Vertex t = target.get();
            while (t.prev != source) {
                t = t.prev;
            }
            move(t.direction);
        }
    }

    private boolean contains(Set<Vertex> vertices, Vertex vertex) {
        for (Vertex v : vertices) {
            if (v.x == vertex.x && v.y == vertex.y) {
                return true;
            }
        }
        return false;
    }

    private int getDist(Vertex v, Direction direction) {
        if (!gameController.couldMove(v.x, v.y, direction)) {
            return Integer.MAX_VALUE;
        }

        switch (v.direction) {
            case TOP:
                if (direction == Direction.TOP) return 1;
                else if (direction == Direction.LEFT || direction == Direction.RIGHT) return 2;
                else if (direction == Direction.BOTTOM) return 3;
                break;
            case LEFT:
                if (direction == Direction.LEFT) return 1;
                else if (direction == Direction.BOTTOM || direction == Direction.TOP) return 2;
                else if (direction == Direction.RIGHT) return 3;
                break;
            case RIGHT:
                if (direction == Direction.RIGHT) return 1;
                else if (direction == Direction.TOP || direction == Direction.BOTTOM) return 2;
                else if (direction == Direction.LEFT) return 3;
                break;
            case BOTTOM:
                if (direction == Direction.BOTTOM) return 1;
                else if (direction == Direction.LEFT || direction == Direction.RIGHT) return 2;
                else if (direction == Direction.TOP) return 3;
                break;
        }
        return Integer.MAX_VALUE;
    }

//    private void createGraph(Vertex source) {
//        if (source.x > 1) {
//            source.neighbours.add(new Vertex(source.x - 1, source.y, Direction.LEFT));
//        }
//        if (source.x < gameController.getGameModel().getField().getWidth() - 1) {
//            source.neighbours.add(new Vertex(source.))
//        }
//    }
}
