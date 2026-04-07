# Game Algorithms — A Comparative Study of Search and Constraint-Solving

> *How does the structure of a problem's constraint space affect the efficiency 
> of different search-based solvers — and can we measure this difference empirically?*

Independent research project exploring algorithm behaviour across three classes 
of combinatorial problems. Each project implements multiple solving strategies, 
runs controlled experiments, and measures performance quantitatively.

**[Live Algorithm Engine →](https://rachitagrawal146.github.io/game-algorithms-java/)** | **[Research Comparison →](https://rachitagrawal146.github.io/game-algorithms-java/compare/)**  
*Step through algorithms in real time, or explore benchmark data across all three problem classes.*

---

## Research Question

Most algorithm courses teach that Forward Checking is "better" than Backtracking, 
and local search is "faster" than systematic search. But better and faster in what 
sense — and under what conditions does each claim break down?

This project uses three combinatorial game environments as formal experimental 
testbeds to answer that question with data rather than theory.

---

## Projects

### 1. N-Queens — Constraint Satisfaction

Place N queens on an N×N board so no two attack each other. Three fundamentally 
different algorithms are compared.

**Algorithms implemented:**
- **Backtracking** — exhaustive column-by-column search with undo on conflict
- **Forward Checking** — backtracking + constraint propagation: after each 
  placement, eliminate impossible rows from future columns and prune immediately 
  if any column runs out of options
- **Min-Conflicts** — local search: place queens randomly, then iteratively 
  move the most-conflicted queen to the row with fewest conflicts. Averaged 
  over 100 trials to account for stochastic variance.

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

1. **FC reduces nodes explored by ~58% vs BT at every N** — constraint propagation 
   consistently prunes just over half the search space regardless of board size.

2. **Despite fewer nodes, FC is 3× slower than BT at large N** — each FC node 
   carries domain copying and propagation overhead. Node count and wall-clock time 
   tell different stories. Asymptotic complexity and practical performance are not 
   the same thing.

3. **MC operates in a completely different performance class** — solving N=30 in 
   under 1ms while FC takes 70 seconds. The difference is not incremental; it is 
   structural. MC does not search in the traditional sense.

4. **Both systematic solvers show irregular non-monotonic growth** — N=19 is cheaper 
   than N=18 for both BT and FC. Algorithm performance depends not just on N but on 
   the specific structure of the solution space at that N.

5. **MC has no worst-case guarantee** — occasionally exhausts its iteration budget 
   and restarts. Deterministic solvers always find a solution; stochastic solvers 
   are usually faster but never certain. This is the fundamental tradeoff.

**[→ Interactive N-Queens Visualizer](https://rachitagrawal146.github.io/game-algorithms-java/nqueens/)**

---

### 2. Connect4 — Adversarial Search

Two-player Connect4 with an AI opponent powered by Minimax with Alpha-Beta pruning.

The AI builds a game tree to configurable depth (default: 6), assuming both 
players play optimally. Alpha-Beta pruning eliminates branches that cannot 
affect the final decision.

**Benchmark Results (AI vs AI, averaged over full game):**

| Depth | Avg Nodes/Move | Avg Time/Move | Game Length |
|-------|---------------|---------------|-------------|
| 2 | 44 | 0.3ms | 15 moves |
| 4 | 549 | 0.7ms | 24 moves |
| 6 | 4,682 | 4.3ms | 39 moves |
| 8 | 48,157 | 53.1ms | 32 moves |

**Alpha-Beta Pruning Efficiency (mid-game position):**

| Depth | With Pruning | Without | % Tree Pruned |
|-------|-------------|---------|---------------|
| 4 | 1,243 | 2,216 | 43.9% |
| 6 | 20,542 | 77,057 | 73.3% |
| 8 | 299,965 | 2,420,976 | 87.6% |
| 10 | 3,968,268 | 68,890,362 | **94.2%** |

**Key Findings:**

6. **Pruning efficiency increases with depth** — from 0% at depth 2 to 94.2% 
   at depth 10. Deeper search makes pruning exponentially more valuable.

7. **Pruning is phase-dependent** — early game: 90.2% pruned, mid game: 88.1%, 
   late game: 67.1%. As the board fills, fewer branches exist to prune, 
   reducing alpha-beta's advantage.

**[→ Interactive Connect 4 AI](https://rachitagrawal146.github.io/game-algorithms-java/connect4/)**

---

### 3. Mastermind — Information-Theoretic Search

Human player vs a constraint-elimination AI solver. The AI maintains all codes 
consistent with feedback received so far, eliminating candidates after each guess. 
Core idea from Donald Knuth's 1977 minimax algorithm.

**Exhaustive Benchmark (all 360 possible codes):**

| Guesses | Codes | Percentage |
|---------|-------|------------|
| 1 | 1 | 0.3% |
| 2 | 10 | 2.8% |
| 3 | 61 | 16.9% |
| 4 | 166 | **46.1%** |
| 5 | 107 | 29.7% |
| 6 | 15 | 4.2% |

Average: **4.15 guesses**. Each guess eliminates ~82% of remaining candidates.

**Key Findings:**

8. **The solver cracks every code in ≤6 guesses**, averaging 4.15. The 4.2% 
   that require 6 guesses exceed Knuth's theoretical 5-guess bound — our 
   first-candidate strategy trades optimality for simplicity. The gap 
   quantifies the cost of that tradeoff.

9. **Information gain is front-loaded** — the first guess eliminates 82% of 
   candidates (360→65), but later guesses yield diminishing returns as the 
   remaining candidates become increasingly similar to each other.

**[→ Interactive Mastermind Solver](https://rachitagrawal146.github.io/game-algorithms-java/mastermind/)**

---

### 4. 21-Card Trick — Deterministic Algorithm

A mathematical card trick that always identifies a chosen card in 3 rounds via 
systematic column rearrangement — showing how deterministic structure can replace 
what appears to be random guessing.

**[→ Interactive 21-Card Trick](https://rachitagrawal146.github.io/game-algorithms-java/cardtrick/)**

---

## Cross-Project Analysis

The central question — *how does constraint structure affect algorithm efficiency?* — 
becomes clearest when comparing pruning across all three problems:

| Problem | Pruning Method | Nodes Saved | Actually Faster? |
|---------|---------------|-------------|-----------------|
| N-Queens (FC) | Domain propagation | 58% | **No — 3x slower** |
| Connect4 (A-B) | Alpha-Beta cutoff | 90% | **Yes — 10x faster** |
| Mastermind | Candidate elimination | 82%/round | **Yes — 4.15 guesses** |

**The unifying finding:** pruning works when its overhead is cheaper than the 
search it eliminates. Alpha-Beta adds near-zero cost (two integer comparisons) 
while cutting 90% of an exponential tree. Forward Checking cuts 58% of nodes 
but adds domain copying at every surviving node — the overhead exceeds the 
savings at large N. Mastermind's elimination is expensive per step but justified 
because the search space is finite and small.

**The efficiency of pruning depends not on how many branches it cuts, but on 
the ratio of pruning cost to search cost.** This ratio is determined by the 
problem's constraint structure, not by the algorithm alone.

---

## Methodology Notes

**Why these three problems?** N-Queens (constraint satisfaction), Connect4 
(adversarial search), and Mastermind (information-theoretic search) each represent 
a distinct class of combinatorial problem. Studying all three allows questions about 
which problem properties make which algorithms effective.

**On stochastic benchmarking:** Min-Conflicts is averaged over 100 independent 
trials. A single run of a randomised algorithm is not a meaningful data point.

**On the FC time anomaly:** The finding that FC is slower than BT despite fewer 
nodes illustrates that asymptotic node count and wall-clock performance diverge 
when per-node cost is high. This is a recurring theme across algorithm design.

---

## Running the Code

```bash
# N-Queens benchmark (compare all three solvers)
javac NQueens/*.java
java -cp NQueens NQueensBenchmark

# Connect4 — play against the AI
javac Connect4/*.java
java -cp Connect4 Connect4

# Mastermind — try to beat the constraint solver
javac Mastermind/*.java
java -cp Mastermind Mastermind
```

*In BlueJ: open the relevant folder as a project, right-click the main class, 
select "Run main method".*

---

## Built With

This project was developed with [Claude Code](https://claude.ai/code) (Anthropic's 
AI coding assistant) as a collaborative tool for implementation, benchmarking, and 
visualizer development. The research questions, experimental design, algorithm 
selection, and analysis of findings are my own work. Claude Code served as a 
pair-programming partner — accelerating the engineering so I could focus on the 
research.

---

*This project sits alongside separate research on multi-agent strategic behaviour 
(modelling trust emergence and collapse across behavioural archetypes in Kuhn Poker 
environments), which explores related questions about how rational agents behave 
under uncertainty and incomplete information.*

---

Built by **[Rachit Agrawal](https://rachitagrawal146.github.io/)** — Grade 11, 
Sahyadri School (KFI), Pune. Part of a broader portfolio exploring decision-making 
in artificial intelligence.
