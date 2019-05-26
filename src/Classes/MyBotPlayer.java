package Classes;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Bot for playing Eater game
 */

class Graph {
    Position[] vertices;
    LinkedList<Integer> edges[];
    int[][] map;

    Graph(int[][] source, int n) {
        map = source;
        vertices = new Position[n];
        edges = new LinkedList[n];
        for (int i = 0; i < n; i++)
            edges[i] = new LinkedList<Integer>();
    }

    void build() {
        buildVertices();
        buildEdges();
    }

    Integer getVertice(int x, int y) {
        int result = 0;
        for (Position pos : vertices) {
            if (pos.getX() == x && pos.getY() == y)
                break;
            result++;
        }
        return result;
    }

    private void buildVertices() {
        for (int i = 0, num = 0; i < map.length; i++)
            for (int j = 0; j < map.length; j++)
                if (map[i][j] == 0 || map[i][j] == 2)// Empty space
                    vertices[num++] = new Position(j, i);
    }

    private void addEdge(int v, int w) {
        edges[v].add(w);
    }

    private void buildEdges() {
        for (int i = 0, v = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] != 1) {
                    if (j - 1 >= 0 && map[i][j - 1] != 1) { // Left
                        addEdge(v, getVertice(j - 1, i));
                    }
                    if (j + 1 < map.length && map[i][j + 1] != 1) { // Right
                        addEdge(v, getVertice(j + 1, i));
                    }
                    if (i - 1 >= 0 && map[i - 1][j] != 1) { // Up
                        addEdge(v, getVertice(j, i - 1));
                    }
                    if (i + 1 < map.length && map[i + 1][j] != 1) { // Down
                        addEdge(v, getVertice(j, i + 1));
                    }
                    v++;
                }
            }
        }
    }

    LinkedList<Integer> buildPath(int from, int to) { // BFS
        LinkedList<Integer> queue = new LinkedList<Integer>();
        LinkedList<Integer> res = new LinkedList<Integer>();

        int prev[] = new int[vertices.length];

        if (from == to)
            return res;
        queue.add(from);

        for (int i = 0; i < vertices.length; i++)
            prev[i] = -1;

        while (queue.size() != 0) {
            int curr = queue.poll();
            Iterator<Integer> i = edges[curr].listIterator();

            while (i.hasNext()) {
                int n = i.next();
                if (prev[n] == -1) {
                    prev[n] = curr;
                    if (n == to) {
                        while (n != from) {
                            res.addFirst(n);
                            n = prev[n];
                        }
                        return res;
                    }
                    queue.add(n);
                }
            }
        }

        return res;
    }
}

public class MyBotPlayer implements BotPlayer {
    // Current Position of the bot
    private Position position;

    // Food instance reference
    private Food food;

    // Food position reference
    private Position foodPosition;

    // Map instance reference
    private Map map;

    // Game object
    private Circle ball;

    // Map representation on graph
    private Graph graph;

    public MyBotPlayer(Map map) {
        ball = new Circle(map.getUnit() / 2, Color.RED);
        this.map = map;
        position = map.getStartPosition();
        ball.setCenterX(position.getX() * map.getUnit() + map.getUnit() / 2);
        ball.setCenterY(position.getY() * map.getUnit() + map.getUnit() / 2);

        map.getChildren().add(ball);

        graph = new Graph(map.getMap(), map.getEmptyCellsCount());
        graph.build();
    }

    @Override
    public void moveRight() {
    }

    @Override
    public void moveLeft() {
    }

    @Override
    public void moveUp() {
    }

    @Override
    public void moveDown() {
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void feed(Food f) {
        food = f;
    }

    @Override
    public void eat() {
        new Thread() {
            @Override
            public void run() {
                try {
                    do {
                        find();
                        move(null);
                        Thread.sleep(120);
                    } while (count != 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private LinkedList<Integer> movingPath;

    private int count = 10;

    @Override
    public void find() {
        List<Node> temp = ((Pane) map.getChildren().stream().filter(node -> node instanceof Pane)
                .collect(Collectors.toList()).get(0)).getChildren().stream().filter(node -> node instanceof Circle)
                        .collect(Collectors.toList());
        if (temp.size() != 0) {
            foodPosition = food.getPosition();
            int start = graph.getVertice(position.getX(), position.getY());
            int end = graph.getVertice(foodPosition.getX(), foodPosition.getY());
            movingPath = graph.buildPath(start, end);
            if (movingPath.size() != 0)
                count--;
            System.out.println(movingPath);
        }
    }

    @Override
    public void move(KeyCode code) {
        if (movingPath != null) {
            Random rnd = new Random();
            for (int i = 0; i < movingPath.size(); i++) {
                Position newPos = graph.vertices[movingPath.get(i)];

                position.setX(newPos.getX());
                position.setY(newPos.getY());
                ball.setCenterX(position.getX() * map.getUnit() + map.getUnit() / 2);
                ball.setCenterY(position.getY() * map.getUnit() + map.getUnit() / 2);
                try {
                    int max = 2000 / movingPath.size();
                    int min = (int) (max / 2.5);
                    int speed = rnd.nextInt(max + min) + min;
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}