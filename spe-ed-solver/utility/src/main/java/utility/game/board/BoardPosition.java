package utility.game.board;

/**
 * immutable Position at the {@link Board}
 */
public final class BoardPosition {

    private final int x;
    private final int y;

    /**
     * Initilizes a immutable Point for the game Board
     */
    public BoardPosition(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    /**
     * Translates this BoardPosition to a new Boardposition
     * 
     * @param deltaX x-direction change
     * @param deltaY y-direction change
     * @return a new, translated BoardPosition
     */
    public BoardPosition translate(final int deltaX, final int deltaY) {
        return new BoardPosition(x + deltaX, y + deltaY);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof BoardPosition)) {
            return false;
        }
        BoardPosition boardPosition = (BoardPosition) o;
        return x == boardPosition.x && y == boardPosition.y;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }

}