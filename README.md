# Game Algorithms Engine

**Implementations of adversarial search, constraint satisfaction, and information-theoretic reasoning — each with empirical benchmarks and interactive visualizers.**

This isn't a collection of tutorials. It's a laboratory for studying how decision-making works in constrained systems — and where it breaks.

---

## The Core Finding

Across all three projects, one pattern emerged: **an algorithm's real efficiency depends on the ratio between what its optimization costs and what it saves.**

- Alpha-beta pruning in Connect 4 cuts billions of nodes — but adding move-ordering heuristics has overhead. There's a depth threshold below which "smarter" pruning is actually slower.
- Forward-checking in N-Queens reduces search space by 1,700× — but only because propagation cost (O(N) per step) is negligible compared to the subtree cost it prevents.
- Entropy-based guessing in Mastermind is optimal — but requires O(n²) computation per turn. For larger code spaces, the overhead outweighs the information gain.

This meta-pattern now shapes how I think about every system I design: **optimization has overhead, and overhead must be justified by savings.**

---

## Projects

### Connect 4 — Adversarial Search

Minimax with alpha-beta pruning, custom heuristic evaluation, and center-column weighting.

| Metric | Unoptimized | With Alpha-Beta |
|--------|-------------|-----------------|
| Nodes at depth 10 | ~282 billion | ~530,000 |
| Complexity | O(b^d) | O(b^(d/2)) best case |

**Key insight:** Move ordering improves pruning by ~40% — but the ordering computation itself consumes time. The crossover point is empirically discoverable, not theoretically obvious.

[→ Interactive Connect 4 AI](https://rachitagrawal146.github.io/game-algorithms-java/connect4/) · [→ Technical deep-dive](https://rachitagrawal146.github.io/Rachitagrawal146.github.io-/projects.html)

---

### N-Queens — Constraint Satisfaction

Backtracking solver with forward-checking constraint propagation. Comparative mode shows naive vs. optimized approaches running simultaneously.

| Approach (N=12) | Configurations Checked | Reduction |
|-----------------|------------------------|-----------|
| Naive backtracking | ~14.2 million | baseline |
| Forward-checking | ~8,400 | **1,700×** |

**Key insight:** The algorithm appears to "slow down" in middle rows and speed up near the end. Early placements maximally constrain the space; once past the bottleneck, remaining placements are nearly forced. *This emerged from watching the algorithm run — it's not in the textbook.*

[→ Interactive N-Queens Visualizer](https://rachitagrawal146.github.io/game-algorithms-java/nqueens/)

---

### Mastermind — Information-Theoretic Search

Two solving strategies: constraint filtering (simple, memoryless) and entropy-based guessing (Knuth-style, maximizes information gain).

| Strategy | Average Guesses | Worst Case |
|----------|-----------------|------------|
| Constraint filtering | 4.5 | 6 |
| Entropy-based | 4.2 | 5 |

**Key insight:** The entropy approach produces "non-obvious" guesses that humans find strange but are objectively optimal. This is a microcosm of a larger problem in AI: systems that optimize correctly often behave in ways that feel wrong to human intuition.

[→ Interactive Mastermind Solver](https://rachitagrawal146.github.io/game-algorithms-java/mastermind/)

---

## Why Games?

Games are the smallest environments where the hardest problems in computer science appear in their purest form.

A Connect 4 engine makes sequential decisions under uncertainty, optimizes across exponentially large search spaces, and manages the exploration-exploitation tradeoff. These are the same problems that power autonomous vehicles, trading algorithms, and AI agents operating in the real world.

I use games as laboratories. The goal isn't entertainment — it's to study decision-making, constraint satisfaction, and adversarial reasoning in environments where I can control every variable, measure every outcome, and understand what's happening inside the system.

---

## Connection to Research

This repository is Part 1 of a larger inquiry. Part 2 is my Polygence research paper:

**"From Game-Theoretic Poker to the Collapse of Trust: Modelling Strategic Behaviour in Multi-Agent Systems"**

Where this repo studies algorithms playing against known rules, the paper studies what happens when the "rules" are other agents — adaptive, strategic, potentially deceptive. The progression: from deterministic games to game-theoretic environments to multi-agent trust dynamics.

---

## Technical Stack

- **Language:** Java
- **Algorithms:** Minimax, alpha-beta pruning, backtracking, constraint propagation, entropy-based search
- **Methodology:** Each project includes empirical benchmarking (nodes evaluated, pruning rates, wall-clock time) alongside theoretical complexity analysis

---

## Development Notes

This project was built with assistance from Claude (Anthropic) as a pair-programming tool for implementation, debugging, and benchmarking infrastructure. The algorithmic design, empirical analysis, and cross-project synthesis are my own work.

---

## Where This Could Go

These implementations are complete as learning tools. If scaling to production:

**Performance:** Port to C++/Rust, add transposition tables, parallelize constraint propagation
**Architecture:** Decouple into MVC, unified benchmark harness, API exposure
**AI Integration:** Replace hand-coded heuristics with learned evaluation functions; compare against MCTS

These aren't hypothetical — they're engineering steps I understand how to take.

---

## Author

**Rachit Agrawal**
Grade 11, Sahyadri School (KFI), Pune
[Portfolio](https://rachitagrawal146.github.io/Rachitagrawal146.github.io-/) · [Email](mailto:agrawalrachit146@gmail.com)
