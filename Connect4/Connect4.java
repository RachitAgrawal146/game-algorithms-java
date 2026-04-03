import java.util.Scanner;

/**
 * Connect 4 game — human (red) vs AI (yellow).
 * The AI uses Minimax with Alpha-Beta pruning at a configurable depth.
 */
public class Connect4
{
    public static void main (String [] args)
    {
        Scanner sc = new Scanner(System.in);
        Board b  = new Board();
        AI ai    = new AI();

        // ----------------------------------------------------------------
        // Setup
        // ----------------------------------------------------------------

        System.out.println("=== Connect 4 — You vs AI ===");
        System.out.println("You are Red (\u26AA), AI is Yellow (\u26AB)");
        System.out.print("Enter AI search depth (default 6): ");
        String depthInput = sc.nextLine().trim();
        int depth = 6;
        if (!depthInput.isEmpty())
        {
            try
            {
                depth = Integer.parseInt(depthInput);
            }
            catch (NumberFormatException e)
            {
                depth = 6;
            }
        }
        System.out.println("AI depth set to " + depth);
        System.out.println();

        Player human = new Player("You", 1, false);
        Player computer = new Player("AI", 2, true);

        boolean humanTurn = true;

        // ----------------------------------------------------------------
        // Game loop
        // ----------------------------------------------------------------

        while (true)
        {
            b.print();
            System.out.println();

            if (humanTurn)
            {
                int col = -1;
                while (true)
                {
                    System.out.print(human.getName() + " — enter column (1-7): ");
                    String input = sc.nextLine().trim();
                    try
                    {
                        col = Integer.parseInt(input) - 1;
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("Invalid input. Please enter a number between 1 and 7.");
                        continue;
                    }
                    if (col < 0 || col > 6)
                    {
                        System.out.println("Column must be between 1 and 7.");
                        continue;
                    }
                    if (b.isColumnFull(col))
                    {
                        System.out.println("That column is full. Choose another.");
                        continue;
                    }
                    break;
                }
                b.dropPiece(col, human.getPiece());
            }
            else
            {
                System.out.println(computer.getName() + " is thinking...");
                int col = ai.getBestMove(b.board, depth);
                b.dropPiece(col, computer.getPiece());
                System.out.println(computer.getName() + " plays column " + (col + 1));
            }

            System.out.println();

            // Check for win
            if (b.checkWin(1))
            {
                b.print();
                System.out.println("Congratulations, you win!");
                break;
            }
            if (b.checkWin(2))
            {
                b.print();
                System.out.println("AI wins! Better luck next time.");
                break;
            }
            if (b.checkDraw())
            {
                b.print();
                System.out.println("It's a draw!");
                break;
            }

            humanTurn = !humanTurn;
        }

        sc.close();
    }
}
