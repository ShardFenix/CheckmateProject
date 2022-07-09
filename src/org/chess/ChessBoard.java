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
	List<ChessPiece> attackingPieces = new LinkedList<ChessPiece>();

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
						attackingPieces.add(piece);
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
                if (piece.getColor() == c && piece.isEssential())
                {
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
		ChessPiece kingPiece = playerKingPiece(c);
		List<ChessPiece> alliedPieces = getAlliedPieces(c);
		List<Point> kingMovableSpaces = getKingMovableSpaces(c);
		List<Point> attackingSpaces = getAttackingSpaces(c);

		if (kingPiece != null && !attackingPieces.isEmpty())
		{		
			for (ChessPiece alliedPiece : alliedPieces)
			{
				 for(Point attackingSpace : attackingSpaces)
				 {
					 if (canMove(alliedPiece, attackingSpace.x, attackingSpace.y) == null) 
					 {
						 return false;
					 }
					 
					 for (ChessPiece attackingPiece : attackingPieces)
						{
						 	if(canMove(alliedPiece, attackingPiece.x, attackingPiece.y) == null)
						 	{
						 		return false;
						 	}
							if (canMove(kingPiece, attackingPiece.x, attackingPiece.y) == null)
							{
								return false;
							}
						}
				 }
			}
			
			if (!kingMovableSpaces.isEmpty())
			{
				return false;
			}
		}

		return true;
	}
	
	
	public List<ChessPiece> getAlliedPieces(char c)
	{
		List<ChessPiece> alliedPieces = new LinkedList<ChessPiece>();
		ChessPiece kingPiece = playerKingPiece(c);
		for	(int x = 0; x<= 7; x++)
		{
			for (int y = 0; y <= 7; y++)
			{
				ChessPiece piece = get(x,y);
				if (piece == null)
				{
					continue;
				}
				if (piece.getColor() == c && piece != kingPiece)
				{
					alliedPieces.add(piece);
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
	
	public List<Point> getAttackingSpaces(char c)
	{
		List<Point> attackingSpaces = new LinkedList<Point>();
		for (ChessPiece attackingPiece : attackingPieces)
		{
			Point kingPos = playerKingPos(c);
			int dx, dy, p, x, y;  
		    dx = attackingPiece.x - kingPos.x;  
		    dy = attackingPiece.y - kingPos.y;  
		    x = kingPos.x;  
		    y = kingPos.y;  
		    p = 2 * dy - dx;  
		    while(x < attackingPiece.x)  
		    {  
		    	Point space = new Point();
		        if(p >= 0)  
		        {  
		        	space.x = x;
		        	space.y = y;
		        	attackingSpaces.add(space);  
		            y = y + 1;  
		            p = p + 2 * dy - 2 * dx;  
		        }  
		        else  
		        {  

		        	space.x = x;
		        	space.y = y;
		        	attackingSpaces.add(space);  
		            p = p + 2 * dy;}  
		            x = x + 1;  
		        }  
		}

		return attackingSpaces;
	}
	
}
