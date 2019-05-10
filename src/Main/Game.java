package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

import Classes.Food;
import Classes.Map;
import Classes.MyBotPlayer;
import Classes.MyPlayer;
import Classes.Player;
import Classes.Position;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Game class extends javafx Application. Launching gui
 * 
 * @author Ryspekov Ansar
 * 
 * @version 0.1 Writing base of the project, working with
 *          packages/folders/resources.
 * 
 * @version 0.2 Writing sequence of methods calls w/o logic
 * 
 * @version 0.3 Commenting each method
 * 
 * @version 0.4 Writing basic logic
 * 
 * @version 0.5 Completing part 1 of the project
 * 
 * @version 0.6 Creating MyBotPlayer class, which implements BotPlayer interface
 * 
 * @version 0.7 Implementing graph and bfs algorithm for bot
 * 
 * @version 0.8 writing random map generation
 * 
 * @version 0.9 Putting Game.java in Main package + method for finished game
 * 
 * @method main - Starting point of the application (param: args - console
 *         arguments)
 * 
 * @method objectsSetup - showing menu for map loading settings
 * 
 * @method mapSetup - creating instances for objects
 * 
 * @method start - javafx method for logic when starting the application (param:
 *         stage - main window of the application)
 */

public class Game extends Application {
    // Created game map
    private Map map;

    // Created game player
    private Player player;

    // Creates game food
    private Food food;

    // Root of scene
    private Group root = new Group();

    // Width and also height of application
    private float width;

    // Name of file from which map will be created
    private static String fileName;

    public static void main(String[] args) throws Exception {
        if (args.length != 0)
            fileName = args[0];

        launch(args);
    }

    private void mapSetup(String fileName, boolean playerType) {
        try {
            map = new Map(fileName);
            if (playerType) {
                player = new MyPlayer(map);
                food = new Food(map, player);
                root.getScene().addEventHandler(KeyEvent.KEY_RELEASED, key -> {
                    player.move(key.getCode());
                });
            } else {
                player = new MyBotPlayer(map);
                food = new Food(map, player);
                ((MyBotPlayer) player).feed(food);
                ((MyBotPlayer) player).eat();
            }

            width = map.getUnit() * map.getSize();
            root.getScene().getWindow().setWidth(width + 15);
            root.getScene().getWindow().setHeight(width + 35);
            root.getChildren().clear();
            root.getChildren().add(map);
        } catch (Exception e) {
            Alert error = new Alert(AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("Bad chosen map");
            error.setContentText("Chose another one");
            error.showAndWait();
            e.printStackTrace();
        }
    }

    private void objectsSetup() throws FileNotFoundException {
        ToggleGroup group = new ToggleGroup();

        RadioButton playerRButton = new RadioButton("Player");
        RadioButton botRButton = new RadioButton("Bot");
        playerRButton.setSelected(true);
        playerRButton.setToggleGroup(group);
        botRButton.setToggleGroup(group);

        Button btn1 = new Button("Map from file");
        btn1.setOnAction(action -> {
            FileChooser chooser = new FileChooser();
            chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            chooser.setTitle("Choose map file");
            File file = chooser.showOpenDialog(root.getScene().getWindow());
            if (file != null) {
                mapSetup(file.getAbsolutePath(), playerRButton.isSelected());
            }
        });
        Button btn2 = new Button("Map from console arguments");
        btn2.setOnAction(action -> {
            mapSetup(System.getProperty("user.dir") + "\\res\\" + fileName, playerRButton.isSelected());
        });

        Button btn3 = new Button("Generate random map");
        btn3.setOnAction(action -> {
            int size = rnd.nextInt(47) + 7;
            size = size % 2 != 0 ? size : size - 1;
            mapGeneration(size);
            mapSetup(fileName, playerRButton.isSelected());
        });

        root.getChildren().add(new VBox(30, new HBox(20, btn1, btn2, btn3), new HBox(20, playerRButton, botRButton)));
    }

    private Random rnd = new Random();

    private void mapGeneration(int size) {
        String data = "";
        int[][] maze = new int[size][size];

        // Maze init
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; maze[i][j] = i % 2 != 0 && j % 2 != 0 ? 3 : 1, j++)
                ;

        // Player generation
        int playerX = rnd.nextInt(size - 1);
        playerX = playerX % 2 != 0 ? playerX : playerX + 1;
        int playerY = rnd.nextInt(size - 1);
        playerY = playerY % 2 != 0 ? playerY : playerY + 1;

        // Maze gen
        int x = playerX;
        int y = playerY;
        Stack<Position> positions = new Stack<>();
        positions.push(new Position(x, y)); // Player position
        ArrayList<Integer> orientation = new ArrayList<Integer>(); // 0 left, 1 right, 2 up, 3 down
        do {
            Position current = positions.peek();
            x = current.getX();
            y = current.getY();

            // Adding possible directions (it's not out of maze & unvisited)
            if (x - 2 >= 0 && maze[y][x - 2] != 0) // Left
                orientation.add(0);
            if (x + 2 < size && maze[y][x + 2] != 0) // Right
                orientation.add(1);
            if (y - 2 >= 0 && maze[y - 2][x] != 0) // Up
                orientation.add(2);
            if (y + 2 < size && maze[y + 2][x] != 0) // Down
                orientation.add(3);

            // Check if it has possible directions
            if (orientation.size() != 0) { // Have more ways to go
                Collections.shuffle(orientation);
                Position newPos = new Position(x, y);
                maze[y][x] = 0;
                switch (orientation.get(0)) {
                case 0: // Left
                    maze[y][x - 1] = 0;
                    maze[y][x - 2] = 0;
                    newPos.setX(x - 2);
                    break;
                case 1: // Right
                    maze[y][x + 1] = 0;
                    maze[y][x + 2] = 0;
                    newPos.setX(x + 2);
                    break;
                case 2: // Up
                    maze[y - 1][x] = 0;
                    maze[y - 2][x] = 0;
                    newPos.setY(y - 2);
                    break;
                case 3: // Down
                    maze[y + 1][x] = 0;
                    maze[y + 2][x] = 0;
                    newPos.setY(y + 2);
                    break;
                }
                positions.push(newPos);
            } else // Go back by stack
                positions.pop();

            orientation.clear();
        } while (positions.size() != 0);

        maze[playerY][playerX] = 2;

        // File representation of a maze
        data += size + "\n";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (j != size - 1)
                    data += maze[i][j] + " ";
                else
                    data += maze[i][j];
            }
            if (i != size - 1)
                data += "\n";
        }

        fileName = System.getProperty("user.dir") + "\\res\\Map_random.txt";
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            out.write(data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void end(int score) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game over");
        alert.setHeaderText("Game over. Your score is: " + score);
        alert.showAndWait();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(root));

        objectsSetup();

        stage.setTitle("Eater");
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        System.exit(0);
        super.stop();
    }
}