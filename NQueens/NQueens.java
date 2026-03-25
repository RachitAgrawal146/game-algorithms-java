import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * This class uses 3 different algorithms to solve the NQueens Problem :
 *   1. Backtracking       — deterministic, checks all possible positions for placement and goes back to previous state if no solutions exist    
 *   2. Forward Checking   — deterministic, prunes possible branches before making placements
 *   3. Minimum Conflicts  — stochastic, places the queens randomly at first and then moves the queen with the most conflicts
 * 
 * Each method tracks the nodesExplored and timeElapsedMs.
 */

public class NQueens
{
    // Fields
    
    private final int n;                 // board size and number of queens
    private final int [] queens;         // queens[col] = row where queen sits in that column

    public long nodesExplored = 0;       // number of positions considered — reset before each solve
    public long timeElapsedMs = 0;       // solve time in milliseconds
    public long timeElapsedNs = 0;       // solve time in nanoseconds (for fast solvers like MC)


    // Constructor
    public NQueens (int n)
    {
        this.n = n;
        this.queens = new int [n];
        Arrays.fill(queens, -1);         // -1 means no queen placed in that column yet
    }

    // Methods

    void printBoard (int [] [] board)//prints the board
    {
        for (int i = 0 ; i < board.length ; i += 1)
        {
            for (int j = 0 ; j < board.length ; j += 1)
            {
                if ((board[i][j] == 0) && ((i + j) % 2 == 0))
                    System.out.print("\u25a1" + " ");
                if ((board[i][j] == 0) && ((i + j) % 2 == 1))
                    System.out.print("\u25a0" + " ");
                if (board[i][j] == 1)
                    System.out.print("\u265b" + " ");
            }
            System.out.println();
        }
    }


    int [] [] startBoard (int n)//returns a board of dimensions n x n of value all 0s
    {
        int [] [] board = new int [n] [n];
        return board;
    }


    int [] place1d (int [] arr, int c)//returns a 1-D array with the queen places in a row
    {
        int [] ans = new int [arr.length];
        for (int i = 0 ; i < arr.length ; i += 1)
        {
            if (i == c)
                ans[i] = 1;
            else
                ans[i] = arr[i];
        }
        return ans;
    }


    int [] [] placeQueen (int [] [] board, int r, int c)//returns a 2-D board with the queen placed at (r,c)
    {
        int [] [] ansBoard = new int [board.length] [board.length];
        for (int i = 0 ; i < board.length ; i += 1)
        {
            if (i == r)
                ansBoard[i] = place1d(board[r], c);
            else
                ansBoard[i] = board[i];
        }
        return ansBoard;
    }


    int countRow (int [] [] board, int row)//counts the number of queens in a given row
    {
        int sum = 0;
        for (int i = 0 ; i < board.length ; i++)
        {
            if (board[row][i] == 1)
                sum += 1;
        }
        return sum;
    }


    int countColumn (int [] [] board, int column)//counts the number of queens in a given column
    {
        int sum = 0;
        for (int i = 0 ; i < board.length ; i++)
        {
            if (board[i][column] == 1)
                sum += 1;
        }
        return sum;
    }


    
    boolean checkDiagonals (int row, int col)//checks if placing a queen at (row,col) will result in a diagonal 
    {
        for (int c = 0 ; c < col ; c++)
        {
            if (Math.abs(queens[c] - row) == Math.abs(c - col))
                return false;            
        }
        return true;                   
    }


    boolean isSafe (int row, int col) //returns true if a queen can be placed at (row,col)
    {
        for (int c = 0 ; c < col ; c++)
        {
            if (queens[c] == row)
                return false;            // same row conflict
        }
        return checkDiagonals(row, col);
    }


    int [] [] buildBoard ()//converts the queen array into a 2-D array for display
    {
        int [] [] board = startBoard(n);
        for (int col = 0 ; col < n ; col++)
        {
            if (queens[col] != -1)
                board = placeQueen(board, queens[col], col);
        }
        return board;
    }


    public void printSolution ()//prints the solution for a given board
    {
        printBoard(buildBoard());
    }

    
    
