package org.chess;

import java.awt.Point;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
	
	public ChessPiece playerKingPiece(char c) {
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
                    return piece;
                }
            }
        }
        return piece;
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
		if (!isPlayerInCheck(c))
		{
			return false;
		}
		Point kingPos = playerKingPos(c);
		ChessPiece kingPiece = playerKingPiece(c);
		List<Point> enemyPieces = getEnemyPieces(c);
		List<Point> alliedPieces = getAlliedPieces(c);
		List<Point> kingMovableSpaces = getKingMovableSpaces(c);
		List<Point> kingRuleset = getKingRuleset(c);

		if (kingPos != null && enemyPieces != null) {
			for (Point enemyPiece : enemyPieces)
			{
				if (canMove(kingPiece, enemyPiece.x, enemyPiece.y) == null)
				{
					return false;
				}
				for (Point alliedPiece : alliedPieces)
				{
					 for(Point rulesetSpace : kingRuleset)
					 {
						 if (canMove(get(alliedPiece.x,alliedPiece.y), rulesetSpace.x, rulesetSpace.y) == null) 
						 {
							 return false;
						 }
					 }
				}
			}
		}
		if (kingMovableSpaces.isEmpty())
		{
			return true;
		}

		return false;
	}
	
	public List<Point> getEnemyPieces(char c)
	{
		List<Point> enemyPieces = new LinkedList<Point>();
		for	(int x = 0; x<= 7; x++)
		{
			for (int y = 0; y <= 7; y++)
			{
				ChessPiece piece = get(x,y);
				Point chessPiecePos = new Point();
				chessPiecePos.x = x;
				chessPiecePos.y = y;
				if (piece == null)
				{
					continue;
				}
				if (piece.getColor() != c)
				{
					enemyPieces.add(chessPiecePos);
				}
			}
		}
		return enemyPieces;
	}
	
	public List<Point> getAlliedPieces(char c)
	{
		List<Point> alliedPieces = new LinkedList<Point>();
		ChessPiece kingPiece = playerKingPiece(c);
		for	(int x = 0; x<= 7; x++)
		{
			for (int y = 0; y <= 7; y++)
			{
				ChessPiece piece = get(x,y);
				Point chessPiecePos = new Point();
				chessPiecePos.x = x;
				chessPiecePos.y = y;
				if (piece == null)
				{
					continue;
				}
				if (piece.getColor() == c && piece != kingPiece)
				{
					alliedPieces.add(chessPiecePos);
				}
			}
		}
		return alliedPieces;
	}

	public List<Point> getKingMovableSpaces(char c)
	{
		Point kingPos = playerKingPos(c);
		ChessPiece piece = playerKingPiece(c);
		List<Point> movableSpaces = new LinkedList<Point>();
		if (kingPos != null && piece != null)
		{
			Point boardSpace = new Point();
			for	(int x = 0; x<= 7; x++)
			{
				for (int y = 0; y <= 7; y++)
				{
					boardSpace.x = x;
					boardSpace.y = y;
					if (canMove(piece, x, y) == null)
					{
						movableSpaces.add(boardSpace);
					}
				}
			}
		}
		return movableSpaces;
	}
	
	public List<Point> getKingRuleset(char c)
	{
		Point kingPos = playerKingPos(c);
		ChessPiece piece = playerKingPiece(c);
		List<Point> ruleset = new LinkedList<Point>();
		if (kingPos != null && piece != null)
		{
			for (int i = kingPos.x - 1; i < kingPos.x + 1; ++i) {
			    for (int j = kingPos.y - 1; j < kingPos.y + 1; ++j) {
			        Point square = new Point();
			        if (kingPos.x != i && kingPos.y != j)
			        {
			        	if (i > -1 && j < 9)
			        	{
					        square.x = i;
					        square.y = j;
					        ruleset.add(square);
			        	}
			        }
			    }
			}
		}
		return ruleset;
	}
	
}
