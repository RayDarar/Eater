package Classes;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Class Map for holding all the game objects. Constructed from a given text
 * file
 */

public class Map extends Pane {
    // size of one cell (in pixels)
    private int unit;

    // size of map (number of columns/rows)
    private int size;

    // Map data
    private int[][] map;

    // Starting point for the player
    private Position start;

    // Num of empty cells
    private int numOfEmptyCells = 0;

    public Map(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists())
            throw new FileNotFoundException();

        try (Scanner reader = new Scanner(file)) {
            double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 1.6;

            size = reader.nextInt();

            unit = (int) (height / size);

            map = new int[size][size];

            for (int i = 0; i < size; i++) {
                reader.nextLine();
                for (int j = 0; j < size; j++) {
                    map[i][j] = reader.nextInt();

                    Rectangle rect = new Rectangle(j * unit, i * unit, unit, unit);

                    if (map[i][j] == 0 || map[i][j] == 2) { // Empty space
                        rect.setFill(Color.WHITE);
                        rect.setStroke(Color.BLACK);
                        numOfEmptyCells++;
                    }
                    if (map[i][j] == 2) { // Player
                        start = new Position(j, i);
                    }

                    getChildren().add(rect);
                }
            }
        }
    }

    public int getEmptyCellsCount() {
        return numOfEmptyCells;
    }

    public int getUnit() {
        return unit;
    }

    public int getSize() {
        return size;
    }

    public int[][] getMap() {
        return map;
    }

    public Position getStartPosition() {
        return start;
    }
}