import java.util.Scanner;
import java.util.*; //ArrayList
public class StartReversi {
  public static void main(String[] args) {
    Board board = new Board();
    Alphabeta ai = null;
    board.printboard();
    boolean checkValid = true;
    String step = null;
    //as long as there exist any move for player, then game does not end
    System.out.println("input 0: want human vs huamn\ninput 1: human vs AI ");
    Scanner reader = new Scanner(System.in);  // Reading from System.in
    int mode = reader.nextInt();
    if (mode == 0) { // human vs human
      while(board.generate_moves('X') != null || board.generate_moves('O') != null ){
        do{
          if (checkValid) {
            System.out.println("Please input your step : ");
            HashSet<String> moves = new HashSet<String>();
            moves = board.generate_moves(board.getPlayer());
            //for (int i = 0; i < moves.size(); i++) {
            System.out.println(moves);
            moves.clear();
            //}
          }else System.out.println("Invalid step, Please input your step: ");
          reader = new Scanner(System.in);  // Reading from System.in
          step = reader.nextLine().toString();
          checkValid = board.checkValidStep(step); // check whether such move can make, if can, flip 'X' to 'O', or vice verse.
        } while (!checkValid);

        //valid step, then update the board
        int row = step.charAt(0) - 65;// turn 0-7 in ascii to int 0-7
        int column = step.charAt(1) - 48; // turn A-H to int 0-7
        board.setBoard(row, column, board.getPlayer()); //getPlayer() can return 'X' or 'O'
        board.printboard();
      }
    }else if (mode == 1) { // AI here
        boolean maxPlayer = true;

        while(!board.generate_moves('X').isEmpty()|| !board.generate_moves('O').isEmpty() ){
            if (maxPlayer) { // YOU
                if (!board.generate_moves('X').isEmpty()) {
                  System.out.println("Please input your step : ");
                  HashSet<String> moves = new HashSet<String>();
                  moves = board.generate_moves('X');
                  System.out.println("hint:"+moves);
                  moves.clear();
                  checkValid = true;
                  do{
                    if (!checkValid) {
                      System.out.println("Invalid input, re-enter:");
                    }
                    reader = new Scanner(System.in);
                    step = reader.nextLine().toString();
                    checkValid = board.checkValidStep(step);
                  } while (!checkValid);
                  int row = step.charAt(0) - 65;// turn 0-7 in ascii to int 0-7
                  int column = step.charAt(1) - 48; // turn A-H to int 0-7
                  board.setBoard(row, column, 'X'); //getPlayer() can return 'X' or 'O'
                  board.printboard();
                }else{
                  //System.out.println(board.getPlayer());
                  board.haveNoAction(1);// if no action, just add 1 to var count, when do getPlayer based on #, such count will add on it.
                  System.out.println("You don't have any step to move, turn to AI.");
                  //System.out.println(board.getPlayer());
                }
                maxPlayer = false;
          }else if (!maxPlayer) { //AI
              if (!board.generate_moves('O').isEmpty()) {
                int depth = 5;
                ai = new Alphabeta(board,depth);
                String action = ai.makeDecision();
                System.out.println("Ai action is :"+action);
                board.checkValidStep(action);
                int row = action.charAt(0) - 65;// turn 0-7 in ascii to int 0-7
                int column = action.charAt(1) - 48; // turn A-H to int 0-7
                board.setBoard(row, column, 'O'); //getPlayer() can return 'X' or 'O'
                board.printboard();
              }else{
                board.haveNoAction(1);
                System.out.println("AI doesn't have any step to move, your turn.");
              }
              maxPlayer = true;
          }else {
            //
          }
        }//while end

        //judge who Win
        int x = 0, o = 0;
        for (int i = 0; i < 8 ;i++ ) {
          for (int j = 0; j < 8 ; j++ ) {
             if (board.board[i][j] == 'X') {
               x++;
             }else if(board.board[i][j] == 'O') o++;
             else {//
             }
          }
        }
        if (x>o) {
          System.out.println("You Win! :"+x+" : "+o);
        }else System.out.println("You Lose! :"+x+" : "+o);
    }// human vs ai end
    else{ // wrong mode
        System.out.println("Invalid input, plz restart and input 0 or 1.");
    }
  }
}
