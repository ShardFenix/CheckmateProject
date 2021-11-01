package org.chess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


public abstract class AbstractChessBoard {

	private int whitePlayerId=-1;
	private int blackPlayerId=-1;
	private boolean whitesTurn = true;
	private String name;
	
	private ChessPiece[][] board;


	/**
	 * For a player 'W' or 'B', return whether that player has an essential piece
	 * that's in check.
	 *
	 * @param c
	 * @return
	 */
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
	public boolean isPlayerInCheckmate(char c) {
		return false;
	}
	
	
	
	public AbstractChessBoard(String fileName) throws IOException {
		name=fileName;
		File f = new File("boards/" + fileName + ".txt");
		String fileContents = new String(Files.readAllBytes(f.toPath()));
		fileContents = fileContents.replaceAll("(\\r|\\n)+", "");
		String[] items = fileContents.split(",");

		board = new ChessPiece[8][8];
		for (int i = 0; i < 64; i++) {
			if (i >= items.length) {
				board[7 - (i / 8)][i % 8] = null;
			} else if (items[i].isEmpty()) {
				board[7 - (i / 8)][i % 8] = null;
			} else {
				board[7 - (i / 8)][i % 8] = new ChessPiece(items[i].substring(1));
				board[7 - (i / 8)][i % 8].setColor(items[i].charAt(0));
				board[7 - (i / 8)][i % 8].x = i % 8;
				board[7 - (i / 8)][i % 8].y = 7 - i / 8;
			}
		}
	}

