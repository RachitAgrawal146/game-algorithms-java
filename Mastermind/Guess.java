/**
 * Evaluates a Mastermind guess against the secret code.
 * Uses a two-pass algorithm to correctly handle repeated colours.
 */
public class Guess
{
    /**
     * Checks a guess against the code and returns the number of
     * black (exact match) and white (right colour, wrong position) pegs.
     *
     * Pass 1 counts blacks (exact position matches).
     * Pass 2 counts whites (correct colour in wrong position),
     * using boolean arrays to prevent double-counting.
     *
     * @param guess the 4-digit guess string
     * @param code  the 4-digit secret code string
     * @return an array {black, white}
     */
    int [] checkGuess (String guess, String code)
    {
        int black = 0;
        int white = 0;
        boolean [] codeUsed  = new boolean [4];
        boolean [] guessUsed = new boolean [4];

        // First pass — count blacks (exact matches)
        for (int i = 0 ; i < 4 ; i += 1)
        {
            if (guess.charAt(i) == code.charAt(i))
            {
                black += 1;
                codeUsed[i]  = true;
                guessUsed[i] = true;
            }
        }

        // Second pass — count whites (right colour, wrong position)
        for (int i = 0 ; i < 4 ; i += 1)
        {
            if (guessUsed[i]) continue;
            for (int j = 0 ; j < 4 ; j += 1)
            {
                if (codeUsed[j]) continue;
                if (guess.charAt(i) == code.charAt(j))
                {
                    white += 1;
                    codeUsed[j] = true;
                    break;
                }
            }
        }
        return new int [] {black, white};
    }
}
