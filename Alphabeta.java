import java.lang.Math; //Math.max min
import java.util.*; //ArrayList
public class Alphabeta{
  Board board = new Board();
  char[][] board_copy;
  int depth = 0;
  public Alphabeta(Board board/*state*/, int depth){
    //this.board.board = board.getBoard(); java pass object by reference, the same with char array. thus we cant directly assign to it.
    board_copy = board.getBoard();
    for (int i = 0; i < 8 ; i++ ) {
      for (int j = 0 ; j < 8 ;j++ ) {
        this.board.setBoard(i,j,board_copy[i][j]);
      }
    }
    //System.out.println(this.board.getPlayer());
    this.board.countNoAction = board.countNoAction;
    //System.out.println(this.board.getPlayer());
    this.depth = depth;
  }

  public String makeDecision() { /* (Board state) return String action 'A5' */
      String result = null; // format will be like 'D6'
      double resultValue = Double.NEGATIVE_INFINITY;
      char player = board.getPlayer(); // 'X' or 'O'
    //System.out.println( board.generate_moves(player));

      Board b = new Board(board);
      //System.out.println(b.generate_moves(player)); generate_moves ok!
      for (String action : b.generate_moves(player)) {
          //System.out.println("root-"+action);

          //sovle bug with board update: within loop, every time, board shud back to orignal status, but it changed after every time loop. need to solve:
          Board stage_board = new Board(board);

          stage_board.checkValidStep(action);
          int row = action.charAt(0) - 65;// turn 0-7 in ascii to int 0-7
          int column = action.charAt(1) - 48; // turn A-H to int 0-7
          stage_board.setBoard(row, column, 'O');
          //stage_board.printboard();
          char nxtplayer = stage_board.getPlayer();
          double value = minValue(stage_board, nxtplayer,
                  Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, depth - 1);
          if (value > resultValue) {
              result = action;
              resultValue = value;
          }
      }
      return result;
  }

  public double maxValue(Board state, char player, double alpha, double beta, int d) {
      if (state.isTerminal(player)|| d == 0){  //isTerminal():check whether board is full, assume d≠o, but already full, then return v
          //System.out.println("max return");
          return state.getUtility('O'); // utility always for ai 'O'
        }
      double value = Double.NEGATIVE_INFINITY;
      //System.out.println("in max- choose step:" + board.generate_moves(player));
    //  HashSet<String> moveList = state.generate_moves(player);
      Board b = new Board(state);
      for (String action : b.generate_moves(player)) {
          //System.out.println("max-"+action);

          Board stage_board = new Board(state);

          stage_board.checkValidStep(action);


          int row = action.charAt(0) - 65;// turn 0-7 in ascii to int 0-7
          int column = action.charAt(1) - 48; // turn A-H to int 0-7
          stage_board.setBoard(row, column, player);
          //stage_board.printboard();

          char nxtplayer = stage_board.getPlayer();
          value = Math.max(value, minValue(stage_board, nxtplayer, alpha, beta, d - 1));
          //System.out.println("max value:"+value);
          if (value >= beta)
              return value;
          alpha = Math.max(alpha, value);
      }
      return value;
  }

  public double minValue(Board state, char player, double alpha, double beta, int d) {
      if (state.isTerminal(player)||d == 0){
          //System.out.println("min return");
          return state.getUtility('O');// utility always for ai 'O'
        }
      double value = Double.POSITIVE_INFINITY;
      //System.out.println("in min- choose step:" + board.generate_moves(player));

      Board b = new Board(state);
      for (String action : b.generate_moves(player)) {
          //System.out.println("min-"+action);

          Board stage_board = new Board(state);

          stage_board.checkValidStep(action);

          int row = action.charAt(0) - 65;// turn 0-7 in ascii to int 0-7
          int column = action.charAt(1) - 48; // turn A-H to int 0-7
          stage_board.setBoard(row, column, player);
          //stage_board.printboard();

          char nxtplayer = stage_board.getPlayer();
          value = Math.min(value, maxValue(stage_board, nxtplayer, alpha, beta, d - 1));
          //System.out.println("min value:"+value);
          if (value <= alpha)
              return value;
          beta = Math.min(beta, value);
      }
      return value;
  }

}
