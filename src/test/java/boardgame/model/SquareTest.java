package boardgame.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SquareTest {

    @Test
    public void testEnumValues() {

        Square noneSquare = Square.NONE;
        Square kingSquare = Square.KING;
        Square rookSquare = Square.ROOK;
        Square bishopSquare = Square.BISHOP;

        assertEquals("NONE", noneSquare.name());
        assertEquals("KING", kingSquare.name());
        assertEquals("ROOK", rookSquare.name());
        assertEquals("BISHOP", bishopSquare.name());

        assertEquals(0, noneSquare.ordinal());
        assertEquals(1, kingSquare.ordinal());
        assertEquals(2, rookSquare.ordinal());
        assertEquals(3, bishopSquare.ordinal());
    }
}
