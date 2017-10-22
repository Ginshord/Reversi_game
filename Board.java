import java.lang.String;
import java.util.*; //ArrayList

class Board {
  int countNoAction = 0;
  public char board[][] = new char[8][8];
  HashSet<String> moves = new HashSet<String>(); //will avoid duplicated elements
  private static double[][] board_value =
      {
      	{100, -1, 5, 4, 4, 5, -1, 100},
      	{-1, -20,1, 1, 1, 1,-20, -1},
      	{5 , 1,  1, 1, 1, 1,  1,  5},
      	{4 , 1,  1, 0, 0, 1,  1,  4},
      	{4 , 1,  1, 0, 0, 1,  1,  4},
      	{5 , 1,  1, 1, 1, 1,  1,  5},
      	{-1,-20, 1, 1, 1, 1,-20, -1},
      	{100, -1, 5, 4, 4, 5, -1, 100}
      };
    //initialize the board
    public Board() {
      for (int i = 0; i < 8; i++) {
        for (int j = 0 ; j< 8 ;j++ ) {
           board[i][j] = '.';
        }
      }
      board[3][3] = 'X';board[3][4] = 'O';  // O = white  for AI
      board[4][3] = 'O';board[4][4] = 'X';  // X = black  for user
    }

    public Board(Board b) {
      for (int i = 0; i < 8; i++) {
        for (int j = 0 ; j< 8 ;j++ ) {
          setBoard(i,j,b.board[i][j]);
        }
      }
      countNoAction = b.countNoAction;
    }

    //printout the board
    public void printboard(){
      int initial_char = 65;
      System.out.println("  0 1 2 3 4 5 6 7 ");
      for (int x = 0; x < 8; x++) {
          System.out.printf("%c ",initial_char);
          for (int y = 0; y < 8; y++) {
              System.out.print(board[x][y]+" ");
          }
          System.out.println();
          initial_char++;
      }
    }

    public char[][] getBoard(){
      return board;
    }

    //just play a piece on board, instead of this.board = board; just in case that pass by reference.
    public void setBoard(int row, int column, char v){
      board[row][column] = v;
    }

    public void haveNoAction(int num){
      countNoAction += num;
    }

    //through the number of pieces, figure out who's next player
    public char getPlayer(){
      int count = 0;
      for (int i = 0; i < 8; i++) {
        for (int j = 0 ; j< 8 ;j++ ) {
           if (board[i][j] != '.') {
             count++;
           }
        }
      }
      count += countNoAction;
      if (count%2 != 0) { //default: 'X' black first start, when # is even => black 'X' player.
        return 'O';
      }else return 'X';
    }

    //This function takes the current board and generates a list of possible moves for the current player.
    //check 8*8 pieces, if it's empty '.', then check whether such empty position could be placed a piece.
    public HashSet<String> generate_moves(char player){  //player would be either 'X' or 'O'
      moves.clear();
      for (int row = 0; row < 8; row++) {
        for (int column = 0 ; column < 8 ; column++) {
            if (board[row][column] == '.') {
              if (check_moves(row, column, player)) {
                //turn row, column to 'A5'
                char a = (char)(row+65);
                char b = (char)(column + '0');
                String step = "" + a + b;
                //System.out.println("step is:"+step);
                moves.add(step);
              }
            }
        }
      }
      return moves;
    }

    public boolean isTerminal(char player){
      HashSet<String> move;
      if (!(move = generate_moves(player)).isEmpty()) {
        return false;
      }else {
        //System.out.println("Terminal!");
        return true;
      }

    }

    public double getUtility(char player){
      //System.out.print("board_value:");
      //  System.out.println(board_value[0][0]);
      double value = 0.00;
      for (int row = 0; row < 8 ;row++ ) {
        for (int column = 0; column < 8 ;column++ ) {
          if (board[row][column] == player) {
            //System.out.println("board_value"+board_value[row][column]);
            value += board_value[row][column];
          }
        }
      }
    //System.out.println(value);
      return value;
    }


