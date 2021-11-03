package org.chess;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class ChessPiece {

	private HashMap<String, String> vars = new HashMap<>();
	LinkedList<String> movementRules = new LinkedList<>();
	LinkedList<String> captureRules = new LinkedList<>();
	LinkedList<String> firstMoveRules = new LinkedList<>();
	private char color = 'W';
	private boolean pristine = true;

	public int x, y;

	public ChessPiece() {
	}

	public ChessPiece(String name) {
		File f = new File("pieces/"+name+".txt");
		try {
			LinkedList<String> lines = new LinkedList<>();
			Scanner scan = new Scanner(f);
			while (scan.hasNextLine()) {
				lines.add(scan.nextLine());
			}
			for (String line : lines) {
				if (!line.isEmpty()) {
					int indexOfEq = line.indexOf('=');
					String key = line.substring(0, indexOfEq);
					String value = line.substring(indexOfEq + 1);
					vars.put(key, value);
					if (key.equals("movementRule")) {
						String[] legalMoves = value.split(",");
						for (String move : legalMoves) {
							movementRules.add(move);
						}
					}
					if (key.equals("captureRule")) {
						String[] legalMoves = value.split(",");
						for (String move : legalMoves) {
							captureRules.add(move);
						}
					}
					if (key.equals("firstMove")) {
						String[] legalMoves = value.split(",");
						for (String move : legalMoves) {
							firstMoveRules.add(move);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setColor(char c) {
		if (color != c) {
			// flip this piece's movement rules upside down
			LinkedList<String> newRules = new LinkedList<String>();
			for (String rule : movementRules) {
				newRules.add(flipRule(rule));
			}
			movementRules = newRules;

			LinkedList<String> newCapRules = new LinkedList<String>();
			for (String rule : captureRules) {
				newCapRules.add(flipRule(rule));
			}
			captureRules = newCapRules;

			LinkedList<String> newFirstRules = new LinkedList<String>();
			for (String rule : firstMoveRules) {
				newFirstRules.add(flipRule(rule));
			}
			firstMoveRules = newFirstRules;
		}
		color = c;
	}

	public boolean hasMoved() {
		return !pristine;
	}

	public char getColor() {
		return color;
	}

	public boolean isWhite() {
		return color == 'W';
	}

	public boolean isBlack() {
		return color == 'B';
	}

	public void printVars() {
		for (String key : vars.keySet()) {
			System.out.println(key + "\t=\t" + vars.get(key));
		}
	}

	/**
	 * Given a rule of the form /[12346789]+?/, flip all the directions of each
	 * move.
	 *
	 * @param rule
	 * @return
	 */
	public String flipRule(String rule) {
		StringBuilder sb = new StringBuilder();
		for (char c : rule.toCharArray()) {
			switch (c) {
			case '1':
				sb.append('7');
				break;
			case '2':
				sb.append('8');
				break;
			case '3':
				sb.append('9');
				break;
			case '7':
				sb.append('1');
				break;
			case '8':
				sb.append('2');
				break;
			case '9':
				sb.append('3');
				break;
			default:
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public boolean jumps() {
		return "true".equals(getVar("jumps"));
	}

	public String getVar(String key) {
		String returnValue = vars.get(key);
		if (returnValue == null) {
			return "";
		}
		return vars.get(key);
	}
	
	public boolean isEssential(){
		return "true".equalsIgnoreCase(getVar("essential"));
	}

	void setIsPristine(boolean b) {
		pristine = b;
	}

}
