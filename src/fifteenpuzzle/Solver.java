
package fifteenpuzzle;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.sql.SQLOutput;
import java.util.*;
import java.util.PriorityQueue;


public class Solver {// the solver will input a board and result in movements
	public static int SIZE; //the dimension of the board
	public static int[][] goal;

	public static void main(String[] args) throws BadBoardException, IOException {

		long startTime = System.currentTimeMillis();

		System.out.println("number of arguments: " + args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}

		if (args.length < 2) {
			System.out.println("File names are not specified");
			System.out.println("usage: java " + MethodHandles.lookup().lookupClass().getName() + " input_file output_file");
			return;
		}


		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		SIZE = (int) br.read() -'0';

		String a = br.readLine();
		System.out.println(SIZE);

		int[][] board = new int[SIZE][SIZE];
		int c1, c2, s;

		//convert the board into 2D array
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

		System.out.println("INITIAL board : " + Arrays.deepToString(board));


		goal = new int[SIZE][SIZE];
		int index = 1;

		//creating the goal board
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
		resultList.pop();

		System.out.println("\nSOLUTION : ");
		while (!resultList.isEmpty()) {
			System.out.println(resultList.pop());
		}


		//calculating run time
		long endTime = System.currentTimeMillis();
		long runtime = endTime - startTime;
		if ( runtime > 30000) {
			System.out.println("run time exceeed");
			return ;
		}
		System.out.println("Runtime: " + runtime + "ms");
	}



	public static Vertex queueContains(PriorityQueue<Vertex> queue, Vertex neighbor) {
		for (Vertex i : queue) {
			if (neighbor.equals(i)) {
				return i;
			}
		}
		return null;
	}

	public static Vertex solve(int[][] start) {
		Vertex startState = new Vertex(start);
		startState.setHeuristic(startState.getHeuristic(goal));
		startState.setF(startState.getF());
		Vertex result;
		PriorityQueue<Vertex> q = new PriorityQueue<>();
		q.add(startState);
		Vertex goalVertex = new Vertex(goal);

		HashMap<Long, Integer> closed = new HashMap<>();

		while (!q.isEmpty()) {
			Vertex curr = q.remove();
			ArrayList<Vertex> neighbors = curr.generateChild();

			for (Vertex neighbor:neighbors) {

				neighbor.setHeuristic(neighbor.getHeuristic(goal));
				neighbor.setF(neighbor.getF());

				if (neighbor.getHashCode() == goalVertex.getHashCode()) {
					neighbor.setParent(curr);
					result = neighbor;
					return result; // if the solution came out then return
				}
				else {
					Vertex inQueue = queueContains(q,neighbor);
					if (inQueue != null) {//
						if (inQueue.getF() > neighbor.getF()) {
							q.remove(inQueue);
							neighbor.setParent(curr);
							q.add(neighbor);

						}
					}
					else if (closed.containsKey(neighbor.getHashCode())) {
						if (closed.get(neighbor.getHashCode()) > neighbor.getF()) {
							closed.put(neighbor.getHashCode(), neighbor.getF());
							q.add(neighbor);
							neighbor.setParent(curr);
						}
					}
					else {
						q.add(neighbor);
						neighbor.setParent(curr);
					}
				}
			}
			closed.put(curr.getHashCode(), curr.getF());

		}
		return null;
	}
}