    // Solver 1 — Backtracking
    /**
     * Solves the N-Queens problem using recursive backtracking.
     * Places queens column by column. On a conflict, undoes the last
     * placement (queens[col] = -1) and tries the next row — genuine backtracking.
     * Exhaustive and guaranteed to find a solution for all N >= 1.
     * 
     * Populates nodesExplored and timeElapsedMs on completion.
     */
    public boolean solveBacktracking ()
    {
        nodesExplored = 0;
        Arrays.fill(queens, -1);
        long start = System.currentTimeMillis();
        boolean result = backtrack(0);
        timeElapsedMs = System.currentTimeMillis() - start;
        return result;
    }

    private boolean backtrack (int col)
    {
        if (col == n)
            return true;                 // all queens placed — solution found

        for (int row = 0 ; row < n ; row++)
        {
            nodesExplored++;
            if (isSafe(row, col))
            {
                queens[col] = row;                       // place queen
                if (backtrack(col + 1)) 
                    return true;     // recurse to next column
                queens[col] = -1;                        // undo — this is the backtrack step
            }
        }
        return false;                    // no valid row found — signal caller to backtrack
    }

    
    
    // Solver 2 — Forward Checking
    /**
     * Solves the N-Queens problem using backtracking with forward checking.
     * 
     * In addition to backtracking, maintains a domains[][] array where
     * domains[col][row] = true means row is still available for that column.
     * After each placement, propagate() eliminates conflicting rows from all
     * future columns. If any column's domain becomes empty, backtracks immediately
     * without exploring that branch further.
     * 
     * Explores significantly fewer nodes than plain backtracking at the cost
     * of more work per node (domain copying and propagation).
     */
    public boolean solveForwardChecking ()
    {
        nodesExplored = 0;
        Arrays.fill(queens, -1);
        boolean [] [] domains = new boolean [n] [n];
        for (boolean [] domain : domains)
            Arrays.fill(domain, true);   // initially all rows available in all columns
        long start = System.currentTimeMillis();
        boolean result = forwardCheck(0, domains);
        timeElapsedMs = System.currentTimeMillis() - start;
        return result;
    }

    private boolean forwardCheck (int col, boolean [] [] domains)
    {
        if (col == n)
            return true;

        for (int row = 0 ; row < n ; row++)
        {
            nodesExplored++;
            if (!domains[col][row])
                continue;                // this row was pruned — skip without recursing

            queens[col] = row;
            boolean [] [] newDomains = copyDomains(domains);

            if (propagate(col, row, newDomains))
            {
                if (forwardCheck(col + 1, newDomains)) return true;
            }
            queens[col] = -1;
        }
        return false;
    }

    /**
     * After placing a queen at (placedRow, placedCol), removes conflicting rows
     * from all future columns' domains. Returns false immediately if any future
     * column runs out of available rows — signals a dead end to the caller.
     */
    private boolean propagate (int placedCol, int placedRow, boolean [] [] domains)
    {
        for (int futureCol = placedCol + 1 ; futureCol < n ; futureCol++)
        {
            int dist = futureCol - placedCol;

            domains[futureCol][placedRow] = false;               // same row is forbidden

            if (placedRow - dist >= 0)
                domains[futureCol][placedRow - dist] = false;    // upper diagonal

            if (placedRow + dist < n)
                domains[futureCol][placedRow + dist] = false;    // lower diagonal

            if (domainEmpty(domains[futureCol]))
                return false;                                    // dead end — prune this branch
        }
        return true;
    }

    private boolean domainEmpty (boolean [] domain)
    {
        for (boolean b : domain)
            if (b) 
                return false;
        return true;
    }

    private boolean [] [] copyDomains (boolean [] [] domains)
    {
        boolean [] [] copy = new boolean [n] [n];
        for (int i = 0 ; i < n ; i++)
            copy[i] = Arrays.copyOf(domains[i], n);
        return copy;
    }

