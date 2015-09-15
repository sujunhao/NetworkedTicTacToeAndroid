package edu.carleton.comp2601client_100892373;
public class Game {
    private static Game gameInstance = null;
    int[][] table;
    boolean turn; //true if it is x's turn//This is confusing because the computer can play for x


    public Game() {
        table = new int[3][3];
        initializeBoard();
        turn = true;
        gameInstance = this;
    }
    public static Game getGameInstance(){
        if (gameInstance == null){
            gameInstance = new Game();
        }
        return  gameInstance;
    }

    public  void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                table[i][j] = 0;
            }
        }
    }

    public void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(table[i][j] + " | ");
            }
            System.out.println();
            System.out.println("-------------");
        }
    }

    public  int checkRowsAndColumns() {
        int winner = 0;
        //Check rows
        for (int i = 0; i < 3; i++) {
            //rows
            if ((table[i][0] + table[i][1] + table[i][2]) > 2) {
                winner = 1;
            }
            if ((table[i][0] + table[i][1] + table[i][2]) < -2) {
                winner = -1;
            }
            //columns
            if ((table[0][i] + table[1][i] + table[2][i]) > 2) {
                winner = 1;
            }
            if ((table[0][i] + table[1][i] + table[2][i]) < -2) {
                winner = -1;
            }
        }
        return winner;
    }

    public  int checkDiagonals() {
        if (table[0][0] + table[1][1] + table[2][2] > 2 || table[0][2] + table[1][1] + table[2][0] > 2 ){
            return 1;
        }
        if (table[0][0] + table[1][1] + table[2][2] <  -2 || table[0][2] + table[1][1] + table[2][0] < -2){
            return -1;
        }
        else{
            return 0;
        }
    }
    public boolean checkForFull(){
        boolean isFull = true;
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (table[i][j] == 0){
                    isFull = false;
                }
            }
        }
        return isFull;
    }

    public int checkForWin(){
        if (checkRowsAndColumns() > 0){
            return 1;
        }
        if (checkRowsAndColumns() < 0){
            return -1;
        }
        if (checkDiagonals() > 0){
            return 1;
        }
        if (checkDiagonals() < 0){
            return -1;
        }
        if (checkForFull() == true){
            return 2;
        }
        else{
            return 0;
        }
    }



    //Handle player place because only play can place x
    public boolean playerPlace(int row, int col) {
        if ((row >= 0) && (row < 3)){
            if ((col >= 0) && (col < 3)){
                if (table[row][col] == 0){
                        table[row][col] = 1;
                        turn = false;
                        return true;
                }
            }
        }
        return false;
    }
}
