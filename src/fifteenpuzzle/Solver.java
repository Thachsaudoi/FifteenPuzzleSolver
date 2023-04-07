
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

	public static void main(String[] args) throws BadBoardException, IOException {

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
		while (b.getParent() != null) {
			resultList.add(b.getMove());
			b = b.getParent();
		}
		System.out.println("solution in main : ");
		System.out.println("solution is missing the last element");
		while (!resultList.isEmpty()) {
			System.out.println(resultList.pop());
		}

		//File input = new File(args[0]);
		// solve..
		//File output = new File(args[1]);

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
//		HashMap<Integer, Integer> closed = new HashMap<>();

		//********** THIS IS WHERE A* STARTED*********************


		HashSet<Integer> closed = new HashSet<>();
		PriorityQueue<Vertex> q = new PriorityQueue<>();
		q.add(startState);
		Vertex goalVertex = new Vertex(goal);

		int countPop = 0 ;
		while (!q.isEmpty()) {
			Vertex node = q.remove();
			countPop++;

			for (Vertex neighbor : node.generateChild()) {
				neighbor.setHeuristic(neighbor.getHeuristic(goal));
				neighbor.setF(neighbor.getF());

				if (neighbor.getHashCode() == goalVertex.getHashCode()) {
					System.out.println("final count pop: "+ countPop);
					System.out.println("count inside the hashset: "+ closed.size());
					neighbor.setParent(node);
					return neighbor;
				}

				else{
					if (queueContains(q, neighbor)) {
						// it did go inside here
						Vertex openNeighbor = q.stream().filter(n -> n.equals(neighbor)).findFirst().get();
						if (openNeighbor.getF() > neighbor.getF()) {
							System.out.println("hello world");
							// the problem is the thing never go inside here

							q.remove(openNeighbor);
							neighbor.setParent(node);
							q.add(neighbor);
//							System.out.println("open neighbor: " + openNeighbor.getMove());
//							System.out.println("open f" + openNeighbor.getF());
//							System.out.println("neighbor: " + neighbor.getMove());
//							System.out.println("neighbor f" + neighbor.getF());

						}
					}
//					else if ( closed.contains(neighbor.getHashCode())){
//						// so if the one in the closed has higher fu then update the fu then move it back to the open queue
//
//					}
					else {
						neighbor.setParent(node);
						q.add(neighbor);
					}
				}
			}
			closed.add(node.getHashCode()); // this line is moving node to the close set

		}
		return null;
	}
}
