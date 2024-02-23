package game;

import game.Exceptions.BiteItselfException;
import game.Exceptions.BiteWallException;
import game.Exceptions.ThereIsNoPointsLeftException;

public class Game implements ControllerListener {
    
    private final Board board;
    private final Snake snake;
    private Food food;

    private StepsCalculator stepsCalc;

    private final Renderer renderer;

    public Game(Controller theController, Renderer theRenderer) {
        this.board = BoardFactory.buildSquareBoard(15);
        this.snake = this.board.buildSnake();

        this.renderer = theRenderer;

        theController.addListener(this);
    }

    public void update(ControllerEvent controllerEvent) {
        switch (controllerEvent) {
            case KEY_UP:
                this.snake.setDirection(Direction.UP);
                break;

            case KEY_DOWN:
                this.snake.setDirection(Direction.DOWN);
                break;

            case KEY_RIGHT:
                this.snake.setDirection(Direction.RIGHT);
                break;

            case KEY_LEFT:
                this.snake.setDirection(Direction.LEFT);
                break;
        }
    }

    public void run() throws BiteItselfException, BiteWallException {
        this.stepsCalc = new StepsCalculator(5);

        try {
            this.food = this.buildFood();
            this.render();
            this.mainLoop();
        } catch (ThereIsNoPointsLeftException e) {
        }
    }

    public Board getBoard() {
        return board;
    }

    public Snake getSnake() {
        return snake;
    }

    public Food getFood() {
        return food;
    }

    private void mainLoop()
            throws BiteItselfException, BiteWallException, ThereIsNoPointsLeftException {

        while (this.board.getNumberOfInnerPoints() > this.snake.size()) {
            if (this.snake.getSteps() < this.stepsCalc.getSteps()) {
                this.snake.nextStep();

                if (!this.board.isAPointInside(this.snake.getHeadPoint())) {
                    throw new BiteWallException();
                }

                if (this.snake.eats(this.food)) {
                    this.food = this.buildFood();
                }

                this.render();
            }
        }
    }

    private Food buildFood() throws ThereIsNoPointsLeftException {
        return this.board.buildFood(1, this.snake);
    }

    private void render() {
        this.renderer.render(this);
    }
}