    // Solver 3 — Minimum Conflicts
    /**
     * Solves the N-Queens problem using the minimum conflicts local search heuristic.
     * 
     * Unlike backtracking, does not search systematically. Instead:
     *   1. Place one queen per column at a random row (almost certainly invalid)
     *   2. Find the queen with the most conflicts
     *   3. Move her to the row in her column that causes the fewest conflicts
     *   4. Repeat until no conflicts remain
     * 
     * Can get stuck in local minima (every move seems to make things worse).
     * Handles this with random restarts — if no solution found within the
     * iteration budget, throws away the board and starts fresh.
     * 
     * Tie-breaking is randomised in both steps 2 and 3 to avoid cycles.
     * Typically solves in O(n) steps on average — much faster than backtracking
     * but has no worst-case guarantee.
     * 
     */
    public boolean solveMinConflicts (int maxIterations)
    {
        nodesExplored = 0;
        long start = System.nanoTime();

        while (maxIterations > 0)
        {
            // Random initial placement — one queen per column, random row
            for (int col = 0 ; col < n ; col++)
                queens[col] = (int) (Math.random() * n);

            int result = minConflictsSearch(maxIterations);

            if (result >= 0)
            {
                long elapsed = System.nanoTime() - start;
                timeElapsedMs = elapsed / 1_000_000;
                timeElapsedNs = elapsed;
                return true;
            }
            maxIterations += result;     // subtract iterations used this restart
        }

        long elapsed = System.nanoTime() - start;
        timeElapsedMs = elapsed / 1_000_000;
        timeElapsedNs = elapsed;
        return false;
    }

    /**
     * Runs one round of min-conflicts search from the current queens[] placement.
     * Returns the number of iterations used if solved, or -maxIter if not solved.
     */
    private int minConflictsSearch (int maxIter)
    {
        for (int iter = 0 ; iter < maxIter ; iter++)
        {
            int conflictedCol = findMostConflictedCol();
            if (conflictedCol == -1)
                return iter;             // no conflicts anywhere — solution found

            queens[conflictedCol] = rowWithMinConflicts(conflictedCol);
            nodesExplored++;
        }
        return -maxIter;                 // ran out of iterations — restart needed
    }

    /**
     * Returns the column whose queen has the most conflicts with other queens.
     * If multiple columns are tied, picks one randomly to avoid cycles.
     * Returns -1 if no conflicts exist (board is solved).
     */
    private int findMostConflictedCol ()
    {
        int maxConflicts = 0;
        int worstCol = -1;

        for (int col = 0 ; col < n ; col++)
        {
            int c = countConflicts(col, queens[col]);
            if (c > maxConflicts)
            {
                maxConflicts = c;
                worstCol = col;
            }
        }

        if (worstCol == -1)
            return -1;                   // board already solved

        // Collect all columns tied at maxConflicts and pick one randomly
        List<Integer> tied = new ArrayList<>();
        for (int col = 0 ; col < n ; col++)
        {
            if (countConflicts(col, queens[col]) == maxConflicts)
                tied.add(col);
        }
        return tied.get((int) (Math.random() * tied.size()));
    }

    /**
     * Returns the row in the given column that would result in the fewest conflicts.
     * If multiple rows are tied, picks one randomly to avoid cycles.
     */
    private int rowWithMinConflicts (int col)
    {
        int minConflicts = Integer.MAX_VALUE;

        // First pass — find the minimum conflict count
        for (int row = 0 ; row < n ; row++)
        {
            int old = queens[col];
            queens[col] = row;
            int c = countConflicts(col, row);
            queens[col] = old;
            if (c < minConflicts)
                minConflicts = c;
        }

        // Second pass — collect all rows tied at that minimum
        List<Integer> tied = new ArrayList<>();
        for (int row = 0 ; row < n ; row++)
        {
            int old = queens[col];
            queens[col] = row;
            int c = countConflicts(col, row);
            queens[col] = old;
            if (c == minConflicts)
                tied.add(row);
        }
        return tied.get((int) (Math.random() * tied.size()));
    }

    /**
     * Counts how many other queens conflict with a queen placed at (col, row).
     * Checks both row conflicts and diagonal conflicts against every other column.
     */
    private int countConflicts (int col, int row)
    {
        int conflicts = 0;
        for (int c = 0 ; c < n ; c++)
        {
            if (c == col) continue;
            if (queens[c] == row) conflicts++;
            if (Math.abs(queens[c] - row) == Math.abs(c - col)) conflicts++;
        }
        return conflicts;
    }

    // -------------------------------------------------------------------------
    // Utility
    // -------------------------------------------------------------------------

    /**
     * Returns a copy of the internal queens array.
     * queens[col] = row where the queen in that column is placed.
     */
    public int [] getQueens ()
    {
        return Arrays.copyOf(queens, n);
    }
}
