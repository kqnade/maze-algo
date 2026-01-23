package personal.kqnade.model;

import java.awt.Point;
import java.util.*;

public class DijkstraSearcher implements Searcher {
    private Maze maze;
    private PriorityQueue<Node> queue;
    private Set<Point> visited; // Closed set
    private Map<Point, Integer> distance;
    private Map<Point, Point> parentMap;
    private Point current;
    private boolean completed;
    private List<Point> finalPath;
    private Statistics statistics;

    private static class Node implements Comparable<Node> {
        Point position;
        int cost;

        Node(Point position, int cost) {
            this.position = position;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.cost, other.cost);
        }
    }

    public DijkstraSearcher(Maze maze) {
        this.maze = maze;
        this.statistics = new Statistics("Dijkstra");
        initialize();
    }

    @Override
    public void initialize() {
        queue = new PriorityQueue<>();
        visited = new HashSet<>();
        distance = new HashMap<>();
        parentMap = new HashMap<>();
        current = maze.getStart();
        completed = false;
        finalPath = null;

        statistics.setStepCount(0);
        statistics.setVisitedCellCount(0);
        statistics.setPathLength(0);
        statistics.setCompleted(false);

        if (current != null) {
            distance.put(current, 0);
            queue.add(new Node(current, 0));
        }
    }

    @Override
    public boolean step() {
        if (completed || queue.isEmpty()) {
            return false;
        }

        Node currentNode = queue.poll();
        current = currentNode.position;

        if (visited.contains(current)) {
            return !queue.isEmpty();
        }

        visited.add(current);
        statistics.incrementStepCount();
        statistics.setVisitedCellCount(visited.size());

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
                int newDist = distance.getOrDefault(current, Integer.MAX_VALUE) + 1;
                
                if (newDist < distance.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distance.put(neighbor, newDist);
                    parentMap.put(neighbor, current);
                    queue.add(new Node(neighbor, newDist));
                }
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
