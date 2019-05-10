package Classes;

/**
 * Stores the position (x and y coordinates)
 * 
 * @method getX - return value of x
 * 
 * @method getY - return value of Y
 * 
 * @method setX - set new value of x
 * 
 * @method setY - set new value of y
 * 
 * @method equals - compares coordinates of two positions
 */

public class Position {
    // X coordinate
    private int x;

    // Y coordinate
    private int y;

    public Position(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean equals(Position pos) {
        return x == pos.x && y == pos.y;
    }
}