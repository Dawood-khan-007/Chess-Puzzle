package boardgame.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PositionTest {

    @Test
    public void testToString() {

        Position position = new Position(2, 3);
        assertEquals("(2, 3)", position.toString());

        Position position2 = new Position(0, 0);
        assertEquals("(0, 0)", position2.toString());
    }

}
