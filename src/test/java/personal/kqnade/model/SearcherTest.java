package personal.kqnade.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Point;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SearcherTest {

    private Maze maze;

    @BeforeEach
    void setUp() {
        // Create a simple 5x5 maze with a clear path
        // 1 1 1 1 1
        // 1 S 0 0 1
        // 1 1 1 0 1
        // 1 G 0 0 1
        // 1 1 1 1 1
        maze = new Maze(5, 5);
        for(int y=0; y<5; y++) for(int x=0; x<5; x++) maze.setCell(x, y, 1);
        
        maze.setCell(1, 1, 0); // Start
        maze.setCell(2, 1, 0);
        maze.setCell(3, 1, 0);
        maze.setCell(3, 2, 0);
        maze.setCell(3, 3, 0);
        maze.setCell(2, 3, 0);
        maze.setCell(1, 3, 0); // Goal

        maze.setStart(new Point(1, 1));
        maze.setGoal(new Point(1, 3));
    }

    @Test
    void testBFS() {
        Searcher searcher = new BFSSearcher(maze);
        runSearcher(searcher);
        assertTrue(searcher.isCompleted());
        assertNotNull(searcher.getFinalPath());
        assertEquals(new Point(1, 3), searcher.getFinalPath().get(searcher.getFinalPath().size()-1));
    }

    @Test
    void testDFS() {
        Searcher searcher = new DFSSearcher(maze);
        runSearcher(searcher);
        assertTrue(searcher.isCompleted());
        assertNotNull(searcher.getFinalPath());
    }

    @Test
    void testAStar() {
        Searcher searcher = new AStarSearcher(maze);
        runSearcher(searcher);
        assertTrue(searcher.isCompleted());
        assertNotNull(searcher.getFinalPath());
    }

    @Test
    void testDijkstra() {
        Searcher searcher = new DijkstraSearcher(maze);
        runSearcher(searcher);
        assertTrue(searcher.isCompleted());
        assertNotNull(searcher.getFinalPath());
    }

    @Test
    void testGreedy() {
        Searcher searcher = new GreedySearcher(maze);
        runSearcher(searcher);
        assertTrue(searcher.isCompleted());
        assertNotNull(searcher.getFinalPath());
    }

    private void runSearcher(Searcher searcher) {
        int maxSteps = 100;
        while (!searcher.isCompleted() && maxSteps-- > 0) {
            if (!searcher.step()) break;
        }
    }
}
