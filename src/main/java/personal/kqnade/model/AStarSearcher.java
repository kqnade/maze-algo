package personal.kqnade.model;

import java.awt.Point;
import java.util.*;

public class AStarSearcher implements Searcher {
    private Maze maze;
    private PriorityQueue<Node> openSet;
    private Set<Point> closedSet; // Visited for visualization purposes, or actually closed set
    private Map<Point, Integer> gScore;
    private Map<Point, Point> parentMap;
    private Point current;
    private boolean completed;
    private List<Point> finalPath;
    private Statistics statistics;

    private static class Node implements Comparable<Node> {
        Point position;
        int f; // f = g + h

        Node(Point position, int f) {
            this.position = position;
            this.f = f;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.f, other.f);
        }
    }

    public AStarSearcher(Maze maze) {
        this.maze = maze;
        this.statistics = new Statistics("A*");
        initialize();
    }

    @Override
    public void initialize() {
        openSet = new PriorityQueue<>();
        closedSet = new HashSet<>();
        gScore = new HashMap<>();
        parentMap = new HashMap<>();
        current = maze.getStart();
        completed = false;
        finalPath = null;

        statistics.setStepCount(0);
        statistics.setVisitedCellCount(0);
        statistics.setPathLength(0);
        statistics.setCompleted(false);

        if (current != null) {
            gScore.put(current, 0);
            openSet.add(new Node(current, heuristic(current, maze.getGoal())));
            // Note: In A*, we usually add to closed set when expanding, not when generating.
            // But for visualization, we might want to show what's been "seen" or "expanded".
            // Let's treat closedSet as "expanded" nodes.
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
        
        // If already closed, skip (lazy deletion)
        if (closedSet.contains(current)) {
            return !openSet.isEmpty();
        }

        closedSet.add(current);
        statistics.incrementStepCount();
        statistics.setVisitedCellCount(closedSet.size());

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

            if (maze.isInBounds(nx, ny) && !maze.isWall(nx, ny) && !closedSet.contains(neighbor)) {
                int tentativeG = gScore.getOrDefault(current, Integer.MAX_VALUE) + 1;
                
                if (tentativeG < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    parentMap.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);
                    int f = tentativeG + heuristic(neighbor, maze.getGoal());
                    openSet.add(new Node(neighbor, f));
                }
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
        return new ArrayList<>(closedSet);
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
