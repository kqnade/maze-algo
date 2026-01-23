package personal.kqnade.model;

import java.awt.Point;
import java.util.List;

public interface Searcher {
    void initialize();
    boolean step();
    boolean isCompleted();
    List<Point> getVisitedCells();
    Point getCurrentPosition();
    List<Point> getFinalPath();
    Statistics getStatistics();
}