    public boolean check_moves(int row1, int column1, char player){
        int row,column;
        char opponent;
        if (player == 'X') {
          opponent = 'O';
        }else opponent = 'X';

        //check if such step satisfy the reversi rule: 'O' settled between one X and new step 'X' in 8 directions
        row = row1; column = column1;
        if (row - 1 >= 0 && board[row - 1][column] == opponent) {// check the piece above step 'X' is 'O'
          while(row - 1 >= 0 && board[row - 1][column] == opponent){
            row--;
          }
          if (row - 1 < 0 ){} //
          else if(board[row - 1][column] == player) return true;
          else{} //
        }
        row = row1; column = column1;
        if (row + 1 <= 7 && board[row + 1][column] == opponent) {// check the piece below step 'X' is 'O'
          while(row + 1 <= 7 && board[row + 1][column] == opponent){
            row++;
          }
          if (row + 1 > 7){} //
          else if(board[row + 1][column] == player) return true;
          else{} //
        }
        row = row1; column = column1;
        if (column + 1 <= 7 && board[row][column+1] == opponent) {// check the right piece of step 'X' is 'O'
          while(column + 1 <= 7 && board[row][column+1] == opponent){
            column++;
          }
          if (column + 1 > 7){} //
          else if(board[row][column+1] == player) return true;
          else{} //
        }
        row = row1; column = column1;
        if (column - 1 >= 0 && board[row][column-1] == opponent) {// check the left piece of step 'X' is 'O'
          while(column - 1 >= 0 && board[row][column-1] == opponent){
            column--;
          }
          if (column - 1 < 0 ) {}//
          else if(board[row][column-1] == player) return true;
          else{} //
        }
        row = row1; column = column1;
        if (row - 1 >= 0 && column + 1 <= 7 && board[row - 1][column + 1] == opponent) {// check the upper	right piece of step 'X' is 'O'
          while(row - 1 >= 0 && column + 1 <= 7 && board[row - 1][column + 1] == opponent){
            row--;
            column++;
          }
          if (row - 1 < 0 || column + 1 > 7){} //
          else if(board[row - 1][column + 1] == player) return true;
          else {}//
        }
        row = row1; column = column1;
        if (row - 1 >= 0 && column - 1 >= 0 && board[row - 1][column - 1] == opponent) {// check the upper	left piece of step 'X' is 'O'
          while(row - 1 >= 0 && column - 1 >= 0 && board[row - 1][column - 1] == opponent){
            row--;
            column--;
          }
          if (row - 1 < 0 || column - 1 < 0){} //
          else if(board[row - 1][column - 1] == player) return true;
          else {}//
        }
        row = row1; column = column1;
        if (row + 1 <= 7 && column + 1 <= 7 && board[row + 1][column + 1] == opponent) {// check the	right bottom piece of step 'X' is 'O'
          while(row + 1 <= 7 && column + 1 <= 7 && board[row + 1][column + 1] == opponent){
            row++;
            column++;
          }
          if (row + 1 > 7 || column + 1 > 7) {}//
          else if(board[row + 1][column + 1] == player) return true;
          else{} //
        }
        row = row1; column = column1;
        if (row + 1 <= 7 && column - 1 >= 0 && board[row + 1][column - 1] == opponent) {// check the left bottom piece of step 'X' is 'O'
          while(row + 1 <= 7 && column - 1 >= 0 && board[row + 1][column - 1] == opponent){
            row++;
            column--;
          }
          if (row + 1 > 7 || column - 1 < 0){} //
          else if(board[row + 1][column - 1] == player) return true;
          else {}//
        }
        return false; // pieces around such step is black 'X' or no piece around such step or no another 'X' surround 'O' with this step
    }



