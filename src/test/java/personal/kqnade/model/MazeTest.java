package personal.kqnade.model;

import org.junit.jupiter.api.Test;
import java.awt.Point;
import static org.junit.jupiter.api.Assertions.*;

class MazeTest {

    @Test
    void testMazeInitialization() {
        Maze maze = new Maze(10, 10);
        assertEquals(10, maze.getWidth());
        assertEquals(10, maze.getHeight());
        assertNotNull(maze.getCells());
    }

    @Test
    void testSetAndGetCell() {
        Maze maze = new Maze(5, 5);
        maze.setCell(2, 2, 1); // Set wall
        assertEquals(1, maze.getCell(2, 2));
        assertTrue(maze.isWall(2, 2));

        maze.setCell(2, 2, 0); // Set path
        assertEquals(0, maze.getCell(2, 2));
        assertFalse(maze.isWall(2, 2));
    }

    @Test
    void testBounds() {
        Maze maze = new Maze(5, 5);
        assertTrue(maze.isInBounds(0, 0));
        assertTrue(maze.isInBounds(4, 4));
        assertFalse(maze.isInBounds(-1, 0));
        assertFalse(maze.isInBounds(0, 5));
    }

    @Test
    void testStartAndGoal() {
        Maze maze = new Maze(5, 5);
        Point start = new Point(0, 0);
        Point goal = new Point(4, 4);
        
        maze.setStart(start);
        maze.setGoal(goal);
        
        assertEquals(start, maze.getStart());
        assertEquals(goal, maze.getGoal());
    }
}
