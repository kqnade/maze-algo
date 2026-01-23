package personal.kqnade.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnionFindTest {

    @Test
    void testUnionFind() {
        UnionFind uf = new UnionFind(10);

        // Initially everyone is in their own set
        assertFalse(uf.isSame(0, 1));

        uf.union(0, 1);
        assertTrue(uf.isSame(0, 1));
        
        uf.union(1, 2);
        assertTrue(uf.isSame(0, 2)); // Transitivity

        assertFalse(uf.isSame(0, 3));
    }
}
