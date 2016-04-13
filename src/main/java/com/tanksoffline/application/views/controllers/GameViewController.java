package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.App;
import com.tanksoffline.application.controllers.GameController;
import com.tanksoffline.application.data.fields.Direction;
import com.tanksoffline.application.data.fields.Field;
import com.tanksoffline.application.data.fields.FieldCell;
import com.tanksoffline.application.models.core.game.GameModel;
import com.tanksoffline.application.models.core.game.TankModel;
import com.tanksoffline.application.utils.BoundRenderer;
import com.tanksoffline.application.utils.Renderer;
import com.tanksoffline.application.views.drawing.DefaultCellRenderer;
import com.tanksoffline.application.views.drawing.DefaultFieldRenderer;
import com.tanksoffline.application.views.sprites.Sprite;
import com.tanksoffline.application.views.sprites.TankSprite;
import com.tanksoffline.core.utils.SingletonFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GameViewController implements Initializable, PartialView {
    private BoundRenderer<TankModel> tankModelBoundRenderer;
    private BoundRenderer<Sprite> spriteRenderer;
    private GameController gameController;
    private Renderer<Field> fieldRenderer;
    private SingletonFactory<Image> playerImageFactory;
    private SingletonFactory<Image> enemyImageFactory;
    private Sprite playerSprite;

    @FXML
    private Canvas canvas;

    @FXML
    private Button backBtn;

    @FXML
    private Label playerHpLabel;

    @FXML
    private Label enemyHpLabel;

    private Scene scene;

    public GameViewController() {
        this.gameController = new GameController();
        this.playerImageFactory = new SingletonFactory<>(
                () -> new Image(App.getInstance().getGameModel().getPlayer().getIcon()));
        this.enemyImageFactory = new SingletonFactory<>(
                () -> new Image(App.getInstance().getGameModel().getEnemy().getIcon()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.getInstance().getPrimaryStage().getScene().setOnKeyPressed(this::onKeyTyped);
        GameModel gameModel = gameController.getGameModel();

        TankModel player = gameModel.getPlayer();

        double cellSize = canvas.getWidth() / gameModel.getField().getWidth();
        playerSprite = new TankSprite(player, cellSize, player.getXPosition() * cellSize,
                player.getYPosition() * cellSize, cellSize, cellSize);

        tankModelBoundRenderer = (tankModel, gc, bounds) -> {
            Paint tmp = gc.getFill();
            gc.setFill(Color.AQUAMARINE);
            double angle = 0.0;
            switch (tankModel.getDirection()) {
                case BOTTOM:
                    angle = 180.0;
                    break;
                case LEFT:
                    angle = 270.0;
                    break;
                case RIGHT:
                    angle = 90.0;
            }

            ImageView iv = new ImageView((tankModel == App.getInstance().getGameModel().getPlayer())
                    ? playerImageFactory.create()
                    : enemyImageFactory.create());
            iv.setRotate(angle);
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);

            gc.drawImage(iv.snapshot(params, null), bounds.getMinX() + 0.1 * bounds.getWidth(),
                    bounds.getMinY() + 0.1 * bounds.getHeight(),
                    0.8 * bounds.getWidth(), 0.8 * bounds.getHeight());
            gc.setFill(tmp);
        };

        spriteRenderer = (sprite, gc, bounds) -> {
            Paint tmp = gc.getFill();
            gc.setFill(Color.AQUAMARINE);
            gc.drawImage(sprite.getImage(), bounds.getMinX() + 0.1 * bounds.getWidth(),
                    bounds.getMinY() + 0.1 * bounds.getHeight(), 0.8 * bounds.getWidth(), 0.8 * bounds.getHeight());
            gc.setFill(tmp);
        };

        BoundRenderer<FieldCell> fieldCellBoundRenderer = new DefaultCellRenderer() {
            @Override
            public void render(FieldCell fieldCell, GraphicsContext gc, Bounds bounds) {
                super.render(fieldCell, gc, bounds);

                int x = (int) (bounds.getMinX() / bounds.getWidth());
                int y = (int) (bounds.getMinY() / bounds.getHeight());

                GameModel gameModel = App.getInstance().getGameModel();
                if (gameModel.getPlayer().getXPosition() == x && gameModel.getPlayer().getYPosition() == y) {
                    //spriteRenderer.render(playerSprite, gc, playerSprite.update());
                    tankModelBoundRenderer.render(gameModel.getPlayer(), gc, bounds);
                } else if (gameModel.getEnemy().getXPosition() == x && gameModel.getEnemy().getYPosition() == y) {
                    tankModelBoundRenderer.render(gameModel.getEnemy(), gc, bounds);
                }

            }
        };

        fieldRenderer = new DefaultFieldRenderer(new DefaultCellRenderer());

        App.getInstance().getGameModel().getPlayer().getHealthProperty()
                .addObserver((observable, oldValue, newValue) -> playerHpLabel.setText("Вы " + newValue));

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(200), event -> {
            System.out.println("FRAME");
            fieldRenderer.render(gameController.getGameModel().getField(), canvas.getGraphicsContext2D());
            spriteRenderer.render(playerSprite, canvas.getGraphicsContext2D(), playerSprite.update(0.2));
        });

        backBtn.setOnAction(event -> {
            timeline.stop();
            App.getInstance().getPrimaryStage().getScene().setOnKeyPressed(null);
            GameViewController.this.onBackClick();
            GameViewController.this.onBackClick();
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public void onKeyTyped(KeyEvent event) {
        Direction direction = null;
        switch (event.getCode()) {
            case UP:
                direction = Direction.TOP;
                break;
            case LEFT:
                direction = Direction.LEFT;
                break;
            case RIGHT:
                direction = Direction.RIGHT;
                break;
            case DOWN:
                direction = Direction.BOTTOM;
                break;
        }

        System.out.println(event.getCode());

        if (direction != null) {
            gameController.onMove(direction);
        } else if (event.getCode() == KeyCode.ENTER) {
            gameController.onShoot();
        }
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