    //check whether a user's step 'X' is valid , and BTW reverse the white pieces 'O' into black 'X'
    public boolean checkValidStep(String step){
        // separate the step like 'D6' into 'D' and '6'
        int row1 = 0,column1 = 0;  //board[row - 65][column] = 'X'
        int row,column;
        char opponent, hplayer;
        boolean valid = false;
        if ((hplayer=getPlayer())=='X') {
          opponent = 'O';
        }else opponent = 'X';

        if (step != null) {
          row1 = step.charAt(0) - 65;
          column1 = step.charAt(1)-48;  //board[row - 65][column] = 'X'
        }

        //check if this position already has piece
        if (step != null && row1<=7 && row1>=0 && column1 <=7 && column1 >=0 && board[row1][column1] != '.') {
          return false;
        }

        //check if such step satisfy the reversi rule: 'O' settled between one X and new step 'X' in 8 directions
        row = row1; column = column1;
        //System.out.println(row + " " + column);
        if (row - 1 >= 0 && board[row - 1][column] == opponent) {// check the piece above step 'X' is 'O'
          while(row - 1 >= 0 && board[row - 1][column] == opponent){
            row--;
          }
          if (row - 1 < 0 ){} //
          else if(board[row - 1][column] == hplayer) {
            row = row1; column = column1;
            while(row - 1 >= 0 && board[row - 1][column] == opponent){
              board[row - 1][column] = hplayer;
              row--;
            }
            valid = true;
          }
          else{} //
        }
        row = row1; column = column1;
        if (row + 1 <= 7 && board[row + 1][column] == opponent) {// check the piece below step 'X' is 'O'
          while(row + 1 <= 7 && board[row + 1][column] == opponent){
            row++;
          }
          if (row + 1 > 7){} //
          else if(board[row + 1][column] == hplayer){
            row = row1; column = column1;
            while(row + 1 <= 7 && board[row + 1][column] == opponent){
              board[row + 1][column] = hplayer;
              row++;
            }
            valid = true;
          }
          else{} //
        }
        row = row1; column = column1;
        if (column + 1 <= 7 && board[row][column+1] == opponent) {// check the right piece of step 'X' is 'O'
          while(column + 1 <= 7 && board[row][column+1] == opponent){
            column++;
          }
          if (column + 1 > 7){} //
          else if(board[row][column+1] == hplayer) {
            row = row1; column = column1;
            while(column + 1 <= 7 && board[row][column+1] == opponent){
              board[row][column+1] = hplayer;
              column++;
            }
            valid = true;
          }
          else{} //
        }
        row = row1; column = column1;
        if (column - 1 >= 0 && board[row][column-1] == opponent) {// check the left piece of step 'X' is 'O'
          while(column - 1 >= 0 && board[row][column-1] == opponent){
            column--;
          }
          if (column - 1 < 0 ) {}//
          else if(board[row][column-1] == hplayer) {
            row = row1; column = column1;
            while(column - 1 >= 0 && board[row][column-1] == opponent){
              board[row][column-1] = hplayer;
              column--;
            }
            valid = true;
          }
          else{} //
        }
        row = row1; column = column1;
        if (row - 1 >= 0 && column + 1 <= 7 && board[row - 1][column + 1] == opponent) {// check the upper	right piece of step 'X' is 'O'
          while(row - 1 >= 0 && column + 1 <= 7 && board[row - 1][column + 1] == opponent){
            row--;
            column++;
          }
          if (row - 1 < 0 || column + 1 > 7){} //
          else if(board[row - 1][column + 1] == hplayer) {
            row = row1; column = column1;
            while(row - 1 >= 0 && column + 1 <= 7  && board[row-1][column+1] == opponent){
              board[row-1][column+1] = hplayer;
              row--;
              column++;
            }
            valid = true;
          }
          else {}//
        }
        row = row1; column = column1;
        if (row - 1 >= 0 && column - 1 >= 0 && board[row - 1][column - 1] == opponent) {// check the upper	left piece of step 'X' is 'O'
          while(row - 1 >= 0 && column - 1 >= 0 && board[row - 1][column - 1] == opponent){
            row--;
            column--;
          }
          if (row - 1 < 0 || column - 1 < 0){} //
          else if(board[row - 1][column - 1] == hplayer) {
            row = row1; column = column1;
            while(row - 1 >= 0 && column - 1 >= 0  && board[row-1][column-1] == opponent){
              board[row-1][column-1] = hplayer;
              row--;
              column--;
            }
            valid = true;
          }
          else {}//
        }
        row = row1; column = column1;
        if (row + 1 <= 7 && column + 1 <= 7 && board[row + 1][column + 1] == opponent) {// check the	right bottom piece of step 'X' is 'O'
          while(row + 1 <= 7 && column + 1 <= 7 && board[row + 1][column + 1] == opponent){
            row++;
            column++;
          }
          if (row + 1 > 7 || column + 1 > 7) {}//
          else if(board[row + 1][column + 1] == hplayer) {
            row = row1; column = column1;
            while(row + 1 <= 7 && column + 1 <= 7  && board[row+1][column+1] == opponent){
              board[row+1][column+1] = hplayer;
              row++;
              column++;
            }
            valid = true;
          }
          else{} //
        }
        row = row1; column = column1;
        if (row + 1 <= 7 && column - 1 >= 0 && board[row + 1][column - 1] == opponent) {// check the left bottom piece of step 'X' is 'O'
          while(row + 1 <= 7 && column - 1 >= 0 && board[row + 1][column - 1] == opponent){
            row++;
            column--;
          }
          if (row + 1 > 7 || column - 1 < 0){} //
          else if(board[row + 1][column - 1] == hplayer) {
            row = row1; column = column1;
            while(row + 1 <= 7 && column - 1 >= 0  && board[row+1][column-1] == opponent){
              board[row+1][column-1] = hplayer;
              row++;
              column--;
            }
            valid = true;
          }
          else {}//
        }
        return valid; // pieces around such step is black 'X' or no piece around such step or no another 'X' surround 'O' with this step
    }
}