	public AbstractChessBoard(AbstractChessBoard other) {
		whitesTurn = other.whitesTurn;
		board = new ChessPiece[8][8];
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				board[y][x] = other.get(x, y);
			}
		}
	}

	public ChessPiece get(int x, int y) {
		return board[y][x];
	}

	protected String canMoveWithMovementRule(ChessPiece p, int x, int y) {
		List<String> rules = p.movementRules;
		if (!p.hasMoved() && !p.firstMoveRules.isEmpty()) {
			rules = p.firstMoveRules;
		}
		nextMovementRule: for (String movementRule : rules) {
			int max = 7;
			String maxDistance = p.getVar("maxDistance");
			if (!maxDistance.isEmpty()) {
				String varName = TSUtils.substringByGroup(movementRule, "\\$(\\w+)", 1);
				maxDistance = maxDistance.replaceAll("\\$" + varName, p.getVar(varName));
				max = Integer.parseInt(maxDistance);
			}
			int currentX = p.x;
			int currentY = p.y;
			char previous = '5';
			boolean collidedWithEnemyPiece = false;
			// loop through each individual move
			for (int i = 0; i < movementRule.length(); i++) {
				if (max == 0) {
					return "Can't move to " + x + "," + y + " because it is too far away.";
				}
				max--;
				char direction = movementRule.charAt(i);
				if (collidedWithEnemyPiece) {
					if (direction == '+' && currentX == x && currentY == y) {
						return null;
					}
					continue nextMovementRule;
				}
				boolean canStopEarly = false;
				if (direction == '+') {
					if (currentX == x && currentY == y) {
						return null;
					}
					canStopEarly = true;
					direction = previous;
					i--;
				}
				previous = direction;
				int simulatedX = currentX, simulatedY = currentY;
				switch (direction) {
				case '1':
					simulatedX = currentX - 1;
					simulatedY = currentY - 1;
					break;
				case '2':
					simulatedY = currentY - 1;
					break;
				case '3':
					simulatedX = currentX + 1;
					simulatedY = currentY - 1;
					break;
				case '4':
					simulatedX = currentX - 1;
					break;
				case '6':
					simulatedX = currentX + 1;
					break;
				case '7':
					simulatedX = currentX - 1;
					simulatedY = currentY + 1;
					break;
				case '8':
					simulatedY = currentY + 1;
					break;
				case '9':
					simulatedX = currentX + 1;
					simulatedY = currentY + 1;
					break;
				default:// do nothing
				}
				boolean canMoveToSpace = isSpaceMovable(p, simulatedX, simulatedY);
				if (!canMoveToSpace) {
					continue nextMovementRule;
				}
				// if there's a piece on this space
				if (get(simulatedX, simulatedY) != null && !p.jumps()) {
					collidedWithEnemyPiece = true;
				}
				currentX = simulatedX;
				currentY = simulatedY;
				if (currentX == x && currentY == y && canStopEarly) {
					return null;
				}
			} // end for each directional move
			if (currentX == x && currentY == y) {
				return null;
			}
		}
		return "Can't move to " + x + "," + y + " because there are no legal movement paths.";
	}

	public String canMoveWithCaptureRule(ChessPiece p, int x, int y) {
		nextMovementRule: for (String movementRule : p.captureRules) {
			int currentX = p.x;
			int currentY = p.y;
			char previous = '5';
			boolean collidedWithEnemyPiece = false;
			// loop through each individual move
			for (int i = 0; i < movementRule.length(); i++) {
				char direction = movementRule.charAt(i);
				if (collidedWithEnemyPiece) {
					if (direction == '+' && currentX == x && currentY == y) {
						return null;
					}
					continue nextMovementRule;
				}
				boolean canStopEarly = false;
				if (direction == '+') {
					if (currentX == x && currentY == y) {
						return null;
					}
					canStopEarly = true;
					direction = previous;
					i--;
				}
				previous = direction;
				int simulatedX = currentX, simulatedY = currentY;
				switch (direction) {
				case '1':
					simulatedX = currentX - 1;
					simulatedY = currentY - 1;
					break;
				case '2':
					simulatedY = currentY - 1;
					break;
				case '3':
					simulatedX = currentX + 1;
					simulatedY = currentY - 1;
					break;
				case '4':
					simulatedX = currentX - 1;
					break;
				case '6':
					simulatedX = currentX + 1;
					break;
				case '7':
					simulatedX = currentX - 1;
					simulatedY = currentY + 1;
					break;
				case '8':
					simulatedY = currentY + 1;
					break;
				case '9':
					simulatedX = currentX + 1;
					simulatedY = currentY + 1;
					break;
				default:// do nothing
				}
				boolean canMoveToSpace = isSpaceMovable(p, simulatedX, simulatedY);
				if (!canMoveToSpace) {
					continue nextMovementRule;
				}
				// if there's a piece on this space
				if (get(simulatedX, simulatedY) != null && !p.jumps()) {
					collidedWithEnemyPiece = true;
				}
				currentX = simulatedX;
				currentY = simulatedY;
				if (currentX == x && currentY == y && canStopEarly) {
					return null;
				}
			} // end for each directional move
			if (currentX == x && currentY == y) {
				return null;
			}
		}
		return "Couldn't move to " + x + "," + y + " because there are no legal capture moves.";
	}

	/**
	 * Returns the player's essential unit if he has only one. If the player has
	 * more than one essential unit, return null
	 *
	 * @param color
	 * @return
	 */
	public ChessPiece getEssential(char color) {
		ChessPiece essential = null;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				ChessPiece piece = get(i, j);
				if (piece == null) {
					continue;
				}
				if (piece.getColor() != color) {
					continue;
				}
				if (piece.getVar("essential").equals("true")) {
					if (essential != null) {
						return null;
					}
					essential = piece;
				}
			}
		}
		return essential;
	}

	/**
	 * Determines whether the selected piece can move to [x,y] from where it
	 * currently is.
	 *
	 * @return
	 */
	public String canMove(ChessPiece p, int x, int y) {
		// allied unit on that space
		if (get(x, y) != null && get(x, y).getColor() == p.getColor()) {
			return "Can't move to " + x + "," + y + " because it is occupied.";
		}
		// if we only have one essential unit
		ChessPiece essential = getEssential(p.getColor());

		if (essential != null) {
			AbstractChessBoard simulatedBoard = new ChessBoard(this);
			simulatedBoard.board[y][x] = p;
			if (p == essential) {
				simulatedBoard.board[p.y][p.x] = null;
			}
			if (simulatedBoard.isPlayerInCheck(p.getColor())) {
				return "Can't move to " + x + "," + y + " because you would be in check.";
			}
		}
		// simulate if the piece were actually there
		// return false if our last essential unit would be in check.

		// if the piece doesn't have special capture rules
		if (p.getVar("captureRule").isEmpty()) {
			return canMoveWithMovementRule(p, x, y);
		}
		// enemy on space and unit has capture rules
		if (get(x, y) != null && get(x, y).getColor() != p.getColor()) {
			return canMoveWithCaptureRule(p, x, y);
		}
		return canMoveWithMovementRule(p, x, y);
	}

	/**
	 * Return true if X,Y is within the board, AND either the unit can jump, or the
	 * space is empty
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isSpaceMovable(ChessPiece p, int x, int y) {
		if (p == null) {
			return false;
		}
		if (x < 0 || x > 7 || y < 0 || y > 7) {
			return false;
		}
		if (get(x, y) == null) {
			return true;
		}
		if (p.getVar("jumps").equals("true")) {
			return true;
		}
		// see if it's an enemy piece
		if (get(x, y).getColor() != p.getColor()) {
			return true;
		}
		return false;
	}

	public int getWhitePlayerId() {
		return whitePlayerId;
	}

	public void setWhitePlayerId(int whitePlayerId) {
		this.whitePlayerId = whitePlayerId;
	}

	public int getBlackPlayerId() {
		return blackPlayerId;
	}

	public void setBlackPlayerId(int blackPlayerId) {
		this.blackPlayerId = blackPlayerId;
	}

	public boolean isWhitesTurn() {
		return whitesTurn;
	}

	public void setWhitesTurn(boolean whitesTurn) {
		this.whitesTurn = whitesTurn;
	}

	public ChessPiece[][] getBoard() {
		return board;
	}

	public void setBoard(ChessPiece[][] board) {
		this.board = board;
	}

	public void print() {
		for (int y=7;y>=0;y--) {
			for (int x=0;x<8;x++) {
				ChessPiece piece = get(x, y);
				if (piece==null) {
					System.out.print("       ");
				} else {
					String name = piece.getColor()+piece.getVar("name");
					while (name.length()<7) {name+=" ";}
					System.out.print(name);
				}
				System.out.print(" | ");
			}
			System.out.println();
		}
		System.out.println("=================================");
	}

	public void print2() {
		System.out.println("\n"+name+"\n-----------------------------------------");
		for (int y=7;y>=0;y--) {
			System.out.print("| ");
			for (int x=0;x<8;x++) {
				ChessPiece piece = get(x, y);
				if (piece==null) {
					System.out.print("  ");
				} else {
					String name = piece.getColor()+piece.getVar("name");
					if (name.substring(1).equals("Knight")) {
						name=name.substring(0,1).toLowerCase()+"N";
					}
					name=name.substring(0,1).toLowerCase()+name.charAt(1);
					System.out.print(name);
				}
				System.out.print(" | ");
			}
			System.out.println("\n-----------------------------------------");
		}
		System.out.println();
	}
	
}
