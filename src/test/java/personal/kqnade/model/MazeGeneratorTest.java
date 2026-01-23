package personal.kqnade.model;

import org.junit.jupiter.api.Test;
import java.awt.Point;
import static org.junit.jupiter.api.Assertions.*;

class MazeGeneratorTest {

    @Test
    void testGenerateMaze() {
        MazeGenerator generator = new MazeGenerator();
        Maze maze = generator.generate(11, 11); // Odd dimensions preferred

        assertNotNull(maze);
        assertEquals(11, maze.getWidth());
        assertEquals(11, maze.getHeight());
        assertNotNull(maze.getStart());
        assertNotNull(maze.getGoal());

        // Start and Goal should not be walls
        assertFalse(maze.isWall(maze.getStart().x, maze.getStart().y));
        assertFalse(maze.isWall(maze.getGoal().x, maze.getGoal().y));
    }
    
    @Test
    void testGenerateMazeEvenDimensions() {
        MazeGenerator generator = new MazeGenerator();
        // Generator adjusts even dimensions to odd
        Maze maze = generator.generate(10, 10); 
        
        assertEquals(11, maze.getWidth());
        assertEquals(11, maze.getHeight());
    }
}
