# Game Algorithms — A Comparative Study of Search and Constraint-Solving

> *How does the structure of a problem's constraint space affect the efficiency of different search-based solvers — and can we measure this difference empirically?*

Independent research project exploring algorithm behaviour across three classes of combinatorial problems. Each project implements multiple solving strategies, runs controlled experiments, and measures performance quantitatively.

**[Live Algorithm Visualizer →](https://rachitagrawal146.github.io/game-algorithms-java/Visualizer/NQueensVisualizer.html)**
*Step through Backtracking, Forward Checking, and Min-Conflicts solving the N-Queens problem in real time. Watch domains shrink, backtracks happen, and conflicts resolve.*

---

## Research Question

Most algorithm courses teach that Forward Checking is "better" than Backtracking, and local search is "faster" than systematic search. But better and faster in what sense — and under what conditions does each claim break down?

This project uses three combinatorial game environments as formal experimental testbeds to answer that question with data rather than theory.

---

## Projects

### 1. N-Queens — Constraint Satisfaction Problem

The N-Queens problem asks: place N queens on an N×N chessboard such that no two attack each other. Three fundamentally different algorithms are implemented and compared.

**Algorithms:**
- **Backtracking** — exhaustive column-by-column search with undo on conflict
- **Forward Checking** — backtracking extended with constraint propagation: after each placement, eliminate impossible rows from future columns and prune immediately if any column runs out of options
- **Min-Conflicts** — local search heuristic: place queens randomly, then iteratively move the most-conflicted queen to the row with fewest conflicts. Averaged over 100 trials to account for stochastic variance.

**Benchmark Results (N = 8 to 30):**

| N | BT Nodes | BT Time | FC Nodes | FC Time | MC Nodes (avg) | MC Time (avg) |
|---|----------|---------|----------|---------|----------------|---------------|
| 8 | 876 | 0ms | 396 | 0ms | ~350 | ~400μs |
| 10 | 975 | 0ms | 485 | 0ms | ~450 | ~370μs |
| 14 | 26,495 | 1ms | 12,159 | 2ms | ~300 | ~450μs |
| 16 | 160,712 | 3ms | 69,928 | 8ms | ~380 | ~330μs |
| 18 | 743,229 | 13ms | 317,097 | 37ms | ~200 | ~270μs |
| 20 | 3,992,510 | 74ms | 1,687,810 | 218ms | ~220 | ~650μs |
| 22 | — | — | 15,470,169 | 973ms | ~180 | ~410μs |
| 28 | — | — | 33,652,682 | 3,950ms | ~80 | ~790μs |
| 30 | — | — | 660,589,815 | 69,989ms | ~79 | ~895μs |

**Key Findings:**

1. **FC reduces nodes explored by ~58% vs BT at every N** — constraint propagation consistently prunes just over half the search space regardless of board size.

2. **FC is 3× slower than BT at large N despite fewer nodes** — each FC node is significantly more expensive than a BT node due to domain copying and propagation overhead. Node count and wall-clock time tell different stories.

3. **MC operates in a completely different performance class** — solving N=30 in under 1ms while FC takes 70 seconds. The difference is not incremental; it is structural. MC does not search at all in the traditional sense.

4. **Both systematic solvers show irregular (non-monotonic) growth** — some N values are structurally "easy" (the solution appears early in the search tree) while others are catastrophically hard. N=19 is cheaper than N=18 for both BT and FC. This irregularity is itself a finding: algorithm performance depends not just on N but on the specific structure of the solution space at that N.

5. **MC has no worst-case guarantee** — occasionally exhausts its iteration budget and restarts. This is the fundamental tradeoff: deterministic solvers always find a solution; stochastic solvers are usually faster but never certain.

**[→ Interactive Visualizer](https://rachitagrawal146.github.io/game-algorithms-java/Visualizer/NQueensVisualizer.html)**

---

### 2. Connect4 — Adversarial Search

Two-player Connect4 with an AI opponent powered by Minimax search with Alpha-Beta pruning.

**How the AI works:**
The AI builds a game tree to a configurable depth (default: 6), assuming both players play optimally. At each node, it either maximises (AI's turn) or minimises (human's turn) the board score. Alpha-Beta pruning eliminates branches that cannot affect the final decision, typically reducing the search space by ~50%.

**Scoring function:**
Every window of 4 consecutive cells (horizontal, vertical, diagonal) is evaluated. Three-in-a-row with an open end scores +5; two-in-a-row with two open ends scores +2; opponent's three-in-a-row scores -4. Centre column control earns a positional bonus. Terminal states (wins/losses) score ±1000.

**Key observation:** At depth 6, the AI evaluates tens of thousands of positions per move and plays at a strong amateur level. At depth 8, it becomes very difficult to beat. The relationship between search depth and play quality is measurable and non-linear — small depth increases have outsized effects once the AI can "see" forced sequences.

---

### 3. Mastermind — Information-Theoretic Search

Human player vs a constraint-elimination AI solver.

The AI maintains a set of all codes consistent with feedback received so far. After each guess, it eliminates any candidate that would not have produced the same black/white response. This is a direct implementation of the core idea in Donald Knuth's 1977 minimax algorithm for Mastermind, which proved that any code can be solved in at most 5 guesses.

---

### 4. 21-Card Trick — Deterministic Algorithm

A mathematical card trick that always identifies a chosen card in exactly 3 rounds. The algorithm works by controlling the position of the target card through systematic column rearrangement — demonstrating how deterministic structure can completely replace randomness in what appears to be a guessing game.

---

## Methodology Notes

**Why these three problems?** N-Queens (constraint satisfaction), Connect4 (adversarial search), and Mastermind (information-theoretic search) each represent a distinct class of combinatorial problem. Comparing algorithm behaviour across all three allows questions about *which properties of a problem make which algorithms effective* — a question with no clean textbook answer.

**On stochastic benchmarking:** Min-Conflicts results are averaged over 100 independent trials per N value. A single run is meaningless for a randomised algorithm. BT and FC require only one run as they are fully deterministic.

**On the FC time anomaly:** The counterintuitive finding that FC is slower than BT despite exploring far fewer nodes illustrates a general principle: asymptotic complexity and practical performance are not the same thing. The cost of constraint propagation per node dominates at large N.

---

## Technologies

Java — no external libraries. All data structures and algorithms implemented from scratch.

- Recursive backtracking with explicit undo
- Constraint propagation with domain copying
- Local search with random restarts and tie-breaking
- Minimax with Alpha-Beta pruning and heuristic evaluation
- Benchmarking harness with multi-trial averaging

---

## Running the Code

```bash
# N-Queens benchmark
javac NQueens/*.java
java -cp NQueens NQueensBenchmark

# Connect4 (play against AI)
javac Connect4/*.java
java -cp Connect4 Connect4

# Mastermind
javac Mastermind/*.java
java -cp Mastermind Mastermind
```

---

*Developed independently as part of a broader interest in how algorithmic choices shape outcomes in strategic environments — a theme that also runs through my research on multi-agent systems and game-theoretic behaviour.*
