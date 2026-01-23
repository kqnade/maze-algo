package personal.kqnade.model;

import java.awt.Point;

public class Maze {
    private int width;
    private int height;
    private int[][] cells; // 0=path, 1=wall
    private Point start;
    private Point goal;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new int[height][width];
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int[][] getCells() { return cells; }
    public Point getStart() { return start; }
    public void setStart(Point start) { this.start = start; }
    public Point getGoal() { return goal; }
    public void setGoal(Point goal) { this.goal = goal; }

    public void setCell(int x, int y, int value) {
        if (isInBounds(x, y)) {
            cells[y][x] = value;
        }
    }

    public int getCell(int x, int y) {
        if (isInBounds(x, y)) {
            return cells[y][x];
        }
        return 1; // Treat out of bounds as wall
    }

    public boolean isWall(int x, int y) {
        return getCell(x, y) == 1;
    }

    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
