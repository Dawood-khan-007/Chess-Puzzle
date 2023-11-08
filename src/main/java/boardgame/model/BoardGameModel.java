package boardgame.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class BoardGameModel {

    public static final int BOARD_WIDTH = 2;
    public static final int BOARD_LENGTH = 3;

    private final ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_WIDTH][BOARD_LENGTH];

    /**
     * Constructs a new instance of the BoardGameModel class.
     * Initializes the game board with empty squares, and sets specific squares with initial pieces.
     */
    public BoardGameModel() {
        for (var i = 0; i < BOARD_WIDTH; i++) {
            for (var j = 0; j < BOARD_LENGTH; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<>(Square.NONE);
            }
        }
        board[0][0]= new ReadOnlyObjectWrapper<>(Square.KING);
        board[0][1]= new ReadOnlyObjectWrapper<>(Square.BISHOP);
        board[0][2]= new ReadOnlyObjectWrapper<>(Square.BISHOP);
        board[1][0]= new ReadOnlyObjectWrapper<>(Square.ROOK);
        board[1][1]= new ReadOnlyObjectWrapper<>(Square.ROOK);
        board[1][2]= new ReadOnlyObjectWrapper<>(Square.NONE);
    }

    /**
     * Returns the read-only property of the square at the specified position on the board.
     *
     * @param i the row index of the square
     * @param j the column index of the square
     * @return the read-only property of the square
     */
    public ReadOnlyObjectProperty<Square> squareProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    /**
     * Sets the square at the specified position on the board with the specified square value.
     *
     * @param p      the position of the square
     * @param square the square value to set
     */
    public void setSquare(Position p, Square square) { board[p.row()][p.col()].set(square); }

    /**
     * Returns the square value at the specified position on the board.
     *
     * @param p the position of the square
     * @return the square value at the specified position
     */
    public Square getSquare(Position p) {
        return board[p.row()][p.col()].get();
    }

    /**
     * Checks if the specified position on the board is empty.
     *
     * @param p the position to check
     * @return true if the position is empty, false otherwise
     */
    public boolean isEmpty(Position p) { return getSquare(p) == Square.NONE; }

    /**
     * Checks if the specified position is within the bounds of the board.
     *
     * @param p the position to check
     * @return true if the position is within the board bounds, false otherwise
     */
    public static boolean isOnBoard(Position p) {
        return 0 <= p.row() && p.row() < BOARD_WIDTH && 0 <= p.col() && p.col() < BOARD_LENGTH;
    }

    /**
     * Checks if a move from the "from" position to the "to" position is valid.
     *
     * @param from the starting position
     * @param to   the destination position
     * @return true if the move is valid, false otherwise
     */
    public boolean canMove(Position from, Position to) {
        if (getSquare(from) == Square.KING) {
            return isOnBoard(from) && isOnBoard(to) && !isEmpty(from) && isEmpty(to) && isKingMove(from, to);
        } else if (getSquare(from) == Square.BISHOP) {
            return isOnBoard(from) && isOnBoard(to) && !isEmpty(from) && isEmpty(to) && isBishopMove(from, to);
        }
        return isOnBoard(from) && isOnBoard(to) && !isEmpty(from) && isEmpty(to) && isRookMove(from, to);
    }

    /**
     * Checks if a move from the "from" position to the "to" position is a valid king move.
     *
     * @param from the starting position
     * @param to   the destination position
     * @return true if the move is a valid king move, false otherwise
     */
    public static boolean isKingMove(Position from, Position to) {
        var dx = Math.abs(to.row() - from.row());
        var dy = Math.abs(to.col() - from.col());

        return dx <= 1 && dy <= 1;
    }

    /**
     * Checks if a move from the "from" position to the "to" position is a valid rook move.
     *
     * @param from the starting position
     * @param to   the destination position
     * @return true if the move is a valid rook move, false otherwise
     */
    public static boolean isRookMove(Position from, Position to) {
        int dx = Math.abs(to.row() - from.row());
        int dy = Math.abs(to.col() - from.col());

        return (dx == 0 && dy != 0) || (dx != 0 && dy == 0);
    }

    /**
     * Checks if a move from the "from" position to the "to" position is a valid bishop move.
     *
     * @param from the starting position
     * @param to   the destination position
     * @return true if the move is a valid bishop move, false otherwise
     */
    public static boolean isBishopMove(Position from, Position to) {
        int dx = Math.abs(to.row() - from.row());
        int dy = Math.abs(to.col() - from.col());

        return dx == dy;
    }

    /**
     * Moves a piece from the "from" position to the "to" position on the board.
     *
     * @param from the starting position
     * @param to   the destination position
     */
    public void move(Position from, Position to) {
        setSquare(to, getSquare(from));
        setSquare(from, Square.NONE);
    }

    /**
     * Checks if the specified position is a valid source position (contains a non-empty square).
     *
     * @param position the position to check
     * @return true if the position is a valid source position, false otherwise
     */
    public boolean isValidSourcePosition(Position position) {
        Square square = getSquare(position);
        return square != Square.NONE;
    }

    /**
     * Finds an empty position on the board.
     *
     * @return an empty position on the board, or null if no empty position is found
     */
    public Position findEmptyPosition() {
        for (int row = 0; row < BOARD_WIDTH; row++) {
            for (int col = 0; col < BOARD_LENGTH; col++) {
                Position position = new Position(row, col);
                if (isEmpty(position)) {
                    return position;
                }
            }
        }
        return null;
    }

    /**
     * Returns a string representation of the board.
     *
     * @return a string representation of the board
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (var i = 0; i < BOARD_WIDTH; i++) {
            for (var j = 0; j < BOARD_LENGTH; j++) {
                sb.append(board[i][j].get().ordinal()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Entry point for testing the BoardGameModel class.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        var model = new BoardGameModel();
        System.out.println(model);
    }
}
