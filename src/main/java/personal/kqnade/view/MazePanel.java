package personal.kqnade.view;

import personal.kqnade.model.Maze;
import personal.kqnade.model.Searcher;
import personal.kqnade.model.Statistics;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

public class MazePanel extends JPanel {
    private Maze maze;
    private Searcher searcher;
    private int cellSize = 20;
    private String title;

    private static final Color WALL_COLOR = Color.BLACK;
    private static final Color PATH_COLOR = Color.WHITE;
    private static final Color VISITED_COLOR = new Color(200, 200, 255);
    private static final Color CURRENT_COLOR = Color.YELLOW;
    private static final Color START_COLOR = Color.GREEN;
    private static final Color GOAL_COLOR = Color.RED;
    private static final Color FINAL_PATH_COLOR = Color.MAGENTA;

    public MazePanel(String title) {
        this.title = title;
        setBackground(Color.WHITE);
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        repaint();
    }

    public void setSearcher(Searcher searcher) {
        this.searcher = searcher;
        repaint();
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (maze == null) {
            return;
        }

        // Draw Title
        g.setColor(Color.BLACK);
        g.drawString(title, 10, 15);

        int xOffset = 10;
        int yOffset = 25;

        // 1. Draw Maze Base (Walls and Paths)
        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                if (maze.isWall(x, y)) {
                    g.setColor(WALL_COLOR);
                } else {
                    g.setColor(PATH_COLOR);
                }
                g.fillRect(xOffset + x * cellSize, yOffset + y * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(xOffset + x * cellSize, yOffset + y * cellSize, cellSize, cellSize);
            }
        }

        // 2. Draw Start and Goal (Background layer)
        if (maze.getStart() != null) {
            g.setColor(START_COLOR);
            Point p = maze.getStart();
            g.fillRect(xOffset + p.x * cellSize, yOffset + p.y * cellSize, cellSize, cellSize);
            g.setColor(Color.GRAY);
            g.drawRect(xOffset + p.x * cellSize, yOffset + p.y * cellSize, cellSize, cellSize);
        }
        if (maze.getGoal() != null) {
            g.setColor(GOAL_COLOR);
            Point p = maze.getGoal();
            g.fillRect(xOffset + p.x * cellSize, yOffset + p.y * cellSize, cellSize, cellSize);
            g.setColor(Color.GRAY);
            g.drawRect(xOffset + p.x * cellSize, yOffset + p.y * cellSize, cellSize, cellSize);
        }

        if (searcher != null) {
            // 3. Draw Visited
            g.setColor(VISITED_COLOR);
            for (Point p : searcher.getVisitedCells()) {
                if (!p.equals(maze.getStart()) && !p.equals(maze.getGoal())) {
                    g.fillRect(xOffset + p.x * cellSize, yOffset + p.y * cellSize, cellSize, cellSize);
                    g.setColor(Color.GRAY);
                    g.drawRect(xOffset + p.x * cellSize, yOffset + p.y * cellSize, cellSize, cellSize);
                }
            }

            // 4. Draw Current Position
            Point current = searcher.getCurrentPosition();
            if (current != null) {
                g.setColor(CURRENT_COLOR);
                g.fillRect(xOffset + current.x * cellSize, yOffset + current.y * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(xOffset + current.x * cellSize, yOffset + current.y * cellSize, cellSize, cellSize);
            }

            // 5. Draw Final Path (Topmost layer)
            List<Point> finalPath = searcher.getFinalPath();
            if (finalPath != null && !finalPath.isEmpty()) {
                g.setColor(FINAL_PATH_COLOR);
                for (Point p : finalPath) {
                    // Draw as a smaller rectangle or circle to distinguish from visited
                    // But user wants "the path", so full fill is better.
                    // To ensure it's visible over everything, we draw it last.
                    g.fillRect(xOffset + p.x * cellSize, yOffset + p.y * cellSize, cellSize, cellSize);
                    g.setColor(Color.GRAY);
                    g.drawRect(xOffset + p.x * cellSize, yOffset + p.y * cellSize, cellSize, cellSize);
                    g.setColor(FINAL_PATH_COLOR); // Reset color for next fill
                }
            }
            
            // Draw Statistics
            Statistics stats = searcher.getStatistics();
            g.setColor(Color.BLACK);
            String statsText = String.format("Steps: %d, Visited: %d, Path: %d", 
                stats.getStepCount(), stats.getVisitedCellCount(), stats.getPathLength());
            g.drawString(statsText, xOffset, yOffset + maze.getHeight() * cellSize + 15);
        }
    }
}
