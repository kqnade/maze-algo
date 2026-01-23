package personal.kqnade.model;

import java.awt.Point;
import java.util.*;

public class BFSSearcher implements Searcher {
    private Maze maze;
    private Queue<Point> queue;
    private Set<Point> visited;
    private Map<Point, Point> parentMap;
    private Point current;
    private boolean completed;
    private List<Point> finalPath;
    private Statistics statistics;

    public BFSSearcher(Maze maze) {
        this.maze = maze;
        this.statistics = new Statistics("BFS");
        initialize();
    }

    @Override
    public void initialize() {
        queue = new LinkedList<>();
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
            queue.add(current);
            visited.add(current);
            statistics.setVisitedCellCount(1);
        }
    }

    @Override
    public boolean step() {
        if (completed || queue.isEmpty()) {
            return false;
        }

        current = queue.poll();
        statistics.incrementStepCount();

        if (current.equals(maze.getGoal())) {
            completed = true;
            statistics.setCompleted(true);
            reconstructPath();
            return false;
        }

        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        for (int i = 0; i < 4; i++) {
            int nx = current.x + dx[i];
            int ny = current.y + dy[i];
            Point next = new Point(nx, ny);

            if (maze.isInBounds(nx, ny) && !maze.isWall(nx, ny) && !visited.contains(next)) {
                queue.add(next);
                visited.add(next);
                parentMap.put(next, current);
                statistics.setVisitedCellCount(visited.size());
            }
        }

        return !queue.isEmpty();
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
