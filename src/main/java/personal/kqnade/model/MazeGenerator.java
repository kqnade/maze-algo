package personal.kqnade.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MazeGenerator {
    private Random random = new Random();

    public Maze generate(int width, int height) {
        // Ensure odd dimensions for walls and paths
        if (width % 2 == 0) width++;
        if (height % 2 == 0) height++;

        Maze maze = new Maze(width, height);

        // Initialize all cells as walls
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                maze.setCell(x, y, 1);
            }
        }

        // List of walls (edges)
        // A wall is between two path candidates (odd coordinates)
        // We only consider internal walls that separate potential path cells
        List<Point> walls = new ArrayList<>();
        
        // Initialize UnionFind for all potential path cells
        // Map (x, y) to index: y * width + x
        UnionFind uf = new UnionFind(width * height);

        // Identify potential walls between path cells
        // Vertical walls: (x, y) where x is even, y is odd
        for (int y = 1; y < height - 1; y += 2) {
            for (int x = 2; x < width - 1; x += 2) {
                walls.add(new Point(x, y));
            }
        }
        // Horizontal walls: (x, y) where x is odd, y is even
        for (int y = 2; y < height - 1; y += 2) {
            for (int x = 1; x < width - 1; x += 2) {
                walls.add(new Point(x, y));
            }
        }

        Collections.shuffle(walls, random);

        // Initialize path cells (odd coordinates) as paths (0) temporarily?
        // Actually, Kruskal's usually starts with all walls and carves paths.
        // But here we treat cells (odd, odd) as nodes.
        // Let's set all (odd, odd) cells to 0 (path) initially?
        // No, usually we start with walls and carve.
        // But for UnionFind, we need to know which sets are connected.
        // Let's assume (odd, odd) are the cells we want to connect.
        
        for (int y = 1; y < height; y += 2) {
            for (int x = 1; x < width; x += 2) {
                maze.setCell(x, y, 0);
            }
        }

        for (Point wall : walls) {
            int x = wall.x;
            int y = wall.y;

            // Determine the two cells this wall separates
            int x1, y1, x2, y2;
            if (x % 2 == 0) { // Vertical wall
                x1 = x - 1; y1 = y;
                x2 = x + 1; y2 = y;
            } else { // Horizontal wall
                x1 = x; y1 = y - 1;
                x2 = x; y2 = y + 1;
            }

            int idx1 = y1 * width + x1;
            int idx2 = y2 * width + x2;

            if (!uf.isSame(idx1, idx2)) {
                uf.union(idx1, idx2);
                maze.setCell(x, y, 0); // Remove wall
            }
        }

        // Set start and goal
        // For simplicity, top-left and bottom-right path cells
        maze.setStart(new Point(1, 1));
        maze.setGoal(new Point(width - 2, height - 2));

        return maze;
    }
}
