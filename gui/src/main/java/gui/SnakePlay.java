package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import game.ControllerEvent;
import game.Exceptions.BiteItselfException;
import game.Exceptions.BiteWallException;
import game.Game;

public class SnakePlay extends Application {

    private MainController mainController = new MainController();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Snake game");

        CanvasPane canvasPane = new CanvasPane(600, 600);

        Scene scene = new Scene(canvasPane);
        stage.setScene(scene);

        initInputHandler(scene);
        initGame(canvasPane.getCanvas());

        stage.show();
    }

    private void initGame(Canvas canvas) {
        Thread gameThread = new Thread(
                () -> {
                    try {
                        new Game(this.mainController, new GameRenderer(canvas)).run();
                    } catch (BiteItselfException e) {
                        e.printStackTrace();
                    } catch (BiteWallException e) {
                        e.printStackTrace();
                    }
                }
        );

        gameThread.start();
    }

    private void initInputHandler(Scene scene) {
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case K:
                case W:
                case UP:
                    this.mainController.sendEvent(ControllerEvent.KEY_UP);
                    break;

                case J:
                case S:
                case DOWN:
                    this.mainController.sendEvent(ControllerEvent.KEY_DOWN);
                    break;

                case L:
                case D:
                case RIGHT:
                    this.mainController.sendEvent(ControllerEvent.KEY_RIGHT);
                    break;

                case H:
                case A:
                case LEFT:
                    this.mainController.sendEvent(ControllerEvent.KEY_LEFT);
                    break;
            }
        });
    }
}
