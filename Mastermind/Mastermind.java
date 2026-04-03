import java.util.Scanner;

/**
 * Mastermind game — human play or AI solver mode.
 * The secret code is 4 distinct digits from 1-6.
 * The player has 10 guesses to crack the code.
 */
public class Mastermind
{
    public static void main (String [] args)
    {
        Scanner sc = new Scanner(System.in);
        Guess g    = new Guess();
        Board b    = new Board();
        CodeGen c  = new CodeGen();
        String code = c.codeGenNoRep();

        System.out.println("=== Mastermind ===");
        System.out.println("Select mode:");
        System.out.println("  1 — Human player");
        System.out.println("  2 — AI solver (watch the computer crack the code)");
        System.out.print("Choice: ");
        String modeInput = sc.nextLine().trim();

        if (modeInput.equals("2"))
        {
            playAI(code, g, b);
        }
        else
        {
            playHuman(code, g, b, sc);
        }

        sc.close();
    }

    // ----------------------------------------------------------------
    // Human player mode
    // ----------------------------------------------------------------

    /**
     * Lets a human player guess the code with up to 10 attempts.
     */
    static void playHuman (String code, Guess g, Board b, Scanner sc)
    {
        System.out.println("Enter your guess in numbers where :");
        System.out.println("1 => RED :");
        System.out.println("2 => GREEN :");
        System.out.println("3 => YELLOW :");
        System.out.println("4 => BLUE :");
        System.out.println("5 => PURPLE :");
        System.out.println("6 => CYAN :");

        boolean solved = false;
        for (int n = 0 ; n < 10 ; n += 1)
        {
            String guess = sc.nextLine().trim();
            int [] result = g.checkGuess(guess, code);
            int black = result[0];
            int white = result[1];

            b.printBoard(black, white, guess);

            if (black == 4)
            {
                System.out.println("Congratulations, You have cracked the code!!");
                solved = true;
                break;
            }
        }

        if (!solved)
        {
            System.out.println("Oops, Better luck next time :( ");
            System.out.println("The correct code was " + code);
        }
    }

    // ----------------------------------------------------------------
    // AI solver mode
    // ----------------------------------------------------------------

    /**
     * Runs the constraint-elimination AI solver against the secret code.
     * Displays each guess and the feedback received.
     */
    static void playAI (String code, Guess g, Board b)
    {
        CodeBreaker cb = new CodeBreaker();
        cb.initializeSet();

        System.out.println("AI is solving...");
        System.out.println();

        for (int n = 0 ; n < 10 ; n += 1)
        {
            int guessInt = cb.getNextGuess();
            if (guessInt == -1)
            {
                System.out.println("AI ran out of candidates — something went wrong.");
                break;
            }

            String guess = String.valueOf(guessInt);
            int [] result = g.checkGuess(guess, code);
            int black = result[0];
            int white = result[1];

            System.out.println("Guess " + (n + 1) + ": " + guess
                    + "  (" + cb.remainingCount() + " candidates remaining)");
            b.printBoard(black, white, guess);

            if (black == 4)
            {
                System.out.println("AI cracked the code in " + (n + 1) + " guesses!");
                return;
            }

            cb.remove(black, white, guessInt);
        }

        System.out.println("AI could not crack the code within 10 guesses.");
        System.out.println("The correct code was " + code);
    }
}
