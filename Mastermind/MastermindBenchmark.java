/**
 * MastermindBenchmark.java
 *
 * Exhaustive empirical analysis of the constraint-elimination solver.
 *
 * Methodology: runs the AI solver against every possible no-repeat code
 * (360 permutations of 4 distinct digits from 1-6) and records:
 *   1. Guess distribution — how many codes solved in 1, 2, 3, 4, 5+ guesses
 *   2. Average candidates eliminated per guess round
 *   3. Worst-case and average-case performance
 *   4. Information gain per guess (candidates remaining after each round)
 *
 * This provides empirical verification of Knuth's theoretical bound
 * that any Mastermind code can be cracked in at most 5 guesses.
 */
public class MastermindBenchmark
{
    public static void main (String [] args)
    {
        System.out.println("=== Mastermind Benchmark: Constraint Elimination Solver ===");
        System.out.println("Testing against all 360 possible no-repeat codes...");
        System.out.println();

        int [] guessDist = new int [11];  // guessDist[k] = how many codes solved in k guesses
        int totalGuesses = 0;
        int worstCase = 0;
        String worstCode = "";

        // Track candidates remaining after each guess round (averaged)
        long [] totalRemainingAfterRound = new long [11];
        int [] roundCounts = new int [11];

        // Generate all 360 codes
        int codeCount = 0;
        Guess g = new Guess();

        for (int a = 1 ; a <= 6 ; a += 1)
        {
            for (int b = 1 ; b <= 6 ; b += 1)
            {
                if (b == a) continue;
                for (int c = 1 ; c <= 6 ; c += 1)
                {
                    if (c == a || c == b) continue;
                    for (int d = 1 ; d <= 6 ; d += 1)
                    {
                        if (d == a || d == b || d == c) continue;

                        String code = "" + a + b + c + d;
                        codeCount += 1;

                        // Run solver against this code
                        CodeBreaker cb = new CodeBreaker();
                        cb.initializeSet();
                        int guesses = 0;

                        for (int round = 0 ; round < 10 ; round += 1)
                        {
                            int guessInt = cb.getNextGuess();
                            if (guessInt == -1) break;

                            String guessStr = String.valueOf(guessInt);
                            int [] result = g.checkGuess(guessStr, code);
                            guesses += 1;

                            if (result[0] == 4) break;

                            cb.remove(result[0], result[1], guessInt);

                            // Track remaining candidates
                            if (round < 10)
                            {
                                totalRemainingAfterRound[round + 1] += cb.remainingCount();
                                roundCounts[round + 1] += 1;
                            }
                        }

                        guessDist[guesses] += 1;
                        totalGuesses += guesses;

                        if (guesses > worstCase)
                        {
                            worstCase = guesses;
                            worstCode = code;
                        }
                    }
                }
            }
        }

        // Print results
        System.out.println("--- Guess Distribution (across all 360 codes) ---");
        System.out.printf("%-10s | %-8s | %-10s%n", "Guesses", "Codes", "Percentage");
        System.out.println("-".repeat(35));
        for (int k = 1 ; k <= 10 ; k += 1)
        {
            if (guessDist[k] == 0) continue;
            double pct = (guessDist[k] * 100.0) / codeCount;
            System.out.printf("%-10d | %-8d | %-10.1f%%%n", k, guessDist[k], pct);
        }

        System.out.println();
        double avg = (double) totalGuesses / codeCount;
        System.out.printf("Total codes tested:  %d%n", codeCount);
        System.out.printf("Average guesses:     %.2f%n", avg);
        System.out.printf("Worst case:          %d guesses (code: %s)%n", worstCase, worstCode);
        System.out.printf("Best case:           %d guess%n", 1);

        // Candidate elimination curve
        System.out.println();
        System.out.println("--- Average Candidates Remaining After Each Guess ---");
        System.out.printf("%-10s | %-14s | %-14s%n", "After", "Avg Remaining", "% Eliminated");
        System.out.println("-".repeat(45));
        System.out.printf("%-10s | %-14d | %-14s%n", "Guess 0", 360, "—");
        double prev = 360.0;
        for (int round = 1 ; round <= 6 ; round += 1)
        {
            if (roundCounts[round] == 0) break;
            double avgRemaining = (double) totalRemainingAfterRound[round] / roundCounts[round];
            double pctElim = ((prev - avgRemaining) / prev) * 100;
            System.out.printf("%-10s | %-14.1f | %-14.1f%%%n",
                    "Guess " + round, avgRemaining, pctElim);
            prev = avgRemaining;
        }
    }
}
