package boardgame.util;

import boardgame.model.BoardGameModel;
import boardgame.model.Position;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 * The BoardGameMoveSelector class is a utility class for managing the selection and movement of positions on a board game.
 * It provides methods to handle the selection of positions, track the current phase of the move selection process,
 * and determine the validity of selections.
 */
public class BoardGameMoveSelector {

    /**
     * The Phase enum represents the different phases of the move selection process.
     * Selecting the source position
     * Selecting the destination position
     * Ready to perform the move
     */
    public enum Phase {
        SELECT_FROM,
        SELECT_TO,
        READY_TO_MOVE
    }

    private final BoardGameModel model;
    private final ReadOnlyObjectWrapper<Phase> phase = new ReadOnlyObjectWrapper<>(Phase.SELECT_FROM);
    private boolean invalidSelection;
    private Position from;
    private Position to;

    /**
     * Constructs a BoardGameMoveSelector with the specified BoardGameModel.
     *
     * @param model the board game model to be used
     */
    public BoardGameMoveSelector(BoardGameModel model) {
        this.model = model;
        invalidSelection = false;
    }

    /**
     * Returns the current phase of the move selection process.
     *
     * @return the current phase
     */
    public Phase getPhase() {
        return phase.get();
    }

    /**
     * Selects a position based on the current phase of the move selection process.
     * This method updates the internal state of the move selector accordingly.
     *
     * @param position the position to select
     */
    public void select(Position position) {
        if (!BoardGameModel.isOnBoard(position)) {
            reset();
        } else {
            switch (phase.get()) {
                case SELECT_FROM -> selectFrom(position);
                case SELECT_TO -> selectTo(position);
                case READY_TO_MOVE -> reset(); // Reset if already in READY_TO_MOVE phase
            }
        }
    }

    /**
     * Selects the "from" position in the move selection process.
     * If the selected position is not empty, it sets the "from" position, updates the phase to SELECT_TO,
     * and marks the selection as valid. Otherwise, it marks the selection as invalid and keeps the phase as SELECT_FROM.
     *
     * @param position the position selected as the "from" position
     */

    public void selectFrom(Position position) {
        if (!model.isEmpty(position)) {
            from = position;
            phase.set(Phase.SELECT_TO);
            invalidSelection = false;
        } else {
            invalidSelection = true;
            phase.set(Phase.SELECT_FROM);
        }
    }

    /**
     * Selects the "to" position in the move selection process.
     * If the move from the current "from" position to the selected "to" position is valid,
     * it sets the "to" position, updates the phase to READY_TO_MOVE,
     * and marks the selection as valid. Otherwise, it marks the selection as invalid and keeps the phase as SELECT_TO.
     *
     * @param position the position selected as the "to" position
     */

    public void selectTo(Position position) {
        if (model.canMove(from, position)) {
            to = position;
            phase.set(Phase.READY_TO_MOVE);
            invalidSelection = false;
        } else {
            invalidSelection = true;
            phase.set(Phase.SELECT_TO);
        }
    }

    /**
     * Returns the selected source position.
     *
     * @return the selected source position, or null if not in the appropriate phase
     */
    public Position getFrom() {
        if (phase.get() == Phase.SELECT_FROM) {
            return null;
        }
        return from;
    }

    /**
     * Returns the selected destination position.
     *
     * @return the selected destination position, or null if not in the appropriate phase
     */
    public Position getTo() {
        if (phase.get() != Phase.READY_TO_MOVE) {
            return null;
        }
        return to;
    }

    /**
     * Checks if the current selection is invalid.
     *
     * @return true if the selection is invalid, false otherwise
     */
    public boolean isInvalidSelection() {
        return invalidSelection;
    }

    /**
     * Resets the move selection process, clearing the selected positions and resetting the phase.
     */
    public void reset() {
        from = null;
        to = null;
        phase.set(Phase.SELECT_FROM);
        invalidSelection = false;
    }
}
