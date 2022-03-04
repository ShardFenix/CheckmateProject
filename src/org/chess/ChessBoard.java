package org.chess;

import java.awt.Point;
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
		Point kingPos = playerKingPos(c);
		if (kingPos != null)
		{
			Point chessPiecePos = new Point();
			ChessPiece piece = null;
			for	(int x = 0; x<= 7; x++)
			{
				for (int y = 0; y <= 7; y++)
				{
					piece = get(x,y);
					chessPiecePos.x = x;
					chessPiecePos.y = y;
					if (piece == null)
					{
						continue;
					}
					if (piece.getColor() == c)
					{
						continue;
					}
					if (canMove(piece, kingPos.x, kingPos.y) == null)
					{
						return true;
					}
					/*
					if (canMoveWithCaptureRule(piece, kingPos.x, kingPos.y) == null)
					{
						return true;
					}*/
				}
			}
		}
		return false;
	}
	
	public Point playerKingPos(char c) {
        Point chessPieceXY = new Point();
        ChessPiece piece = null;
        for (int x = 0; x <= 7; x++) 
        {
            for (int y = 0; y <= 7; y++) 
            {
                piece = get(x,y);
                if (piece == null) {
                    continue;
                }
                if (piece.getColor() != c) {
                    continue;
                }
                if (piece.isEssential()){
                    chessPieceXY.x = x;
                    chessPieceXY.y = y;
                    return chessPieceXY;
                }
            }
        }
        return chessPieceXY;
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
		if (isPlayerInCheck(c))
		{
			return true;
		}
		return false;
	}
}
