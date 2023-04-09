package fifteenpuzzle;

import java.util.ArrayList;
import java.util.Arrays;


public class Vertex implements Comparable<Vertex>{
    private int[][] board;
    private int heuristic;
    private int distanceFromStart;
    private int f;
    private int[] blankPos;
    private String move;
    private Vertex parent;
    private int hashCode = 0;
    private int boardLength;
    private int[][] goal;

    public Vertex() {

    }

    public Vertex(int[][] board)  {
        this.board = board;
        this.goal = goal;
        this.boardLength = this.board.length;
        this.distanceFromStart = 0;
        this.heuristic = this.getHeuristic();
        this.move = "";
        this.blankPos = new int[2];
        this.findBlankPos();
        final int prime = 31;
        this.hashCode = 17;
        for (int[] row : this.board) {
            for (int val : row) {
                this.hashCode = prime * this.hashCode + val;
            }
        }
        this.f = this.heuristic;
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
    public int[][] shuffle(int x1, int y1, int x2, int y2) {
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

    public int getF() {
        return this.f; // if we make constructor put this in.
    }

    public static int linearConflict(int[][] board) {
//        int size = board.length;
//        int conflict = 0;
//        for (int i = 0; i < size; i++) {
//            int[] row = board[i];
//            int[] goalRow = IntStream.range(i*size+1, (i+1)*size).toArray();
//            for (int j = 0; j < size; j++) {
//                if (row[j] != 0 && IntStream.of(goalRow).anyMatch(x -> x == row[j])) {
//                    for (int k = j+1; k < size; k++) {
//                        if (row[k] != 0 && IntStream.of(goalRow).anyMatch(x -> x == row[k]) && row[k] < row[j]) {
//                            conflict++;
//                            break;
//                        }
//                    }
//                    for (int l = i+1; l < size; l++) {
//                        int col = board[l][j];
//                        if (col != 0 && IntStream.of(goalRow).anyMatch(x -> x == col) && col < row[j]) {
//                            conflict++;
//                            break;
//                        }
//                    }
//                }
//            }
        //}
        int horizontalConflict = 0;

        for (int i = 0; i < board.length; i++)
        {
            int max1 = -1;
            for (int j = 0; j < board.length; j++)
            {
                int value1 = board[i][j];
                if (value1 != 0 && value1 % board.length== i + 1)
                {
                    if (value1 > max1)
                    {
                        max1 = value1;
                    }
                    else
                    {
                        horizontalConflict += 2;
                    }
                }

            }

        }


        int verticalConflict = 0;

        for (int r = 0; r < board.length; r++)
        {
            int max2 = -1;
            for (int c = 0; c < board.length; c++)
            {
                int value2 = board[r][c];
                if (value2 != 0 && (value2 - 1) / board.length == r)
                {
                    if (value2 > max2)
                    {
                        max2 = value2;
                    }
                    else
                    {
                        verticalConflict += 2;
                    }
                }

            }

        }
        return horizontalConflict + verticalConflict;
    }
    private int getHeuristic() {
//        int distance = 0;
//        for (int i = 0; i < state.length; i++) {
//            if (state[i] == 0) {
//                continue;
//            }
//            int rowDiff = Math.abs(i / 4 - (state[i] - 1) / 4);
//            int colDiff = Math.abs(i % 4 - (state[i] - 1) % 4);
//            distance += rowDiff + colDiff;
//            // check for linear conflicts in row
//            if (i / 4 == (state[i] - 1) / 4) {
//                for (int j = i + 1; j < i / 4 * 4 + 4; j++) {
//                    if (state[j] == 0 || state[j] / 4 != i / 4) {
//                        continue;
//                    }
//                    if (state[i] > state[j] && (j - i) * (state[j] - 1 - i) > 0) {
//                        distance += 2;
//                    }
//                }
//            }
//            // check for linear conflicts in column
//            if (i % 4 == (state[i] - 1) % 4) {
//                for (int j = i + 4; j < 16; j += 4) {
//                    if (state[j] == 0 || state[j] % 4 != i % 4) {
//                        continue;
//                    }
//                    if (state[i] > state[j] && (j - i) / 4 * (state[j] - 1 - i) > 0) {
//                        distance += 2;
//                    }
//                }
//            }
//        }

            int heuristicCost = 0;
            int numRows = this.board.length;
            int numCols = this.board[0].length;
            int idealRow;
            int idealCol;
            int currentRow;
            int currentCol;
            int value;

            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numCols; col++) {
                    value = this.board[row][col];
                    if (value == 0) {
                        continue;
                    }

                    idealRow = (value - 1) / numCols;
                    idealCol = (value - 1) % numCols;

                    currentRow = row;
                    currentCol = col;

                    heuristicCost += Math.abs(idealRow - currentRow) + Math.abs(idealCol - currentCol);
                }
            }


        return heuristicCost + linearConflict(board);
    }

//    public int getHeuristic() {
//        int heuristicCost = 0;
//        int numRows = this.board.length;
//        int numCols = this.board[0].length;
//        int idealRow;
//        int idealCol;
//        int currentRow;
//        int currentCol;
//        int value;
//
//        for (int row = 0; row < numRows; row++) {
//            for (int col = 0; col < numCols; col++) {
//                value = this.board[row][col];
//                if (value == 0) {
//                    continue;
//                }
//
//                idealRow = (value - 1) / numCols;
//                idealCol = (value - 1) % numCols;
//
//                currentRow = row;
//                currentCol = col;
//
//                heuristicCost += Math.abs(idealRow - currentRow) + Math.abs(idealCol - currentCol);
//            }
//        }
//
//        return heuristicCost;
//    }
//    public  int getHeuristic(int[][] goal) {
//        int distance = 0;
//        int n = board.length;
//        HashMap<Integer, int[]> goalPositions = new HashMap<>();
//
//        // Store the goal positions of each number in a map for O(1) lookup
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                goalPositions.put(goal[i][j], new int[]{i, j});
//            }
//        }
//
//        // Calculate the Manhattan distance for each number on the board
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                int num = board[i][j];
//                if (num == 0) {
//                    continue;
//                }
//                int[] goalPos = goalPositions.get(num);
//                distance += Math.abs(i - goalPos[0]) + Math.abs(j - goalPos[1]);
//            }
//        }
//
//        return distance;
//    }

    @Override
    public boolean equals(Object other) {
        return other != null && ((Vertex) other).getHashCode() == this.getHashCode();
    }

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
        if (other != null) {
            return Integer.compare(this.getF(), other.getF());
        }
        return 0;
    }

    public int[][] getBoard() {
        return this.board;
    }
}