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
		br.readLine();
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
		if (runtime > 30000) {
			System.out.println("run time exceeed");
			return;
		}
		System.out.println("Runtime: " + runtime + "ms");
	}



	public static Vertex solve(int[][] start) {
		Vertex startState = new Vertex(start);
		HashMap<Integer,Vertex> closed = new HashMap<>();
		HashSet<Integer> openStates = new HashSet<>();
		PriorityQueue<Vertex> q = new PriorityQueue<>();
		q.add(startState);
		Vertex goalVertex = new Vertex(goal);
		openStates.add(startState.getHashCode());

		while (!q.isEmpty()) {
			Vertex node = q.remove();
			System.out.println(node.getF());
			openStates.remove(node.getHashCode());

			for (Vertex neighbor : node.generateChild()) {
				if (neighbor.getHashCode() == goalVertex.getHashCode()) {
					neighbor.setParent(node);
					return neighbor;
				}

				Vertex closedNeighbor = null;
				Vertex openNeighbor = null;
				if (openStates.contains(neighbor.getHashCode())) {
					openNeighbor = q.stream().filter(n -> n.equals(neighbor)).findFirst().get();
					if (openNeighbor.getDistanceFromStart() > neighbor.getDistanceFromStart()) {
						q.remove(openNeighbor);
						neighbor.setParent(node);
						q.add(neighbor);
					}
				}
				else {
					int neighborCode = neighbor.getHashCode();
					if (closed.containsKey(neighborCode)) {
						//Check if the next state is in the queue
						closedNeighbor = closed.get(neighbor.getHashCode());
						if(closedNeighbor.getDistanceFromStart() > neighbor.getDistanceFromStart()) {
							closed.put(neighborCode, neighbor);
						}
					}

				}
				if (openNeighbor == null && closedNeighbor == null) {
					q.add(neighbor);
					neighbor.setParent(node);
					openStates.add(neighbor.getHashCode());
				}

			}
			closed.put(node.getHashCode(), node); // this line is moving node to the closed set
		}
		return null;
	}
}