import java.util.ArrayList;

/**
 * Connect 4 AI using Minimax with Alpha-Beta pruning.
 * The AI plays as piece 2 (yellow) and the human plays as piece 1 (red).
 */
public class AI
{
    private static final int AI_PIECE    = 2;
    private static final int HUMAN_PIECE = 1;
    private static final int EMPTY       = 0;
    private static final int ROWS        = 6;
    private static final int COLS        = 7;

    /**
     * Move ordering — centre columns first for better pruning.
     */
    private static final int [] COLUMN_ORDER = {3, 2, 4, 1, 5, 0, 6};

    // ----------------------------------------------------------------
    // Public entry point
    // ----------------------------------------------------------------

    /**
     * Returns the best column for the AI to play in.
     * @param board the current 6x7 board state
     * @param depth the search depth for minimax
     * @return the column index (0-6) of the best move
     */
    int getBestMove (int [] [] board, int depth)
    {
        int bestCol   = 3;
        int bestScore = Integer.MIN_VALUE;

        for (int idx = 0 ; idx < COLUMN_ORDER.length ; idx += 1)
        {
            int col = COLUMN_ORDER[idx];
            if (isColumnFull(board, col))
                continue;

            int row = dropPiece(board, col, AI_PIECE);
            int score = minimax(board, depth - 1, Integer.MIN_VALUE,
                    Integer.MAX_VALUE, false);
            undoPiece(board, col, row);

            if (score > bestScore)
            {
                bestScore = score;
                bestCol   = col;
            }
        }
        return bestCol;
    }

    // ----------------------------------------------------------------
    // Minimax with Alpha-Beta pruning
    // ----------------------------------------------------------------

    /**
     * Recursive minimax search with alpha-beta pruning.
     * @param board        the current board state
     * @param depth        remaining search depth
     * @param alpha        the best score the maximiser can guarantee
     * @param beta         the best score the minimiser can guarantee
     * @param isMaximising true if the current player is the AI (maximiser)
     * @return the heuristic score of the board position
     */
    private int minimax (int [] [] board, int depth, int alpha, int beta,
                         boolean isMaximising)
    {
        if (checkWin(board, AI_PIECE))
            return 100000 + depth;
        if (checkWin(board, HUMAN_PIECE))
            return -100000 - depth;
        if (isBoardFull(board))
            return 0;
        if (depth == 0)
            return scoreBoard(board);

        if (isMaximising)
        {
            int maxEval = Integer.MIN_VALUE;
            for (int idx = 0 ; idx < COLUMN_ORDER.length ; idx += 1)
            {
                int col = COLUMN_ORDER[idx];
                if (isColumnFull(board, col))
                    continue;

                int row = dropPiece(board, col, AI_PIECE);
                int eval = minimax(board, depth - 1, alpha, beta, false);
                undoPiece(board, col, row);

                if (eval > maxEval)
                    maxEval = eval;
                if (eval > alpha)
                    alpha = eval;
                if (alpha >= beta)
                    break;
            }
            return maxEval;
        }
        else
        {
            int minEval = Integer.MAX_VALUE;
            for (int idx = 0 ; idx < COLUMN_ORDER.length ; idx += 1)
            {
                int col = COLUMN_ORDER[idx];
                if (isColumnFull(board, col))
                    continue;

                int row = dropPiece(board, col, HUMAN_PIECE);
                int eval = minimax(board, depth - 1, alpha, beta, true);
                undoPiece(board, col, row);

                if (eval < minEval)
                    minEval = eval;
                if (eval < beta)
                    beta = eval;
                if (alpha >= beta)
                    break;
            }
            return minEval;
        }
    }

    // ----------------------------------------------------------------
    // Board scoring heuristic
    // ----------------------------------------------------------------

