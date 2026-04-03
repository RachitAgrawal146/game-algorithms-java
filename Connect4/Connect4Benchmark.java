import java.util.ArrayList;
import java.util.List;

/**
 * Connect4Benchmark.java
 *
 * Empirical analysis of Minimax with Alpha-Beta pruning for Connect4.
 *
 * Measures:
 *   1. Nodes searched per move at depths 2, 4, 6, 8
 *   2. Alpha-Beta pruning efficiency (% of tree pruned)
 *   3. How pruning efficiency varies by game phase (early/mid/late)
 *   4. Time per move at each depth
 *
 * Methodology: AI plays against itself at each depth. For pruning
 * comparison, each position is evaluated twice — once with pruning
 * (alpha-beta bounds) and once without (alpha = -INF, beta = +INF
 * never tightened).
 */
public class Connect4Benchmark
{
    static final int ROWS = 6;
    static final int COLS = 7;
    static final int AI_PIECE = 2;
    static final int HUMAN_PIECE = 1;
    static final int EMPTY = 0;
    static final int [] MOVE_ORDER = {3, 2, 4, 1, 5, 0, 6};

    static long nodesWithPruning;
    static long nodesWithoutPruning;

    // -----------------------------------------------------------------------
    // Main benchmark
    // -----------------------------------------------------------------------

    public static void main (String [] args)
    {
        System.out.println("=== Connect4 Benchmark: Minimax with Alpha-Beta Pruning ===");
        System.out.println();

        depthBenchmark();
        System.out.println();
        pruningEfficiencyByPhase();
        System.out.println();
        pruningComparisonTable();
    }

    // -----------------------------------------------------------------------
    // Benchmark 1: Depth vs Performance
    // -----------------------------------------------------------------------

    static void depthBenchmark ()
    {
        System.out.println("--- Depth vs Performance (AI vs AI, averaged over full game) ---");
        System.out.printf("%-6s | %-12s | %-10s | %-10s%n",
                "Depth", "Avg Nodes", "Avg Ms", "Total Moves");
        System.out.println("-".repeat(50));

        for (int depth : new int [] {2, 4, 6, 8})
        {
            int [] [] board = new int [ROWS] [COLS];
            long totalNodes = 0;
            long totalTimeNs = 0;
            int moveCount = 0;
            int currentPiece = AI_PIECE;

            while (!checkWin(board, AI_PIECE) && !checkWin(board, HUMAN_PIECE)
                    && !isFull(board))
            {
                long t0 = System.nanoTime();
                nodesWithPruning = 0;
                int col = getBestMoveTracked(board, depth, true, currentPiece);
                long elapsed = System.nanoTime() - t0;

                int row = getNextRow(board, col);
                if (row >= 0) board[row][col] = currentPiece;

                totalNodes += nodesWithPruning;
                totalTimeNs += elapsed;
                moveCount += 1;
                currentPiece = (currentPiece == AI_PIECE) ? HUMAN_PIECE : AI_PIECE;
            }

            long avgNodes = (moveCount > 0) ? totalNodes / moveCount : 0;
            double avgMs = (moveCount > 0) ? (totalTimeNs / moveCount) / 1_000_000.0 : 0;

            System.out.printf("%-6d | %-12d | %-10.2f | %-10d%n",
                    depth, avgNodes, avgMs, moveCount);
        }
    }

    // -----------------------------------------------------------------------
    // Benchmark 2: Pruning Efficiency by Game Phase
    // -----------------------------------------------------------------------

    static void pruningEfficiencyByPhase ()
    {
        System.out.println("--- Alpha-Beta Pruning Efficiency by Game Phase (depth=6) ---");
        System.out.printf("%-8s | %-6s | %-14s | %-14s | %-10s%n",
                "Phase", "Moves", "With Pruning", "Without", "% Pruned");
        System.out.println("-".repeat(65));

        int [] [] board = new int [ROWS] [COLS];
        int currentPiece = AI_PIECE;
        int moveNum = 0;

        long earlyWith = 0, earlyWithout = 0;
        int earlyCount = 0;
        long midWith = 0, midWithout = 0;
        int midCount = 0;
        long lateWith = 0, lateWithout = 0;
        int lateCount = 0;

        while (!checkWin(board, AI_PIECE) && !checkWin(board, HUMAN_PIECE)
                && !isFull(board))
        {
            nodesWithPruning = 0;
            int col1 = getBestMoveTracked(board, 6, true, currentPiece);
            long withP = nodesWithPruning;

            nodesWithoutPruning = 0;
            getBestMoveTracked(board, 6, false, currentPiece);
            long withoutP = nodesWithoutPruning;

            if (moveNum < 14)
            {
                earlyWith += withP;
                earlyWithout += withoutP;
                earlyCount += 1;
            }
            else if (moveNum < 28)
            {
                midWith += withP;
                midWithout += withoutP;
                midCount += 1;
            }
            else
            {
                lateWith += withP;
                lateWithout += withoutP;
                lateCount += 1;
            }

            int row = getNextRow(board, col1);
            if (row >= 0) board[row][col1] = currentPiece;
            currentPiece = (currentPiece == AI_PIECE) ? HUMAN_PIECE : AI_PIECE;
            moveNum += 1;
        }

        printPhaseRow("Early", earlyCount, earlyWith, earlyWithout);
        printPhaseRow("Mid", midCount, midWith, midWithout);
        printPhaseRow("Late", lateCount, lateWith, lateWithout);

        long totalWith = earlyWith + midWith + lateWith;
        long totalWithout = earlyWithout + midWithout + lateWithout;
        printPhaseRow("Overall", earlyCount + midCount + lateCount, totalWith, totalWithout);
    }

