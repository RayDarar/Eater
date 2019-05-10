package Classes;

import javafx.scene.input.KeyCode;

/**
 * Interface for the Player, with basic commands
 */

public interface Player {
    public void move(KeyCode code);

    public void moveRight();

    public void moveLeft();

    public void moveUp();

    public void moveDown();

    public Position getPosition();
}