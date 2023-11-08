package boardgame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardGameModelTest {

    private BoardGameModel model;

    @BeforeEach
    public void setUp() {
        model = new BoardGameModel();
    }

    @Test
    public void testInitialBoardState() {
        assertEquals(Square.KING, model.getSquare(new Position(0, 0)));
        assertEquals(Square.BISHOP, model.getSquare(new Position(0, 1)));
        assertEquals(Square.BISHOP, model.getSquare(new Position(0, 2)));
        assertEquals(Square.ROOK, model.getSquare(new Position(1, 0)));
        assertEquals(Square.ROOK, model.getSquare(new Position(1, 1)));
        assertEquals(Square.NONE, model.getSquare(new Position(1, 2)));
    }

    @Test
    public void testSquareProperty() {
        assertSame(model.squareProperty(0, 0).get(), model.getSquare(new Position(0, 0)));
        assertSame(model.squareProperty(1, 1).get(), model.getSquare(new Position(1, 1)));
        assertSame(model.squareProperty(0, 2).get(), model.getSquare(new Position(0, 2)));
    }

    @Test
    public void testSetSquare() {
        model.setSquare(new Position(0, 0), Square.NONE);
        assertEquals(Square.NONE, model.getSquare(new Position(0, 0)));

        model.setSquare(new Position(1, 2), Square.KING);
        assertEquals(Square.KING, model.getSquare(new Position(1, 2)));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(model.isEmpty(new Position(1, 2)));

        model.setSquare(new Position(1, 2), Square.ROOK);
        assertFalse(model.isEmpty(new Position(1, 2)));
    }

    @Test
    public void testIsOnBoard() {
        assertTrue(BoardGameModel.isOnBoard(new Position(0, 0)));
        assertTrue(BoardGameModel.isOnBoard(new Position(1, 2)));

        assertFalse(BoardGameModel.isOnBoard(new Position(-1, 0)));
        assertFalse(BoardGameModel.isOnBoard(new Position(0, 3)));
        assertFalse(BoardGameModel.isOnBoard(new Position(2, 2)));
    }

    @Test
    public void testCanMove() {
        assertTrue(model.canMove(new Position(1, 1), new Position(1, 2)));
        assertTrue(model.canMove(new Position(0, 1), new Position(1, 2)));
    }

    @Test
    public void testMove() {
        model.move(new Position(0, 0), new Position(1, 1));
        assertEquals(Square.NONE, model.getSquare(new Position(0, 0)));
        assertEquals(Square.KING, model.getSquare(new Position(1, 1)));

        model.move(new Position(0, 1), new Position(0, 0));
        assertEquals(Square.NONE, model.getSquare(new Position(0, 1)));
        assertEquals(Square.BISHOP, model.getSquare(new Position(0, 0)));
    }

    @Test
    public void testIsValidSourcePosition() {
        assertTrue(model.isValidSourcePosition(new Position(0, 0)));
        assertTrue(model.isValidSourcePosition(new Position(1, 1)));
        assertTrue(model.isValidSourcePosition(new Position(0, 2)));
        assertFalse(model.isValidSourcePosition(new Position(1, 2)));
    }

    @Test
    public void testFindEmptyPosition() {
        Position emptyPosition = model.findEmptyPosition();
        assertNotNull(emptyPosition);
        assertTrue(model.isEmpty(emptyPosition));

        model.setSquare(emptyPosition, Square.KING);
        model.setSquare(new Position(0, 0), Square.NONE);
        Position nextEmptyPosition = model.findEmptyPosition();
        assertNotNull(nextEmptyPosition);
        assertNotEquals(emptyPosition, nextEmptyPosition);
        assertTrue(model.isEmpty(nextEmptyPosition));
    }

    @Test
    public void testToString() {
        String expectedString = "1 3 3 \n2 2 0 \n";
        assertEquals(expectedString, model.toString());
    }
}
