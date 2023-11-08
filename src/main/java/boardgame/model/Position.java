package boardgame.model;

/**
 * Represents a position on a board game grid.
 */
public record Position(int row, int col) {

    /**
     * Returns a string representation of the position.
     *
     * @return a string representation of the position
     */
    @Override
    public String toString() {
        return String.format("(%d, %d)", row, col);
    }
}
