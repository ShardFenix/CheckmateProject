package org.chess;

import java.io.IOException;

/**
 * This is the file you can edit. You will need to populate isPlayerInCheck
 * and isPlayerInCheckmate.
 * You are also allowed to add any other new methods you need.
 */
public class ChessBoard extends AbstractChessBoard {


	public ChessBoard(String fileName) throws IOException {
		super(fileName);
	}

	public ChessBoard(AbstractChessBoard other) {
		super(other);
	}
	
	/**
	 * For a player 'W' or 'B', return whether that player has an essential piece
	 * that's in check.
	 *
	 * @param c
	 * @return
	 */
	@Override
	public boolean isPlayerInCheck(char c) {
		return false;
	}
	
	/**
	 * For a player 'W' or 'B', return whether that player has an essential piece
	 * that's in checkmate.
	 *
	 * @param c
	 * @return
	 */
	@Override
	public boolean isPlayerInCheckmate(char c) {
		return false;
	}
}
