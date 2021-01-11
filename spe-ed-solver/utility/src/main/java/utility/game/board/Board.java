package utility.game.board;

import java.util.Arrays;
import java.util.stream.Collectors;

import utility.geometry.Point2i;

/**
 * A representation of a Game-Grid in a spe-ed Game.
 */
public class Board<CellType extends IBoardCell<?>> {

	private final CellType[][] cells;
	private final int height;
	private final int width;

	public Board(CellType[][] cells) {
		this.cells = cells;
		this.height = cells.length;
		this.width = cells[0].length;
	}

	/**
	 * Returns the {@link IBoardCell Cell} at a given position on the {@link Board}.
	 * 
	 * @param position the {@link Point2i} of the {@link IBoardCell Cell}
	 * @return the {@link IBoardCell Cell} or null if the position is not on the
	 *         {@link Board}
	 */
	public CellType getBoardCellAt(Point2i position) {
		if (isOnBoard(position))
			return cells[position.getY()][position.getX()];
		else
			return null;
	}

	/**
	 * Determines if a {@link Point2i position} is on the Board.
	 * 
	 * @param position the {@link Point2i position} to be checked
	 * @return true, if the {@link Point2i position} is on the {@link Board}
	 */
	public boolean isOnBoard(Point2i position) {
		return 0 <= position.getX() && position.getX() < this.width && 0 <= position.getY()
				&& position.getY() < this.height;
	}

	/**
	 * @return the width of the {@link Board}
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height of the {@link Board}
	 */
	public int getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return Arrays.stream(cells).map(Arrays::toString).collect(Collectors.joining(System.lineSeparator()));
	}

}
