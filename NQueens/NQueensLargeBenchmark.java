public class NQueensLargeBenchmark {

    public static void main(String[] args) {

        System.out.printf("%-4s | %-10s | %-7s | %-13s | %-10s%n",
                "N", "FC-Nodes", "FC-Ms", "MC-Nodes(avg)", "MC-Ns(avg)");
        System.out.println("-".repeat(55));

        for (int n = 8; n <= 30; n++) {

            // FC — single run
            NQueens fc = new NQueens(n);
            fc.solveForwardChecking();

            // MC — averaged over 100 trials
            int trials = 100;
            long totalNodes  = 0;
            long totalTimeNs = 0;

            for (int t = 0; t < trials; t++) {
                NQueens mc = new NQueens(n);
                mc.solveMinConflicts(n * 500);
                totalNodes  += mc.nodesExplored;
                totalTimeNs += mc.timeElapsedNs;
            }

            long avgNodes  = totalNodes  / trials;
            long avgTimeNs = totalTimeNs / trials;

            System.out.printf("%-4d | %-10d | %-7d | %-13d | %-10d%n",
                    n,
                    fc.nodesExplored, fc.timeElapsedMs,
                    avgNodes, avgTimeNs);
        }
    }
}
