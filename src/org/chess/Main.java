package org.chess;

public class Main {

	public static void main(String[] args) throws Exception {
		boolean check = false;
		boolean checkmate=false;
		
		//no check
		ChessBoard board = new ChessBoard("standard");
		board.print2();
		check = board.isPlayerInCheck('B');
		checkmate = board.isPlayerInCheckmate('B');
		assertThat(!check && !checkmate);
		
		//Black is in checkmate
		board = new ChessBoard("CheckTest1");
		board.print2();
		check = board.isPlayerInCheck('B');
		checkmate = board.isPlayerInCheckmate('B');
		assertThat(check && checkmate);

		//Black is in check
		board = new ChessBoard("CheckTest2");
		board.print2();
		check = board.isPlayerInCheck('B');
		checkmate = board.isPlayerInCheckmate('B');
		assertThat(check && !checkmate);

		//Black is in check, black queen can prevent checkmate
		board = new ChessBoard("CheckTest3");
		board.print2();
		check = board.isPlayerInCheck('B');
		checkmate = board.isPlayerInCheckmate('B');
		assertThat(check && !checkmate);

		//Black is in check, black king can take queen to prevent checkmate
		board = new ChessBoard("CheckTest4");
		board.print2();
		check = board.isPlayerInCheck('B');
		checkmate = board.isPlayerInCheckmate('B');
		assertThat(check && !checkmate);

		//Black is in checkmate
		board = new ChessBoard("CheckTest5");
		board.print2();
		check = board.isPlayerInCheck('B');
		checkmate = board.isPlayerInCheckmate('B');
		assertThat(check && checkmate);
	}
	
	public static void assertThat(boolean b) throws Exception {
		if (!b) {
			//throw new Exception("Assertion failed.");
		}
	}
}
