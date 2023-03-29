package fifteenpuzzle;

import java.util.ArrayList;

public class Vertex {
    private int hashCode;
    private int[][] board;
    private int heuristic;
    private int distanceFromStart;
    private int f;
    private int[][] goal;
    private int[] blankPos;

    public Vertex() {

    }

    public Vertex(int[][] b) {
        this.goal = new int[board.length][board.length];
        this.board = b;
        this.heuristic = getHeuristic();
        this.f = getF();
        this.distanceFromStart = 0;
    }

    public ArrayList<int[][]> generateChild() {
        int x = this.blankPos[0];
        int y = this.blankPos[1];
        int[][] moves = { { x, y - 1 }, { x, y + 1 }, { x - 1, y }, { x + 1, y } };
        ArrayList<int[][]> children;
        for (int[] i : moves) {
            int[][] child = this.shuffle(x, y, i[0], i[1]);
            if (child != null) {
                Vertex childVertex = new Vertex(child);
                childVertex.setDist(this.distanceFromStart + 1);
                children.add(childVertex);      
           }
     

        return children;
    }


    public void setDist(int newDist) {
        this.distanceFromStart = newDist;
    }

    public int[][] shuffle(int x1, int y1, int x2, int y2) {
        //Move the blank space and return the valid board state
        if (x2 >= 0 && x2 < this.board.length && y2 >= 0 && y2 < this.board.length) {
            int[][] temp_puz = new int[board.length][board.length];
            int temp;
            temp_puz = this.board;
            temp = temp_puz[x2][y2];
            temp_puz[x2][y2] = temp_puz[x1][y1];
            temp_puz[x1][y1] = temp;
            return temp_puz;
        } else {
            return null;
        }
    }

    public int getF() {
        this.f = heuristic + distanceFromStart; // if we make constructor put this in.
        return this.f;
    }

    public int getHeuristic(){
        int temp = 0 ;

        for ( int i = 0; i < board.length; i++){
            for ( int j = 0 ; j < board.length; j++){
                if (this.board[i][j] != goal[i][j])
            }
        }
    }

}
