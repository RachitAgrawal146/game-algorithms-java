import java.util.ArrayList;

/**
 * Represents a Connect 4 game board (6 rows x 7 columns).
 * Handles piece placement, win/draw detection, and board display.
 */
public class Board
{
    int [] [] board = new int [6] [7];

    // ----------------------------------------------------------------
    // ANSI colour codes for terminal output
    // ----------------------------------------------------------------

    public static final String ANSI_RESET  = "\u001B[0m";
    public static final String ANSI_RED    = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_WHITE  = "\u001B[37m";

    // ----------------------------------------------------------------
    // Display
    // ----------------------------------------------------------------

    /**
     * Prints the board to the console with coloured pieces.
     * Piece 1 = red, Piece 2 = yellow, empty = blank.
     */
    void print ()
    {
        System.out.println(" 1   2   3   4   5   6   7");
        for (int i = 0 ; i < 6 ; i += 1)
        {
            for (int j = 0 ; j < 7 ; j += 1)
            {
                if (board[i][j] == 0)
                    System.out.print("|  ");
                else if (board[i][j] == 1)
                    System.out.print("|" + ANSI_RED + "\u26AA" + ANSI_RESET);
                else
                    System.out.print("|" + ANSI_YELLOW + "\u26AB" + ANSI_RESET);
            }
            System.out.println("|");
        }
        System.out.println("----------------------------");
    }

    // ----------------------------------------------------------------
    // Piece placement
    // ----------------------------------------------------------------

    /**
     * Drops a piece into the given column.
     * @param col   the column index (0-6)
     * @param piece the piece to drop (1 or 2)
     * @return the row the piece landed in, or -1 if the column is full
     */
    int dropPiece (int col, int piece)
    {
        for (int i = 5 ; i >= 0 ; i -= 1)
        {
            if (board[i][col] == 0)
            {
                board[i][col] = piece;
                return i;
            }
        }
        return -1;
    }

    /**
     * Removes the topmost piece from the given column.
     * Used by the AI to undo simulated moves.
     * @param col the column index (0-6)
     */
    void undoPiece (int col)
    {
        for (int i = 0 ; i < 6 ; i += 1)
        {
            if (board[i][col] != 0)
            {
                board[i][col] = 0;
                return;
            }
        }
    }

    // ----------------------------------------------------------------
    // Board queries
    // ----------------------------------------------------------------

    /**
     * Checks whether the given column is full.
     * @param col the column index (0-6)
     * @return true if no empty cell exists in this column
     */
    boolean isColumnFull (int col)
    {
        return board[0][col] != 0;
    }

    /**
     * Checks whether every cell on the board is occupied.
     * @return true if the board is completely full
     */
    boolean isBoardFull ()
    {
        for (int j = 0 ; j < 7 ; j += 1)
        {
            if (board[0][j] == 0)
                return false;
        }
        return true;
    }

    /**
     * Returns a list of column indices that are not yet full.
     * @return an ArrayList of valid column indices
     */
    ArrayList<Integer> getValidColumns ()
    {
        ArrayList<Integer> cols = new ArrayList<>();
        for (int j = 0 ; j < 7 ; j += 1)
        {
            if (!isColumnFull(j))
                cols.add(j);
        }
        return cols;
    }

    /**
     * Returns a deep copy of the board array.
     * @return a new 6x7 array with the same values
     */
    int [] [] copyBoard ()
    {
        int [] [] copy = new int [6] [7];
        for (int i = 0 ; i < 6 ; i += 1)
        {
            for (int j = 0 ; j < 7 ; j += 1)
            {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    // ----------------------------------------------------------------
    // Win and draw detection
    // ----------------------------------------------------------------

    /**
     * Checks whether the given piece has four in a row anywhere
     * on the board (horizontal, vertical, or diagonal).
     * @param piece the piece to check (1 or 2)
     * @return true if the piece has a winning line
     */
    boolean checkWin (int piece)
    {
        // Horizontal
        for (int r = 0 ; r < 6 ; r += 1)
        {
            for (int c = 0 ; c < 4 ; c += 1)
            {
                if (board[r][c] == piece && board[r][c + 1] == piece
                        && board[r][c + 2] == piece && board[r][c + 3] == piece)
                    return true;
            }
        }

        // Vertical
        for (int r = 0 ; r < 3 ; r += 1)
        {
            for (int c = 0 ; c < 7 ; c += 1)
            {
                if (board[r][c] == piece && board[r + 1][c] == piece
                        && board[r + 2][c] == piece && board[r + 3][c] == piece)
                    return true;
            }
        }

        // Diagonal (top-left to bottom-right)
        for (int r = 0 ; r < 3 ; r += 1)
        {
            for (int c = 0 ; c < 4 ; c += 1)
            {
                if (board[r][c] == piece && board[r + 1][c + 1] == piece
                        && board[r + 2][c + 2] == piece && board[r + 3][c + 3] == piece)
                    return true;
            }
        }

        // Diagonal (top-right to bottom-left)
        for (int r = 0 ; r < 3 ; r += 1)
        {
            for (int c = 3 ; c < 7 ; c += 1)
            {
                if (board[r][c] == piece && board[r + 1][c - 1] == piece
                        && board[r + 2][c - 2] == piece && board[r + 3][c - 3] == piece)
                    return true;
            }
        }

        return false;
    }

    /**
     * Checks whether the game is a draw (board full, no winner).
     * @return true if the board is full and neither player has won
     */
    boolean checkDraw ()
    {
        return isBoardFull() && !checkWin(1) && !checkWin(2);
    }
}
