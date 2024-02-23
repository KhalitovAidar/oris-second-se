package game;

import game.Exceptions.BiteItselfException;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

public class Snake implements Iterable<Point> {
    private final Deque<Point> bodyPosition;
    private final Set<Point> bodyPointsSet;
    private Direction direction;
    private int growingSteps = 0;
    private int steps = 0;

    public Snake(@NotNull Point startingPoint, @NotNull Direction startingDirection) {
        this.bodyPosition = new LinkedList<>();
        this.bodyPosition.offerFirst(startingPoint);

        this.bodyPointsSet = new HashSet<>();
        this.bodyPointsSet.add(startingPoint);

        this.direction = startingDirection;
    }
    @Override
    public synchronized Iterator<Point> iterator() {
        return this.bodyPosition.iterator();
    }

    synchronized boolean eats(@NotNull Food food) {
        if (this.getHeadPoint().equals(food.getPosition())) {
            this.growingSteps += food.getCalorie();
            return true;
        }

        return false;
    }
    synchronized Snake nextStep() throws BiteItselfException {
        Point headPoint = this.bodyPosition.getFirst();

        if (this.growingSteps > 0) {
            --this.growingSteps;
        } else {
            this.bodyPointsSet.remove(
                    this.bodyPosition.pollLast()
            );
        }

        Point nextPoint = headPoint.relativePoint(this.direction);

        this.bodyPosition.offerFirst(nextPoint);
        ++this.steps;

        if (!this.bodyPointsSet.add(nextPoint)) {
            throw new BiteItselfException();
        }

        return this;
    }

    public synchronized int size() {
        return this.bodyPosition.size();
    }

    public synchronized Direction getDirection() {
        return direction;
    }

    synchronized Snake setDirection(@NotNull Direction newDirection) {
        switch (newDirection) {
            case UP:
            case DOWN:
                if (this.direction.equals(Direction.RIGHT) || this.direction.equals(Direction.LEFT)) {
                    this.direction = newDirection;
                }
            case RIGHT:
            case LEFT:
                if (this.direction.equals(Direction.UP) || this.direction.equals(Direction.DOWN)) {
                    this.direction = newDirection;
                }
        }
        return this;
    }

    public synchronized Point getHeadPoint() {
        return this.bodyPosition.getFirst();
    }

    public synchronized Set<Point> diff(Set<Point> points) {
        return points.stream()
                .filter(p -> !this.bodyPointsSet.contains(p))
                .collect(Collectors.toSet());
    }

    synchronized boolean contains(Point point) {
        return this.bodyPointsSet.contains(point);
    }

    public int getSteps() {
        return steps;
    }
}
