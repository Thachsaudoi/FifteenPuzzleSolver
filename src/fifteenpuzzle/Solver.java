
package fifteenpuzzle;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.sql.SQLOutput;
import java.util.*;
import java.util.PriorityQueue;


public class Solver {// the solver will input a board and result in movements
	// piorityQueue;
	public static int SIZE; // the size of the board
	public static int[][] goal;

	public static boolean solvable(int[][] arr2D)
	{
		int[] puzzle = new int[arr2D.length * arr2D[0].length];
		int index = 0;
		for (int i = 0; i < arr2D.length; i++) {
			for (int j = 0; j < arr2D[0].length; j++) {
				puzzle[index++] = arr2D[i][j];
			}
		}
		int parity = 0;
		int gridWidth = (int) Math.sqrt(puzzle.length);
		int row = 0; // the current row we are on
		int blankRow = 0; // the row with the blank tile

		for (int i = 0; i < puzzle.length; i++)
		{
			if (i % gridWidth == 0) { // advance to next row
				row++;
			}
			if (puzzle[i] == 0) { // the blank tile
				blankRow = row; // save the row on which encountered
				continue;
			}
			for (int j = i + 1; j < puzzle.length; j++)
			{
				if (puzzle[i] > puzzle[j] && puzzle[j] != 0)
				{
					parity++;
				}
			}
		}

		if (gridWidth % 2 == 0) { // even grid
			if (blankRow % 2 == 0) { // blank on odd row; counting from bottom
				return parity % 2 == 0;
			} else { // blank on even row; counting from bottom
				return parity % 2 != 0;
			}
		} else { // odd grid
			return parity % 2 == 0;
		}
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
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		SIZE = (int) br.read() -'0';

		String a = br.readLine();
		System.out.println(SIZE);

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
		br.close();

		System.out.println(Arrays.deepToString(board));

		goal = new int[SIZE][SIZE];
		int index = 1;

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (i == SIZE - 1 && j == SIZE - 1) {
					goal[i][j] = 0;
					break;
				}
				goal[i][j] = index;
				index++;
			}
		}


		Vertex b = solve(board);
		Stack<String> resultList = new Stack<>();
		while (b != null) {
			resultList.add(b.getMove());
			b = b.getParent();
		}

		System.out.println("solution in main : ");
		while (!resultList.isEmpty()) {
			System.out.println(resultList.pop());
		}
	}

	public static boolean queueContains(PriorityQueue<Vertex> queue, Vertex neighbor) {
		for (Vertex i : queue) {
			if (neighbor.equals(i)) {
				return true;
			}
		}
		return false;
	}

	public static Vertex solve(int[][] start) {
		Vertex startState = new Vertex(start);
		startState.setHeuristic(startState.getHeuristic(goal));
		startState.setF(startState.getF());
		Vertex result;
//		HashSet<Integer> closed = new HashSet<>();
		PriorityQueue<Vertex> q = new PriorityQueue<>();
		q.add(startState);
		Vertex goalVertex = new Vertex(goal);
//		while (!q.isEmpty()) {
//			//TESTING: printing queue
//			System.out.println("Queue:");
//			for (Vertex i : q) {
//				i.setHeuristic(i.getHeuristic(goal));
//				i.setF(i.getF());
//				System.out.println(Arrays.deepToString(i.getBoard()));
//				System.out.println("Move: " + i.getMove());
//				System.out.println("f value : " + i.getF());
//				System.out.println("cost: " + i.getDistanceFromStart());
//			}
//			System.out.println("End queue");
//			Vertex node = q.remove();
//			if (solvable(node.getBoard())){
//				if (node.getHashCode() == goalVertex.getHashCode()) {
//					result = node;
//					return result;
//				}
//
//				closed.add(node.getHashCode());
//
//				for (Vertex neighbor : node.generateChild()) {
//					if (closed.contains(neighbor.getHashCode())) {
//						continue;
//					}
//					neighbor.setHeuristic(neighbor.getHeuristic(goal));
//					neighbor.setF(neighbor.getF());
//
//					if (!closed.contains(neighbor.getHashCode())) {
//						if (queueContains(q, neighbor)) {
//							Vertex openNeighbor = q.stream().filter(n -> n.equals(neighbor)).findFirst().get();
//							if (openNeighbor.getDistanceFromStart() > neighbor.getDistanceFromStart()) {
//								q.remove(openNeighbor);
//								neighbor.setParent(node);
//								q.add(neighbor);
//							}
//						} else {
//							neighbor.setParent(node);
//							q.add(neighbor);
//						}
//					}
//				}
//			}
//			else{
//				System.out.println("unsolvable duma");
//				System.out.println(Arrays.deepToString(node.getBoard()));
//				System.out.println(node.getMove());
//				System.out.println("----------------------------------------");
//			}
//		}
//		return null;
		HashMap<Integer, Integer> closed = new HashMap<>();

		while (!q.isEmpty()) {
			Vertex curr = q.remove();
			if (solvable(curr.getBoard())) {
				System.out.println("Next move: " + curr.getMove());
				ArrayList<Vertex> neighbors = curr.generateChild();
//				System.out.println("---------------------------------------");
//				for (Vertex u : neighbors) {
//					System.out.print(u.getMove() + ": ");
//					System.out.println(Arrays.deepToString(u.getBoard()));
//					u.setHeuristic(u.getHeuristic(goal));
//					u.setF(u.getF());
//					System.out.println("f value : " + u.getF());
//				}
//				System.out.println("---------------------------------------");
				for (Vertex u:neighbors) {
					u.setHeuristic(u.getHeuristic(goal));
					u.setF(u.getF());
					if (u.getHashCode() == goalVertex.getHashCode()) {
						u.setParent(curr);
						result = u;
						return result; //Maybe change later
					} else {
						if (q.contains(u)) {
							for (Vertex i:q) {
								if (i.getHashCode() == u.getHashCode() && i.getF() < u.getF()) {
									i.setF(u.getF());
									u.setParent(curr);
								}
							}
						} else if (closed.containsKey(u.getHashCode())) {

							if (closed.get(u.getHashCode()) > u.getF()) {
								closed.put(u.getHashCode(), u.getF());
								q.add(u);
								u.setParent(curr);
							}
						} else {
							q.add(u);
							u.setParent(curr);
						}
					}
				}
				closed.put(curr.getHashCode(), curr.getF());
			} else {
				System.out.println("DUMA UNSOLVABLE");
				System.out.println(Arrays.deepToString(curr.getBoard()));
				System.out.println(curr.getMove());
			}

		}
		return null;
	}
}