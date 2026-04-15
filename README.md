# Game Algorithms Engine

**Implementations of adversarial search, constraint satisfaction, and information-theoretic reasoning — each with empirical benchmarks and interactive visualizers.**

This isn't a collection of tutorials. It's a laboratory for studying how decision-making works in constrained systems — and where it breaks.

**[Live Site →](https://rachitagrawal146.github.io/game-algorithms-java/)** · **[Algorithm Comparison →](https://rachitagrawal146.github.io/game-algorithms-java/compare/)**

---

## The Core Finding

Across all three projects, one pattern emerged: **an algorithm's real efficiency depends on the ratio between what its optimization costs and what it saves.**

| Problem | Pruning Method | Nodes Saved | Actually Faster? |
|---------|---------------|-------------|-----------------|
| N-Queens (Forward Checking) | Domain propagation | 58% | **No — 3× slower** at large N |
| Connect 4 (Alpha-Beta) | Game-tree cutoff | 90–94% | **Yes — 10× faster** |
| Mastermind (Constraint Elim.) | Candidate filtering | 82%/round | **Yes — 4.15 guesses avg** |

Alpha-beta pruning adds near-zero overhead (two integer comparisons) while eliminating 90% of an exponential tree. Forward checking eliminates 58% of nodes but adds O(n²) domain copying at every surviving node — the overhead exceeds the savings at large N. This meta-pattern now shapes how I think about every system I design: **optimization has overhead, and overhead must be justified by savings.**

---

## Projects

### Connect 4 — Adversarial Search

Minimax with alpha-beta pruning, heuristic evaluation, and centre-first move ordering.

| Depth | Avg Nodes/Move | Avg Time/Move | Pruning % |
|-------|---------------|---------------|-----------|
| 2 | 44 | 0.3ms | 0% |
| 4 | 549 | 0.7ms | 43.9% |
| 6 | 4,682 | 4.3ms | 73.3% |
| 8 | 48,157 | 53.1ms | 87.6% |
| 10 | 3,968,268 | — | **94.2%** |

**Key finding:** Pruning efficiency is phase-dependent — 90.2% in the early game, 88.1% midgame, 67.1% in the endgame. As the board fills, fewer branches exist to prune.

[→ Play against the AI](https://rachitagrawal146.github.io/game-algorithms-java/connect4/) · [→ Source: `Connect4/`](Connect4/)

---

### N-Queens — Constraint Satisfaction

Three solvers compared across N=8 to N=30: backtracking, forward checking, and min-conflicts.

| N | BT Nodes | BT Time | FC Nodes | FC Time | MC Nodes (avg) | MC Time (avg) |
|---|----------|---------|----------|---------|----------------|---------------|
| 8 | 876 | 0ms | 396 | 0ms | ~350 | ~400μs |
| 14 | 26,495 | 1ms | 12,159 | 2ms | ~300 | ~450μs |
| 20 | 3,992,510 | 74ms | 1,687,810 | 218ms | ~220 | ~650μs |
| 30 | — | — | 660,589,815 | 69,989ms | ~79 | ~895μs |

**Key finding:** FC explores 58% fewer nodes than BT but is 3× slower at N=20 due to domain copying overhead. Min-Conflicts solves N=30 in under 1ms — 70,000× faster than FC. Non-monotonic growth means some N values are structurally easy regardless of algorithm.

[→ Interactive Visualizer](https://rachitagrawal146.github.io/game-algorithms-java/nqueens/) · [→ Source: `NQueens/`](NQueens/)

---

### Mastermind — Information-Theoretic Search

Constraint-elimination solver tested exhaustively against all 360 possible no-repeat codes.

| Guesses | Codes Solved | Cumulative |
|---------|-------------|------------|
| 1–2 | 11 | 3.1% |
| 3 | 61 | 19.7% |
| 4 | 166 | **65.8%** |
| 5 | 107 | 95.6% |
| 6 | 15 | 100% |

Average: **4.15 guesses**. Each guess eliminates ~82% of remaining candidates. The 4.2% requiring 6 guesses exceed Knuth's 5-guess theoretical bound — our simpler first-candidate strategy trades optimality for clarity. The gap quantifies the cost of that tradeoff.

[→ Interactive Solver](https://rachitagrawal146.github.io/game-algorithms-java/mastermind/) · [→ Source: `Mastermind/`](Mastermind/)

---

### 21-Card Trick — Deterministic Algorithm

Mathematical card trick that identifies a chosen card in exactly 3 rounds via column rearrangement. Deterministic convergence through ternary subdivision — showing how structure can completely replace randomness.

[→ Interactive Demo](https://rachitagrawal146.github.io/game-algorithms-java/cardtrick/) · [→ Source: `CardTrick/`](CardTrick/)

---

## Why Games?

Games are the smallest environments where the hardest problems in computer science appear in their purest form. A Connect 4 engine makes sequential decisions under uncertainty, optimizes across exponentially large search spaces, and manages the exploration-exploitation tradeoff — the same problems that underpin autonomous vehicles, trading algorithms, and AI agents in the real world.

I use games as laboratories. The goal isn't entertainment — it's to study decision-making, constraint satisfaction, and adversarial reasoning in environments where I can control every variable, measure every outcome, and understand what's happening inside the system.

---

## Connection to Research

This repository is Part 1 of a larger inquiry. Part 2 is my Polygence research paper:

**"From Game-Theoretic Poker to the Collapse of Trust: Modelling Strategic Behaviour in Multi-Agent Systems"**

Where this repo studies algorithms playing against known rules, the paper studies what happens when the "rules" are other agents — adaptive, strategic, potentially deceptive. The shared thread: how rational agents behave under constraint, and when their optimizations break down.

---

## Technical Stack

- **Language:** Java — no external libraries. All data structures and algorithms implemented from scratch.
- **Algorithms:** Minimax, alpha-beta pruning, backtracking, constraint propagation, local search with random restarts, entropy-based candidate elimination
- **Methodology:** Empirical benchmarking with multi-trial averaging for stochastic solvers, controlled comparison across problem classes
- **Interactive Demos:** Vanilla HTML/JavaScript with CSS animations — no frameworks

---

## Running the Code

```bash
# N-Queens benchmark (compare all three solvers, N=8–20)
javac NQueens/*.java
java -cp NQueens NQueensBenchmark

# Connect 4 (play against the Minimax AI)
javac Connect4/*.java
java -cp Connect4 Connect4

# Connect 4 benchmark (depth vs performance, pruning efficiency)
java -cp Connect4 Connect4Benchmark

# Mastermind (human play or AI solver mode)
javac Mastermind/*.java
java -cp Mastermind Mastermind

# Mastermind benchmark (exhaustive test against all 360 codes)
java -cp Mastermind MastermindBenchmark
```

*In BlueJ: open any folder as a project, right-click the main class, select "Run main method".*

---

## Development Notes

This project was built with assistance from [Claude Code](https://claude.ai/code) (Anthropic) as a pair-programming tool for implementation, benchmarking infrastructure, and interactive visualizer development. The research questions, experimental design, algorithm selection, and analysis of findings are my own work.

---

## Author

**Rachit Agrawal**
Grade 12, Sahyadri School (KFI), Pune
[Portfolio](https://rachitagrawal146.github.io/Rachitagrawal146.github.io-/) · [GitHub](https://github.com/RachitAgrawal146) · [Email](mailto:agrawalrachit146@gmail.com)
