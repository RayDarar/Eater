package Classes;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Implements the Player interface. Creates the player as a ball on the board.
 * Moves the ball
 * 
 * @method move - changes position of the ball
 * 
 * @method moveLeft - move player to the left by one cell
 * 
 * @method moveRight - move player to the right by one cell
 * 
 * @method moveUp - moves player up by one cell
 * 
 * @method moveDown - moves player down by one cell
 * 
 * @method getPosition - return current position of player on the map
 */

public class MyPlayer implements Player {
    // Player model on the map
    private Circle ball;

    // Map reference
    private Map map;

    // Current player position
    private Position position;

    public MyPlayer(Map map) {
        this.map = map;
        position = map.getStartPosition();

        ball = new Circle(map.getUnit() / 2);
        ball.setFill(Color.RED);

        map.getChildren().add(ball);
        move(KeyCode.T);
    }

    public void move(KeyCode code) {
        switch (code) {
        case RIGHT:
        case D:
            moveRight();
            break;
        case LEFT:
        case A:
            moveLeft();
            break;
        case UP:
        case W:
            moveUp();
            break;
        case DOWN:
        case S:
            moveDown();
            break;
        default:
        }

        ball.setCenterX(position.getX() * map.getUnit() + map.getUnit() / 2);
        ball.setCenterY(position.getY() * map.getUnit() + map.getUnit() / 2);
    }

    @Override
    public void moveLeft() {
        int x = position.getX();
        if (x - 1 >= 0 && map.getMap()[position.getY()][x - 1] != 1)
            position.setX(x - 1);
    }

    @Override
    public void moveRight() {
        int x = position.getX();
        if (x + 1 < map.getSize() && map.getMap()[position.getY()][x + 1] != 1)
            position.setX(x + 1);
    }

    @Override
    public void moveUp() {
        int y = position.getY();
        if (y - 1 >= 0 && map.getMap()[y - 1][position.getX()] != 1)
            position.setY(y - 1);
    }

    @Override
    public void moveDown() {
        int y = position.getY();
        if (y + 1 < map.getSize() && map.getMap()[y + 1][position.getX()] != 1)
            position.setY(y + 1);
    }

    @Override
    public Position getPosition() {
        return position;
    }

}