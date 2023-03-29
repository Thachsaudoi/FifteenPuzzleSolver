package fifteenpuzzle;

public class vertex {
    private int hashCode;
    private int[][] board;
    private int heuristic;
    private int distanceFromStart;
    private int f;
    private int[][] goal;

    public vertex(int[][] b){
        this.goal = new int[board.length][board.length];
        this.board = b;
        this.heuristic = getHeuristic();
        this.f = getF();
        this.distanceFromStart =0;

    }
    public int getF(){
        this.f = heuristic+ distanceFromStart; // if we make constructor put this in.
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
