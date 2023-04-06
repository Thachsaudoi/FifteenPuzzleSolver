
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

	public static boolean solvable(Vertex temp){
		int[][] board = temp.getBoard();
		int[] flattened = new int[board.length * board[0].length - 1]; // ignore empty cell
		int k = 0;
		int blank = 0;
		//turn 2D into 1D array
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] != 0) {
					flattened[k] = board[i][j];
					k++;
				}
			}
		}
		int inversions = 0;

		//get inversion
		for (int i = 0; i < flattened.length - 1; i++) {
			for (int j = i + 1; j < flattened.length; j++) {
				if (flattened[j] != 0 && flattened[i] != 0 && flattened[i] > flattened[j])  {
					inversions++;
				}
			}
		}

		//get the x position
		for(int i = board.length -1 ; i>= 0  ; i-- ){
			for(int j = board.length-1 ; j >= 0 ; j--)
				if (board[i][j] == 0 )
					blank = board.length - 1 ;
		}
		if (board.length % 2 == 1 )  return inversions % 2 == 0 ;
		else{
			if (blank%2 ==1){
				return inversions % 2 ==0;
			}
			return inversions % 2 ==1;
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
		//checkBoard(board);
		br.close();

		//File input = new File(args[0]);
		// solve..
		//File output = new File(args[1]);
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


//		System.out.println("Goal board:");
//		System.out.println(Arrays.deepToString(goal));
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
		HashSet<Integer> closed = new HashSet<>();
		PriorityQueue<Vertex> q = new PriorityQueue<>();
		q.add(startState);
		Vertex goalVertex = new Vertex(goal);
		System.out.println("duma con cac "+startState.getF());
		int[][] duma =  { {1,2,3},{4,5,6},{7,7,0} };
		Vertex dumaVertex = new Vertex(duma);
		System.out.println(dumaVertex.equals(goalVertex));
		int calWhile = 0;
		while (!q.isEmpty()) {
			calWhile++;
			//TESTING: printing queue
			System.out.println("Queue:");
			for (Vertex i : q) {
				i.setHeuristic(i.getHeuristic(goal));
				i.setF(i.getF());
//				System.out.println(i.getMove());
				System.out.println(Arrays.deepToString(i.getBoard()));
				System.out.println("f value : " + i.getF());
				System.out.println("cost: " + i.getDistanceFromStart());
//				System.out.println("hash: "+ i.getHashCode());
			}
			System.out.println("End queue--------");
			Vertex node = q.remove();
			if (solvable(node)){

				if (node.getHashCode() == goalVertex.getHashCode()) {
					result = node;
//				System.out.println("we have result");
					return result;
				}

				closed.add(node.getHashCode());

				System.out.println("-------------------------------");
				for (Vertex neighbor : node.generateChild()) {
					if (closed.contains(neighbor.getHashCode())) {
						continue;
					}
					neighbor.setHeuristic(neighbor.getHeuristic(goal));
					neighbor.setF(neighbor.getF());

					//TESTING : printing out the neighbour
//				System.out.print(neighbor.getMove() + ": ");
//				System.out.println(Arrays.deepToString(neighbor.getBoard()));
//				System.out.println("f value : " + neighbor.getF());
//				System.out.println("cost: " + neighbor.getDistanceFromStart());


					if (!closed.contains(neighbor.getHashCode())) {
						if (queueContains(q, neighbor)) {
							Vertex openNeighbor = q.stream().filter(n -> n.equals(neighbor)).findFirst().get();
							System.out.println("duma cai lon ma");
							if (openNeighbor.getDistanceFromStart() > neighbor.getDistanceFromStart()) {

								/////////////////////////
								System.out.println("We have NEIGHJDAD");
								q.remove(openNeighbor);
								neighbor.setParent(node);
								q.add(neighbor);
//							System.out.println("open neighbor: " + openNeighbor.getMove());
//							System.out.println("open f" + openNeighbor.getF());
//							System.out.println("neighbor: " + neighbor.getMove());
//							System.out.println("neighbor f" + neighbor.getF());

							}
						} else {
							neighbor.setParent(node);
							q.add(neighbor);
						}
					}
				}
				System.out.println("-------------------------------");
			}
			else{
				System.out.println("duma unsolvable*********************");
				System.out.println(Arrays.deepToString(node.getBoard()));
			}


			}

			//TESTING : printing the node that popped out
//			System.out.println("Popped out :  " + node.getMove());
//			System.out.println("cost: " + node.getDistanceFromStart());
//			System.out.println("f value : " + node.getF());




		return null;
	}
}
//		while (!q.isEmpty()) {
//			calWhile ++ ;
//			Vertex curr = q.remove();
//			System.out.println("Next move: " + curr.getMove());
//			ArrayList<Vertex> neighbors = curr.generateChild();
//			System.out.println("---------------------------------------");
//			for (Vertex u : neighbors) {
//				System.out.print(u.getMove() + ": ");
//				System.out.println(Arrays.deepToString(u.getBoard()));
//				u.setHeuristic(u.getHeuristic(goal));
//				u.setF(u.getF());
//				System.out.println("f value : " + u.getF());
//			}
//			System.out.println("---------------------------------------");
//			for (Vertex u:neighbors) {
//				u.setHeuristic(u.getHeuristic(goal));
//				u.setF(u.getF());
//				if (u.getHashCode() == goalVertex.getHashCode()) {
//					u.setParent(curr);
//					result = u;
//					System.out.println("time of while loop executed : " + calWhile);
//					return result; //Maybe change later
//				} else {
//					if (q.contains(u)) {
//						System.out.println("q contain");
//						for (Vertex i:q) {
//							if (i.getHashCode() == u.getHashCode() && i.getF() < u.getF()) {
//								i.setF(u.getF());
//								u.setParent(curr);
//							}
//						}
//					} else if (closed.containsKey(u.getHashCode())) {
//						System.out.println("closed contain: ");
//						System.out.println(u.getMove());
//						System.out.println(Arrays.deepToString(u.getBoard()));
//
//						if (closed.get(u.getHashCode()) > u.getF()) {
//							closed.put(u.getHashCode(), u.getF());
//							q.add(u);
//							u.setParent(curr);
//						}
//					} else {
//						q.add(u);
//						u.setParent(curr);
//					}
//				}
//			}
//			closed.put(curr.getHashCode(), curr.getF());
//		}
//		return null;
//	}
//}