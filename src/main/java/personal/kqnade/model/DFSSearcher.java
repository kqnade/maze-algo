package personal.kqnade.model;

import java.awt.Point;
import java.util.*;

public class DFSSearcher implements Searcher {
    private Maze maze;
    private Stack<Point> stack;
    private Set<Point> visited;
    private Map<Point, Point> parentMap;
    private Point current;
    private boolean completed;
    private List<Point> finalPath;
    private Statistics statistics;

    public DFSSearcher(Maze maze) {
        this.maze = maze;
        this.statistics = new Statistics("DFS");
        initialize();
    }

    @Override
    public void initialize() {
        stack = new Stack<>();
        visited = new HashSet<>();
        parentMap = new HashMap<>();
        current = maze.getStart();
        completed = false;
        finalPath = null;

        statistics.setStepCount(0);
        statistics.setVisitedCellCount(0);
        statistics.setPathLength(0);
        statistics.setCompleted(false);

        if (current != null) {
            stack.push(current);
            visited.add(current);
            statistics.setVisitedCellCount(1);
        }
    }

    @Override
    public boolean step() {
        if (completed || stack.isEmpty()) {
            return false;
        }

        current = stack.pop();
        statistics.incrementStepCount();

        if (current.equals(maze.getGoal())) {
            completed = true;
            statistics.setCompleted(true);
            reconstructPath();
            return false;
        }

        // Order: Up, Right, Down, Left (reverse push order for correct processing order if desired)
        // Usually we want to process neighbors in a specific order.
        // If we push Left, Down, Right, Up, we pop Up first.
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        // Push neighbors
        for (int i = 0; i < 4; i++) {
            int nx = current.x + dx[i];
            int ny = current.y + dy[i];
            Point next = new Point(nx, ny);

            if (maze.isInBounds(nx, ny) && !maze.isWall(nx, ny) && !visited.contains(next)) {
                stack.push(next);
                visited.add(next);
                parentMap.put(next, current);
                statistics.setVisitedCellCount(visited.size());
            }
        }

        return !stack.isEmpty();
    }

    private void reconstructPath() {
        finalPath = new ArrayList<>();
        Point p = maze.getGoal();
        while (p != null) {
            finalPath.add(p);
            p = parentMap.get(p);
        }
        Collections.reverse(finalPath);
        statistics.setPathLength(finalPath.size());
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public List<Point> getVisitedCells() {
        return new ArrayList<>(visited);
    }

    @Override
    public Point getCurrentPosition() {
        return current;
    }

    @Override
    public List<Point> getFinalPath() {
        return finalPath;
    }

    @Override
    public Statistics getStatistics() {
        return statistics;
    }
}