    static void printPhaseRow (String phase, int count, long withP, long withoutP)
    {
        if (count == 0) return;
        long avgWith = withP / count;
        long avgWithout = withoutP / count;
        double pct = (withoutP > 0) ? (1.0 - (double) withP / withoutP) * 100 : 0;
        System.out.printf("%-8s | %-6d | %-14d | %-14d | %-10.1f%n",
                phase, count, avgWith, avgWithout, pct);
    }

    // -----------------------------------------------------------------------
    // Benchmark 3: Pruning Comparison Table
    // -----------------------------------------------------------------------

    static void pruningComparisonTable ()
    {
        System.out.println("--- Nodes With vs Without Alpha-Beta (single mid-game position) ---");
        System.out.printf("%-6s | %-14s | %-14s | %-10s%n",
                "Depth", "With A-B", "Without A-B", "% Pruned");
        System.out.println("-".repeat(50));

        // Set up a mid-game position
        int [] [] board = new int [ROWS] [COLS];
        int [] moves = {3, 3, 4, 2, 5, 1, 3, 4, 2, 4, 2, 5};
        int piece = 1;
        for (int col : moves)
        {
            int row = getNextRow(board, col);
            if (row >= 0) board[row][col] = piece;
            piece = (piece == 1) ? 2 : 1;
        }

        for (int depth : new int [] {2, 4, 6, 8, 10})
        {
            nodesWithPruning = 0;
            getBestMoveTracked(board, depth, true, AI_PIECE);
            long withP = nodesWithPruning;

            nodesWithoutPruning = 0;
            getBestMoveTracked(board, depth, false, AI_PIECE);
            long withoutP = nodesWithoutPruning;

            double pct = (withoutP > 0) ? (1.0 - (double) withP / withoutP) * 100 : 0;
            System.out.printf("%-6d | %-14d | %-14d | %-10.1f%n",
                    depth, withP, withoutP, pct);
        }
    }

    // -----------------------------------------------------------------------
    // Tracked minimax (with and without pruning)
    // -----------------------------------------------------------------------

    static int getBestMoveTracked (int [] [] board, int depth, boolean usePruning, int aiPiece)
    {
        int humanPiece = (aiPiece == AI_PIECE) ? HUMAN_PIECE : AI_PIECE;
        int bestScore = Integer.MIN_VALUE;
        int bestCol = 3;

        for (int col : MOVE_ORDER)
        {
            if (board[0][col] != EMPTY) continue;
            int row = getNextRow(board, col);
            board[row][col] = aiPiece;

            int score;
            if (usePruning)
            {
                nodesWithPruning += 1;
                score = minimaxTracked(board, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE,
                        false, usePruning, aiPiece, humanPiece);
            }
            else
            {
                nodesWithoutPruning += 1;
                score = minimaxTracked(board, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE,
                        false, usePruning, aiPiece, humanPiece);
            }

            board[row][col] = EMPTY;
            if (score > bestScore) { bestScore = score; bestCol = col; }
        }
        return bestCol;
    }

