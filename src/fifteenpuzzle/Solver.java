package fifteenpuzzle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class Solver {// the solver will input a board and result in movements
	// piorityQueue;
	public int hashCode(){
		return 0;
	}
	public int[][] goal;
	public static int SIZE; // the size of the board

	private void checkBoard(int[][] board) throws BadBoardException {
		int[] vals = new int[SIZE * SIZE];

		// check that the board contains all number 0...15
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (board[i][j]<0 || board[i][j]>=SIZE*SIZE)
					throw new BadBoardException("found tile " + board[i][j]);
				vals[board[i][j]] += 1;
			}
		}

		for (int i = 0; i < vals.length; i++)
			if (vals[i] != 1)
				throw new BadBoardException("tile " + i +
						" appears " + vals[i] + "");

	}
	public static void main(String[] args) throws BadBoardException, IOException {
		System.out.println("number of arguments: " + args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}

		if (args.length < 2) {
			System.out.println("File names are not specified");
			System.out.println("usage: java " + MethodHandles.lookup().lookupClass().getName() + " input_file output_file");
			return;
		}

		// TODO
		BufferedReader br = new BufferedReader(new FileReader(args));
		SIZE = (int) br.read();

		int[][] board = new int[SIZE][SIZE];
		int c1, c2, s;

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				c1 = br.read();
				c2 = br.read();
				s = br.read(); // skip the space
				if (s != ' ' && s != '\n') {
					br.close();
					throw new BadBoardException("error in line " + i);
				}
				if (c1 == ' ')
					c1 = '0';
				if (c2 == ' ')
					c2 = '0';
				board[i][j] = 10 * (c1 - '0') + (c2 - '0');
			}
		}
		checkBoard(board);
		br.close();
		//File input = new File(args[0]);
		// solve...
		//File output = new File(args[1]);

	}
}