    /**
     * Evaluates the board position from the AI's perspective.
     * Scans every window of 4 consecutive cells in all four directions
     * and applies a bonus for pieces in the centre column.
     * @param board the current board state
     * @return a heuristic score (positive = good for AI)
     */
    private int scoreBoard (int [] [] board)
    {
        int score = 0;

        // Centre column bonus
        for (int r = 0 ; r < ROWS ; r += 1)
        {
            if (board[r][3] == AI_PIECE)
                score += 3;
        }

        // Horizontal windows
        for (int r = 0 ; r < ROWS ; r += 1)
        {
            for (int c = 0 ; c < 4 ; c += 1)
            {
                int [] window = {board[r][c], board[r][c + 1],
                        board[r][c + 2], board[r][c + 3]};
                score += scoreWindow(window);
            }
        }

        // Vertical windows
        for (int r = 0 ; r < 3 ; r += 1)
        {
            for (int c = 0 ; c < COLS ; c += 1)
            {
                int [] window = {board[r][c], board[r + 1][c],
                        board[r + 2][c], board[r + 3][c]};
                score += scoreWindow(window);
            }
        }

        // Diagonal (top-left to bottom-right)
        for (int r = 0 ; r < 3 ; r += 1)
        {
            for (int c = 0 ; c < 4 ; c += 1)
            {
                int [] window = {board[r][c], board[r + 1][c + 1],
                        board[r + 2][c + 2], board[r + 3][c + 3]};
                score += scoreWindow(window);
            }
        }

        // Diagonal (top-right to bottom-left)
        for (int r = 0 ; r < 3 ; r += 1)
        {
            for (int c = 3 ; c < COLS ; c += 1)
            {
                int [] window = {board[r][c], board[r + 1][c - 1],
                        board[r + 2][c - 2], board[r + 3][c - 3]};
                score += scoreWindow(window);
            }
        }

        return score;
    }

    /**
     * Scores a single window of 4 cells.
     * @param window an array of 4 cell values
     * @return the score contribution of this window
     */
    private int scoreWindow (int [] window)
    {
        int aiCount    = 0;
        int humanCount = 0;
        int emptyCount = 0;

        for (int i = 0 ; i < 4 ; i += 1)
        {
            if (window[i] == AI_PIECE)
                aiCount += 1;
            else if (window[i] == HUMAN_PIECE)
                humanCount += 1;
            else
                emptyCount += 1;
        }

        if (aiCount == 4)
            return 1000;
        if (aiCount == 3 && emptyCount == 1)
            return 5;
        if (aiCount == 2 && emptyCount == 2)
            return 2;
        if (humanCount == 3 && emptyCount == 1)
            return -4;

        return 0;
    }

    // ----------------------------------------------------------------
    // Board manipulation helpers
    // ----------------------------------------------------------------

    /**
     * Drops a piece into the given column on the board array.
     * @param board the board array
     * @param col   the column index (0-6)
     * @param piece the piece to drop (1 or 2)
     * @return the row the piece was placed in
     */
    private int dropPiece (int [] [] board, int col, int piece)
    {
        for (int i = ROWS - 1 ; i >= 0 ; i -= 1)
        {
            if (board[i][col] == EMPTY)
            {
                board[i][col] = piece;
                return i;
            }
        }
        return -1;
    }

    /**
     * Removes a piece from the given row and column.
     * @param board the board array
     * @param col   the column index
     * @param row   the row index
     */
    private void undoPiece (int [] [] board, int col, int row)
    {
        board[row][col] = EMPTY;
    }

    /**
     * Checks whether the given column is full.
     * @param board the board array
     * @param col   the column index
     * @return true if the top cell of the column is occupied
     */
    private boolean isColumnFull (int [] [] board, int col)
    {
        return board[0][col] != EMPTY;
    }

    /**
     * Checks whether every cell on the board is filled.
     * @param board the board array
     * @return true if the board is completely full
     */
    private boolean isBoardFull (int [] [] board)
    {
        for (int j = 0 ; j < COLS ; j += 1)
        {
            if (board[0][j] == EMPTY)
                return false;
        }
        return true;
    }

    /**
     * Checks whether the given piece has four in a row.
     * @param board the board array
     * @param piece the piece to check (1 or 2)
     * @return true if the piece has a winning line
     */
    boolean checkWin (int [] [] board, int piece)
    {
        // Horizontal
        for (int r = 0 ; r < ROWS ; r += 1)
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
            for (int c = 0 ; c < COLS ; c += 1)
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
            for (int c = 3 ; c < COLS ; c += 1)
            {
                if (board[r][c] == piece && board[r + 1][c - 1] == piece
                        && board[r + 2][c - 2] == piece && board[r + 3][c - 3] == piece)
                    return true;
            }
        }

        return false;
    }
}