    static int minimaxTracked (int [] [] board, int depth, int alpha, int beta,
                               boolean isMax, boolean usePruning, int aiPiece, int humanPiece)
    {
        if (checkWin(board, aiPiece))    return 100000 + depth;
        if (checkWin(board, humanPiece)) return -100000 - depth;
        if (isFull(board))               return 0;
        if (depth == 0)                  return scoreBoardStatic(board, aiPiece, humanPiece);

        if (isMax)
        {
            int maxE = Integer.MIN_VALUE;
            for (int col : MOVE_ORDER)
            {
                if (board[0][col] != EMPTY) continue;
                int row = getNextRow(board, col);
                board[row][col] = aiPiece;
                if (usePruning) nodesWithPruning += 1;
                else nodesWithoutPruning += 1;
                int ev = minimaxTracked(board, depth - 1, alpha, beta, false, usePruning, aiPiece, humanPiece);
                board[row][col] = EMPTY;
                if (ev > maxE) maxE = ev;
                if (usePruning)
                {
                    if (ev > alpha) alpha = ev;
                    if (alpha >= beta) break;
                }
            }
            return maxE;
        }
        else
        {
            int minE = Integer.MAX_VALUE;
            for (int col : MOVE_ORDER)
            {
                if (board[0][col] != EMPTY) continue;
                int row = getNextRow(board, col);
                board[row][col] = humanPiece;
                if (usePruning) nodesWithPruning += 1;
                else nodesWithoutPruning += 1;
                int ev = minimaxTracked(board, depth - 1, alpha, beta, true, usePruning, aiPiece, humanPiece);
                board[row][col] = EMPTY;
                if (ev < minE) minE = ev;
                if (usePruning)
                {
                    if (ev < beta) beta = ev;
                    if (alpha >= beta) break;
                }
            }
            return minE;
        }
    }

    // -----------------------------------------------------------------------
    // Board helpers
    // -----------------------------------------------------------------------

    static int getNextRow (int [] [] board, int col)
    {
        for (int r = ROWS - 1 ; r >= 0 ; r -= 1)
            if (board[r][col] == EMPTY) return r;
        return -1;
    }

    static boolean isFull (int [] [] board)
    {
        for (int c = 0 ; c < COLS ; c += 1)
            if (board[0][c] == EMPTY) return false;
        return true;
    }

    static boolean checkWin (int [] [] board, int piece)
    {
        for (int r = 0 ; r < ROWS ; r += 1)
            for (int c = 0 ; c < 4 ; c += 1)
                if (board[r][c]==piece && board[r][c+1]==piece && board[r][c+2]==piece && board[r][c+3]==piece)
                    return true;
        for (int r = 0 ; r < 3 ; r += 1)
            for (int c = 0 ; c < COLS ; c += 1)
                if (board[r][c]==piece && board[r+1][c]==piece && board[r+2][c]==piece && board[r+3][c]==piece)
                    return true;
        for (int r = 0 ; r < 3 ; r += 1)
            for (int c = 0 ; c < 4 ; c += 1)
                if (board[r][c]==piece && board[r+1][c+1]==piece && board[r+2][c+2]==piece && board[r+3][c+3]==piece)
                    return true;
        for (int r = 0 ; r < 3 ; r += 1)
            for (int c = 3 ; c < COLS ; c += 1)
                if (board[r][c]==piece && board[r+1][c-1]==piece && board[r+2][c-2]==piece && board[r+3][c-3]==piece)
                    return true;
        return false;
    }

    static int scoreBoardStatic (int [] [] board, int aiPiece, int humanPiece)
    {
        int score = 0;
        for (int r = 0 ; r < ROWS ; r += 1)
            if (board[r][3] == aiPiece) score += 3;

        for (int r = 0 ; r < ROWS ; r += 1)
            for (int c = 0 ; c < 4 ; c += 1)
                score += sw(board[r][c], board[r][c+1], board[r][c+2], board[r][c+3], aiPiece, humanPiece);
        for (int r = 0 ; r < 3 ; r += 1)
            for (int c = 0 ; c < COLS ; c += 1)
                score += sw(board[r][c], board[r+1][c], board[r+2][c], board[r+3][c], aiPiece, humanPiece);
        for (int r = 0 ; r < 3 ; r += 1)
            for (int c = 0 ; c < 4 ; c += 1)
                score += sw(board[r][c], board[r+1][c+1], board[r+2][c+2], board[r+3][c+3], aiPiece, humanPiece);
        for (int r = 0 ; r < 3 ; r += 1)
            for (int c = 3 ; c < COLS ; c += 1)
                score += sw(board[r][c], board[r+1][c-1], board[r+2][c-2], board[r+3][c-3], aiPiece, humanPiece);
        return score;
    }

    static int sw (int a, int b, int c, int d, int aiPiece, int humanPiece)
    {
        int ai = 0, hu = 0, em = 0;
        for (int v : new int [] {a, b, c, d})
        {
            if (v == aiPiece) ai += 1;
            else if (v == humanPiece) hu += 1;
            else em += 1;
        }
        if (ai == 4) return 1000;
        if (ai == 3 && em == 1) return 5;
        if (ai == 2 && em == 2) return 2;
        if (hu == 3 && em == 1) return -4;
        return 0;
    }
}
