package com.tanksoffline.application.views.controllers;

import com.tanksoffline.application.app.App;
import com.tanksoffline.application.controllers.EnemyTankController;
import com.tanksoffline.application.controllers.GameController;
import com.tanksoffline.application.controllers.MatchActionController;
import com.tanksoffline.application.controllers.TankController;
import com.tanksoffline.application.data.Field;
import com.tanksoffline.application.data.Match;
import com.tanksoffline.application.entities.search.FieldSearch;
import com.tanksoffline.application.entities.search.UserSearch;
import com.tanksoffline.application.services.LoginService;
import com.tanksoffline.application.utils.Direction;
import com.tanksoffline.application.entities.FieldEntity;
import com.tanksoffline.application.data.FieldCell;
import com.tanksoffline.application.models.game.GameModel;
import com.tanksoffline.application.models.game.TankModel;
import com.tanksoffline.application.utils.BoundRenderer;
import com.tanksoffline.application.utils.Renderer;
import com.tanksoffline.application.utils.TaskFactory;
import com.tanksoffline.application.views.sprites.Sprite;
import com.tanksoffline.application.views.sprites.TransiteTankSprite;
import com.tanksoffline.core.mvc.ActionController;
import com.tanksoffline.core.utils.SingletonFactory;
import com.tanksoffline.core.utils.observer.Observable;
import com.tanksoffline.core.utils.observer.Observer;
import javafx.animation.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GameViewController implements Initializable, PartialView {
    private BoundRenderer<TankModel> tankModelBoundRenderer;
    private BoundRenderer<Sprite> spriteRenderer;
    private GameController gameController;
    private Renderer<FieldEntity> fieldRenderer;
    private SingletonFactory<Image> playerImageFactory;
    private SingletonFactory<Image> enemyImageFactory;
    private Sprite playerSprite;
    private Sprite enemySprite;

    private RotateTransition rotateTransition;
    private TranslateTransition translateTransition;

    private TankController playerController;
    private TankController enemyController;

    private ActionController<Match> matchActionController;

//    @FXML
//    private Canvas canvas;

    @FXML
    private Button backBtn;

    @FXML
    private Label playerHpLabel;

    @FXML
    private Label enemyHpLabel;

    @FXML
    private Group group;

    private Scene scene;
    private boolean wasShot;

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
        TankModel enemy = gameModel.getEnemy();

//        double cellSize = canvas.getWidth() / gameModel.getField().getWidth();
//        playerSprite = new TankSprite(player, cellSize, player.getXPosition() * cellSize,
//                player.getYPosition() * cellSize, cellSize, cellSize);
//
//        enemySprite = new TankSprite(enemy, cellSize, enemy.getXPosition() * cellSize,
//                enemy.getYPosition() * cellSize, cellSize, cellSize);
//
//
//        tankModelBoundRenderer = (tankModel, gc, bounds) -> {
//            Paint tmp = gc.getFill();
//            gc.setFill(Color.AQUAMARINE);
//            double angle = 0.0;
//            switch (tankModel.getDirection()) {
//                case BOTTOM:
//                    angle = 180.0;
//                    break;
//                case LEFT:
//                    angle = 270.0;
//                    break;
//                case RIGHT:
//                    angle = 90.0;
//            }
//
//            ImageView iv = new ImageView((tankModel == App.getInstance().getGameModel().getPlayer())
//                    ? playerImageFactory.create()
//                    : enemyImageFactory.create());
//            iv.setRotate(angle);
//            SnapshotParameters params = new SnapshotParameters();
//            params.setFill(Color.TRANSPARENT);
//
//            gc.drawImage(iv.snapshot(params, null), bounds.getMinX() + 0.1 * bounds.getWidth(),
//                    bounds.getMinY() + 0.1 * bounds.getHeight(),
//                    0.8 * bounds.getWidth(), 0.8 * bounds.getHeight());
//            gc.setFill(tmp);
//        };
//
//        spriteRenderer = (sprite, gc, bounds) -> {
//            Paint tmp = gc.getFill();
//            gc.setFill(Color.AQUAMARINE);
//            gc.drawImage(sprite.getImage(), bounds.getMinX() + 0.1 * bounds.getWidth(),
//                    bounds.getMinY() + 0.1 * bounds.getHeight(), 0.8 * bounds.getWidth(), 0.8 * bounds.getHeight());
//            gc.setFill(tmp);
//        };
//
//        BoundRenderer<FieldCell> fieldCellBoundRenderer = new DefaultCellRenderer() {
//            @Override
//            public void render(FieldCell fieldCell, GraphicsContext gc, Bounds bounds) {
//                super.render(fieldCell, gc, bounds);
//
//                int x = (int) (bounds.getMinX() / bounds.getWidth());
//                int y = (int) (bounds.getMinY() / bounds.getHeight());
//
//                GameModel gameModel = App.getInstance().getGameModel();
//                if (gameModel.getPlayer().getXPosition() == x && gameModel.getPlayer().getYPosition() == y) {
//                    //spriteRenderer.render(playerSprite, gc, playerSprite.update());
//                    tankModelBoundRenderer.render(gameModel.getPlayer(), gc, bounds);
//                } else if (gameModel.getEnemy().getXPosition() == x && gameModel.getEnemy().getYPosition() == y) {
//                    tankModelBoundRenderer.render(gameModel.getEnemy(), gc, bounds);
//                }
//
//            }
//        };
//
//        fieldRenderer = new DefaultFieldRenderer(new DefaultCellRenderer());
//
//        App.getInstance().getGameModel().getPlayer().getHealthProperty()
//                .addObserver((observable, oldValue, newValue) -> playerHpLabel.setText("Вы " + newValue));
//
//        Timeline timeline = new Timeline();
//        timeline.setCycleCount(Timeline.INDEFINITE);
//
//        KeyFrame keyFrame = new KeyFrame(Duration.millis(200), event -> {
//            System.out.println("FRAME");
//            fieldRenderer.render(gameController.getGameModel().getField(), canvas.getGraphicsContext2D());
//            spriteRenderer.render(playerSprite, canvas.getGraphicsContext2D(), playerSprite.update(0.2));
//            spriteRenderer.render(enemySprite, canvas.getGraphicsContext2D(), enemySprite.update(0.2));
//        });
//

//
//        timeline.getKeyFrames().add(keyFrame);
//        timeline.play();


        Field field = gameModel.getField();
        double cellSize = 600.0 / field.getWidth();

        for (int i = 0; i < field.getWidth(); i++) {
            for (int j = 0; j < field.getHeight(); j++) {
                Rectangle rectangle = new Rectangle(i * cellSize, j * cellSize, cellSize, cellSize);
                rectangle.setFill(Color.BURLYWOOD);
                rectangle.setStroke(Color.BLACK);
                group.getChildren().add(rectangle);

                FieldCell fieldCell = field.getFieldCell(i, j);
                if (fieldCell.hasBorder(Direction.TOP)) {
                    Line line = new Line(i * cellSize, j * cellSize, (i + 1) * cellSize, j * cellSize);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(5.0);
                    group.getChildren().add(line);
//                    gc.strokeLine(x, y, x + w, y);
                }

                if (fieldCell.hasBorder(Direction.LEFT)) {
                    Line line = new Line(i * cellSize, j * cellSize, i * cellSize, (j + 1) * cellSize);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(5.0);
                    group.getChildren().add(line);
//                    gc.strokeLine(x, y, x, y + h);
                }

                if (fieldCell.hasBorder(Direction.BOTTOM)) {
                    Line line = new Line(i * cellSize, (j + 1) * cellSize, (i + 1) * cellSize, (j + 1) * cellSize);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(5.0);
                    group.getChildren().add(line);
//                    gc.strokeLine(x, y + h, x + w, y + h);
                }

                if (fieldCell.hasBorder(Direction.RIGHT)) {
                    Line line = new Line((i + 1) * cellSize, j * cellSize, (i + 1) * cellSize, (j + 1) * cellSize);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(5.0);
                    group.getChildren().add(line);
//                    gc.strokeLine(x + w, y, x + w, y + h);
                }
            }
        }

//        ImageView iv = new ImageView("/images/player.png");
//        iv.setFitWidth(Math.sqrt(27.5 * 27.5 * 2));
//        iv.setFitHeight(Math.sqrt(27.5 * 27.5 * 2));
//        iv.setX((60.0 - Math.sqrt(27.5 * 27.5 * 2)) / 2.0);
//        iv.setY((60.0 - Math.sqrt(27.5 * 27.5 * 2)) / 2.0);
//        iv.setPreserveRatio(false);
////        iv.setSmooth(true);
//        iv.setCache(true);
//
//        rotateTransition = new RotateTransition(Duration.seconds(1), iv);
//
//        translateTransition = new TranslateTransition(Duration.seconds(1), iv);
//        translateTransition.setByX(60.0);
//        translateTransition.statusProperty().addListener(new ChangeListener<Animation.Status>() {
//            @Override
//            public void changed(ObservableValue<? extends Animation.Status> observable, Animation.Status oldValue, Animation.Status newValue) {
//                System.out.println(newValue);
//            }
//        });

//        group.getChildren().add(iv);

        player.getHealthProperty().addObserver(new Observer<Integer>() {
            @Override
            public void observe(Observable<? extends Integer> observable, Integer oldValue, Integer newValue) {
                playerHpLabel.setText("Вы " + newValue);
                if (newValue <= 0) {
                    playerHpLabel.setText("Вы " + 0);
                    player.getHealthProperty().removeObserver(this);
                }
            }
        });

        enemy.getHealthProperty().addObserver(new Observer<Integer>() {
            @Override
            public void observe(Observable<? extends Integer> observable, Integer oldValue, Integer newValue) {
                enemyHpLabel.setText("Противник " + newValue);
                if (newValue <= 0) {
                    enemyHpLabel.setText("Противник " + 0);
                    enemy.getHealthProperty().removeObserver(this);
                }
            }
        });

        playerHpLabel.setText("Вы " + player.getHealth());
        enemyHpLabel.setText("Противник " + enemy.getHealth());

        playerController = new TankController(gameController, player,
                new TransiteTankSprite(player, "/images/player.png", cellSize));

        enemyController = new EnemyTankController(new TankController(gameController, enemy,
                new TransiteTankSprite(enemy, "/images/enemy.png", cellSize)), group);

        group.getChildren().addAll(playerController.getSprite().getNode(), enemyController.getSprite().getNode());


        Timeline aiLoop = new Timeline(new KeyFrame(Duration.millis(200),
                ae -> {
                    ((EnemyTankController) enemyController).attack();
//                    group.getChildren().add(enemyController.shoot().getNode());
                    if (wasShot) {
                        wasShot = false;
                        group.getChildren().add(playerController.shoot().getNode());
                    }
                }));
        aiLoop.setCycleCount(Animation.INDEFINITE);
        aiLoop.play();

        gameController.getGameModel().getWinnerProperty().addObserver(new Observer<TankModel>() {
            @Override
            public void observe(Observable<? extends TankModel> observable, TankModel oldValue, TankModel newValue) {
                aiLoop.stop();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, (newValue == player) ? "Вы победили" : "Вы проиграли");
                alert.show();
                alert.showingProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (!newValue1) {
                        new Service<Match>() {
                            @Override
                            protected Task<Match> createTask() {
                                Map<String, Object> args = new HashMap<>();
                                args.put("user", new UserSearch().findBy("login", App.getService(LoginService.class)
                                        .getLoggedUserProperty().get().getLogin()).get(0));
                                args.put("result", (newValue == player) ? Match.Result.WIN : Match.Result.LOSE);
                                args.put("field", new FieldSearch().findBy("name", field.getName()).get(0));
                                matchActionController = new MatchActionController();
                                return new TaskFactory<>(matchActionController.create(args)).create();
                            }

                            @Override
                            protected void failed() {
                                this.getException().printStackTrace();
                            }
                        }.start();
                        backBtn.getOnAction().handle(new ActionEvent());
                    }
                });

                gameController.getGameModel().getWinnerProperty().removeObserver(this);
            }
        });

        backBtn.setOnAction(event -> {
            aiLoop.stop();
            App.getInstance().getPrimaryStage().getScene().setOnKeyPressed(null);
            GameViewController.this.onBackClick();
            GameViewController.this.onBackClick();
        });
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
//            if (direction == Direction.RIGHT) {
//                if (translateTransition.getStatus() == Animation.Status.RUNNING) {
//                    translateTransition.setOnFinished(new EventHandler<ActionEvent>() {
//                        @Override
//                        public void handle(ActionEvent event) {
//                            rotateTransition.play();
//                            translateTransition.setOnFinished(null);
//                        }
//                    });
//                } else {
//                    rotateTransition.play();
//                }
//            } else if (direction == Direction.LEFT) {
//                translateTransition.play();
//            }

//            gameController.onMove(direction);
            playerController.move(direction);
        } else if (event.getCode() == KeyCode.ENTER) {
            wasShot = true;
//            group.getChildren().add(playerController.shoot().getNode());
//            gameController.onShoot(App.getInstance().getGameModel().getPlayer());
        }
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
