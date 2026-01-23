package personal.kqnade.model;

import java.awt.Point;
import java.util.*;

public class GreedySearcher implements Searcher {
    private Maze maze;
    private PriorityQueue<Node> openSet;
    private Set<Point> visited;
    private Map<Point, Point> parentMap;
    private Point current;
    private boolean completed;
    private List<Point> finalPath;
    private Statistics statistics;

    private static class Node implements Comparable<Node> {
        Point position;
        int h; // heuristic only

        Node(Point position, int h) {
            this.position = position;
            this.h = h;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.h, other.h);
        }
    }

    public GreedySearcher(Maze maze) {
        this.maze = maze;
        this.statistics = new Statistics("Greedy");
        initialize();
    }

    @Override
    public void initialize() {
        openSet = new PriorityQueue<>();
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
            openSet.add(new Node(current, heuristic(current, maze.getGoal())));
            // Note: Greedy often doesn't use a closed set in the same strict way as A* for optimality,
            // but to avoid cycles we need to track visited.
            // However, pure Greedy Best-First Search can get stuck in loops if not careful or if the graph is weighted weirdly.
            // On a grid, tracking visited is good.
            visited.add(current);
            statistics.setVisitedCellCount(1);
        }
    }

    private int heuristic(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    @Override
    public boolean step() {
        if (completed || openSet.isEmpty()) {
            return false;
        }

        Node currentNode = openSet.poll();
        current = currentNode.position;
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
            Point neighbor = new Point(nx, ny);

            if (maze.isInBounds(nx, ny) && !maze.isWall(nx, ny) && !visited.contains(neighbor)) {
                visited.add(neighbor);
                parentMap.put(neighbor, current);
                statistics.setVisitedCellCount(visited.size());
                openSet.add(new Node(neighbor, heuristic(neighbor, maze.getGoal())));
            }
        }

        return !openSet.isEmpty();
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
