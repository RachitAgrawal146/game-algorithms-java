package Connect4;

public class Board
{
    int [] [] board = new int [6][7];

    // Reset
    public static final String ANSI_RESET = "\u001B[0m";

    // Regular Colors

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_WHITE = "\u001B[37m";

    void print ()
    {

        for (int i = 0 ; i < 6 ; i++)
        {
            for (int j = 0 ; j < 7 ; j++)
            {
                if (board[i][j] == 0)
                    System.out.print("|  ");
                else if (board[i][j] == 1)
                    System.out.print("|\u26AA");
                else
                    System.out.print("|\u26AB");
            }
            System.out.println("|");
        }
        System.out.println("----------------------");
    }
}
