package fifteenpuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Vertex implements Comparable<Vertex>{
    private int[][] board;
    private int heuristic;
    private int distanceFromStart;
    private int f;// fi will be thie price of the
    private int[] blankPos;
    private String move;
    private Vertex parent;
    private int hashCode = 0;
    private int boardLength;

    public Vertex() {

    }

    public Vertex(int[][] board)  {
        this.board = board;
        this.boardLength = this.board.length;
        this.distanceFromStart = 0;
        this.heuristic = 0;
        this.move = "";
        this.blankPos = new int[2];
        this.findBlankPos();
        final int prime = 31;
        this.hashCode = 17;
        for (int[] row : this.board) {
            for (int val : row) {
                this.hashCode = 31 * this.hashCode + val;
            }
        }
    }

    public ArrayList<Vertex> generateChild() {
        int x = this.blankPos[0];
        int y = this.blankPos[1];
        int[][] moves = { { x, y - 1 }, { x, y + 1 }, { x - 1, y }, { x + 1, y } };
        //strMoves are in reverse order (D -> U and U ->D) because 0 move up means the upper one moves down
        String[] strMoves = { "R", "L", "D", "U"};
        ArrayList<Vertex> children = new ArrayList<>();
        int moveLength = moves.length;
        for (int i = 0; i < moveLength; i++) {
            int[][] child = this.shuffle(x, y, moves[i][0], moves[i][1]);
            if (child != null) {
                Vertex childVertex = new Vertex(child);
                String m = this.board[moves[i][0]][moves[i][1]] + " " + strMoves[i];
                childVertex.setMove(m);
                childVertex.setDist(this.distanceFromStart + 1);
                children.add(childVertex);
            }

        }

        return children;
    }

    public int getHashCode() {
        return this.hashCode;
    }

    public int getDistanceFromStart() {
        return this.distanceFromStart;
    }
    public void setMove(String move) {
        this.move = move;
    }


    public String getMove() {
        return this.move;
    }

    public void setParent(Vertex parent) {
        this.parent = parent;
    }

    public Vertex getParent() {
        return this.parent;
    }

    public void setDist(int newDist) {
        this.distanceFromStart = newDist;
    }


    public void setF(int f) {
        this.f = f;
    }

    public void setHeuristic(int h) {
        this.heuristic = h;
    }

    public int[][] shuffle(int x1, int y1, int x2, int y2) {
        //Move the blank space and return the valid board state

        if (x2 >= 0 && x2 < this.boardLength && y2 >= 0 && y2 < this.boardLength) {
            int[][] temp_puz = new int[this.boardLength][this.boardLength];
            int temp;
            for (int i = 0; i < this.boardLength; i++) {
                temp_puz[i] = Arrays.copyOf(this.board[i], this.boardLength);
            }
            temp = temp_puz[x2][y2];
            temp_puz[x2][y2] = temp_puz[x1][y1];
            temp_puz[x1][y1] = temp;
            return temp_puz;
        } else {
            return null;
        }
    }

    public int getF() {
        return this.heuristic + this.distanceFromStart; // if we make constructor put this in.
    }

    public int[] getCoordinate(int tile, int[][] position) {
        if (position == null) {
            position = this.board;
        }
        for (int i = 0; i < this.boardLength; i++) {
            for (int j = 0; j < this.boardLength; j++) {
                if (position[i][j] == tile) {
                    return new int[]{i,j};
                }
            }
        }
        return null;
    }
    public  int getHeuristic(int[][] goal) {
        int distance = 0;
        int n = board.length;
        HashMap<Integer, int[]> goalPositions = new HashMap<>();

        // Store the goal positions of each number in a map for O(1) lookup
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                goalPositions.put(goal[i][j], new int[]{i, j});
            }
        }

        // Calculate the Manhattan distance for each number on the board
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int num = board[i][j];
                if (num == 0) {
                    continue;
                }
                int[] goalPos = goalPositions.get(num);
                distance += Math.abs(i - goalPos[0]) + Math.abs(j - goalPos[1]);
            }
        }

        return distance;
    }

    @Override
    public boolean equals(Object other) {
        return ((Vertex) other).getHashCode() == this.getHashCode();
    }
    //    public int getHeuristic(int[][] goalBoard){
//        int distance = 0;
//        for (int i =0; i < this.boardLength; i++) {
//            for (int j = 0; j < this.boardLength; j++) {
//                int[] coordinates = this.getCoordinate(this.board[i][j], goalBoard);
//                int i1 = coordinates[0];
//                int j1 = coordinates[1];
//                distance += Math.abs(i - i1) + Math.abs(j - j1);
//            }
//        }
//        return distance;
//    }

    public void findBlankPos() {
        for (int i = 0; i < this.boardLength; i++) {
            for (int j = 0; j < this.boardLength; j++) {
                if (this.board[i][j] == 0) {
                    this.blankPos[0] = i;
                    this.blankPos[1] = j;
                    break;
                }
            }
        }
    }

    @Override
    public int compareTo(Vertex other) {

        if (other.f == this.f) {
//          System.out.println("other f " + other.f + " this f " + this.f);
//          System.out.println(" this get dist " + this.getDistanceFromStart() + " , other get dist " + other.getDistanceFromStart());
//          System.out.println(Integer.compare(this.getDistanceFromStart(), other.getDistanceFromStart()));
            return Integer.compare(this.getDistanceFromStart(), other.getDistanceFromStart());
        }

//        return other.f < this.f ? 1 : -1;
        return Integer.compare(this.getF(), other.getF());
    }

    public int[][] getBoard() {
        return this.board;
    }
}
