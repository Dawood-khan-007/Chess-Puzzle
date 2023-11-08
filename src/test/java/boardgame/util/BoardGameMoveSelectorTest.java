package boardgame.util;

import boardgame.model.BoardGameModel;
import boardgame.model.Position;
import javafx.geometry.Pos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardGameMoveSelectorTest {

    private BoardGameModel model;
    private BoardGameMoveSelector moveSelector;

    @BeforeEach
    public void setUp() {
        model = new BoardGameModel();
        moveSelector = new BoardGameMoveSelector(model);
    }

    @Test
    public void testInitialPhase() {
        assertEquals(BoardGameMoveSelector.Phase.SELECT_FROM, moveSelector.getPhase());
    }

    @Test
    public void testSelectForInvalidPosition() {
        Position from = new Position(0, 3);
        moveSelector.select(from);
        assertNull(moveSelector.getFrom());
        assertNull(moveSelector.getTo());
        assertEquals(BoardGameMoveSelector.Phase.SELECT_FROM, moveSelector.getPhase());
        assertFalse(moveSelector.isInvalidSelection());
    }

    @Test
    public void testSelectFromForFilledPosition() {
        Position from = new Position(0, 0);
        moveSelector.select(from);
        assertEquals(from, moveSelector.getFrom());
        assertEquals(BoardGameMoveSelector.Phase.SELECT_TO, moveSelector.getPhase());
        assertFalse(moveSelector.isInvalidSelection());
    }

    @Test
    public void testSelectFromForEmptyPosition() {
        Position from = new Position(1, 2);
        moveSelector.select(from);
        assertTrue(moveSelector.isInvalidSelection());
        assertEquals(BoardGameMoveSelector.Phase.SELECT_FROM, moveSelector.getPhase());
    }

    @Test
    public void testSelectToForValidMove() {
        Position from = new Position(1, 1);
        Position to = new Position(1, 2);
        moveSelector.select(from);
        moveSelector.selectTo(to);
        assertEquals(to, moveSelector.getTo());
        assertEquals(BoardGameMoveSelector.Phase.READY_TO_MOVE, moveSelector.getPhase());
        assertFalse(moveSelector.isInvalidSelection());
    }

    @Test
    public void testSelectToForInvalidMove() {
        Position from = new Position(0, 0);
        Position to = new Position(1, 1);
        moveSelector.select(from);
        moveSelector.selectTo(to);
        assertNull(moveSelector.getTo());
        assertEquals(BoardGameMoveSelector.Phase.SELECT_TO, moveSelector.getPhase());
        assertTrue(moveSelector.isInvalidSelection());
    }

    @Test
    public void testReset() {
        moveSelector.select(new Position(0, 0));
        moveSelector.select(new Position(0, 1));
        moveSelector.reset();
        assertEquals(BoardGameMoveSelector.Phase.SELECT_FROM, moveSelector.getPhase());
        assertFalse(moveSelector.isInvalidSelection());
        assertNull(moveSelector.getFrom());
        assertNull(moveSelector.getTo());
    }
}
