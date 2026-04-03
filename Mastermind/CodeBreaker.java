import java.util.ArrayList;

/**
 * AI solver for Mastermind using constraint elimination.
 * Maintains a list of all possible codes and prunes candidates
 * after each guess based on the feedback received.
 */
public class CodeBreaker
{
    ArrayList<Integer> candidates = new ArrayList<>();

    /**
     * Initialises the candidate set with all 360 permutations
     * of 4 distinct digits chosen from 1-6.
     */
    void initializeSet ()
    {
        candidates.clear();
        for (int a = 1 ; a < 7 ; a += 1)
        {
            for (int b = 1 ; b < 7 ; b += 1)
            {
                if (b == a)
                    continue;
                for (int c = 1 ; c < 7 ; c += 1)
                {
                    if (c == b || c == a)
                        continue;
                    for (int d = 1 ; d < 7 ; d += 1)
                    {
                        if (d == a || d == b || d == c)
                            continue;
                        candidates.add(a * 1000 + b * 100 + c * 10 + d);
                    }
                }
            }
        }
    }

    /**
     * Removes all candidates that would not produce the same
     * feedback (black, white) if they were the secret code
     * and the given attempt were guessed against them.
     *
     * @param black   the number of exact-position matches
     * @param white   the number of correct-colour-wrong-position matches
     * @param attempt the guess that was made (as an integer, e.g. 1234)
     */
    void remove (int black, int white, int attempt)
    {
        Guess g = new Guess();
        String attemptStr = String.valueOf(attempt);

        ArrayList<Integer> remaining = new ArrayList<>();
        for (int i = 0 ; i < candidates.size() ; i += 1)
        {
            int [] result = g.checkGuess(attemptStr, String.valueOf(candidates.get(i)));
            if (result[0] == black && result[1] == white)
                remaining.add(candidates.get(i));
        }
        candidates = remaining;
    }

    /**
     * Returns the next guess — the first remaining candidate.
     * @return the candidate code as an integer, or -1 if no candidates remain
     */
    int getNextGuess ()
    {
        if (candidates.isEmpty())
            return -1;
        return candidates.get(0);
    }

    /**
     * Returns the number of remaining candidate codes.
     * @return the size of the candidate set
     */
    int remainingCount ()
    {
        return candidates.size();
    }
}
