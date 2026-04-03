# Game Algorithms — A Comparative Study

Independent research project exploring how algorithm choice affects
performance across three classes of combinatorial problems.

## Live Demo

**[N-Queens Algorithm Visualizer →](https://rachitagrawal146.github.io/game-algorithms-java/Visualizer/NQueensVisualizer.html)**

Step through Backtracking, Forward Checking, and Min-Conflicts
solving the same board in real time.

## Research Question

How does the structure of a problem's constraint space affect
the efficiency of search-based solvers — and can we measure
this difference empirically?

---

## Projects

### N-Queens — Constraint Satisfaction

Three solvers compared across N=8 to N=30. Each represents a
fundamentally different search strategy: exhaustive backtracking,
constraint propagation, and stochastic local search.

**Benchmark Results (N=8 to N=20):**

| N  | BT-Nodes   | BT-Ms | FC-Nodes   | FC-Ms | MC-Nodes (avg) | MC-Ns (avg) |
|----|------------|-------|------------|-------|----------------|-------------|
| 8  | 876        | 0     | 396        | 1     | ~350           | ~400000     |
| 9  | 333        | 0     | 153        | 0     | ~450           | ~420000     |
| 10 | 975        | 0     | 485        | 0     | ~450           | ~380000     |
| 12 | 3066       | 0     | 1266       | 0     | ~500           | ~480000     |
| 14 | 26495      | 1     | 12159      | 2     | ~300           | ~450000     |
| 16 | 160712     | 3     | 69928      | 8     | ~380           | ~330000     |
| 18 | 743229     | 13    | 317097     | 37    | ~200           | ~270000     |
| 20 | 3992510    | 74    | 1687810    | 218   | ~220           | ~650000     |

**Extended FC vs MC (N=22 to N=30):**

| N  | FC-Nodes       | FC-Ms   | MC-Nodes (avg) | MC-Ns (avg) |
|----|----------------|---------|----------------|-------------|
| 22 | 15,470,169     | 973     | ~180           | ~400000     |
| 28 | 33,652,682     | 3950    | ~80            | ~790000     |
| 30 | 660,589,815    | 69989   | ~80            | ~900000     |

**Key findings:**

- Forward Checking explores ~58% fewer nodes than Backtracking at every N
- Despite fewer nodes, FC is 3× slower than BT at N=20 due to
  constraint propagation overhead (domain copying) per node
- Min-Conflicts solves N=30 in under 1ms vs FC's 70 seconds —
  70-200× faster than both systematic solvers
- Both BT and FC show irregular (non-monotonic) growth — some N
  values are structurally easier than others regardless of algorithm
- MC has no worst-case guarantee — occasionally exhausts its
  budget and restarts with a fresh random assignment

### Connect4 — Adversarial Search

Minimax with Alpha-Beta pruning at configurable search depth.
Play against the AI in the console.

- Alpha-Beta pruning with centre-first move ordering for
  efficiency
- Heuristic board evaluation scoring windows of 4 cells
- Configurable depth (default 6) for experimenting with
  search horizon vs speed tradeoffs

### Mastermind — Information-Theoretic Search

Human player mode or AI solver using constraint elimination.

- Knuth-style candidate pruning: maintains all 360 possible
  no-repeat codes and eliminates inconsistent candidates
  after each guess
- Two-pass feedback algorithm correctly handles repeated colours
- AI typically solves in 4-5 guesses

### 21-Card Trick — Deterministic Algorithm

Mathematical card trick using array rearrangement.
After three rounds of column selection, the chosen card
is guaranteed to be at the centre position — demonstrating
how structure can replace randomness.

## Technologies

Java — Arrays, recursion, backtracking, minimax, alpha-beta
pruning, constraint propagation, local search, heuristic evaluation
