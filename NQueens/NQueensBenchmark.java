public class NQueensBenchmark {

    public static void main(String[] args) {

        System.out.printf("%-4s | %-10s | %-7s | %-10s | %-7s | %-13s | %-10s%n",
                "N", "BT-Nodes", "BT-Ms", "FC-Nodes", "FC-Ms", "MC-Nodes(avg)", "MC-Ns(avg)");
        System.out.println("-".repeat(75));

        for (int n = 8; n <= 20; n++) {

            // BT — single run
            NQueens bt = new NQueens(n);
            bt.solveBacktracking();

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

            System.out.printf("%-4d | %-10d | %-7d | %-10d | %-7d | %-13d | %-10d%n",
                    n,
                    bt.nodesExplored, bt.timeElapsedMs,
                    fc.nodesExplored, fc.timeElapsedMs,
                    avgNodes, avgTimeNs);
        }
    }
}
