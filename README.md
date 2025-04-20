# VM-cloud-algo

# Genetic Algorithm for Cloud Service Provider (CSP) Selection

This Java project implements a simple Genetic Algorithm (GA) to solve a Cloud Service Provider (CSP) selection problem. Each CSP is evaluated based on cost, reliability, and latency. The algorithm evolves combinations of CSPs to find the most optimal set.

## Features

- Binary chromosome representation (e.g., `10101010`)
- Tournament selection (k = 3)
- Single-point crossover
- Bit-flip mutation
- Multi-objective fitness function
- Tracks best solution across generations

## Fitness Function

The fitness function evaluates selected CSPs based on:

- **Cost**: Normalized based on the maximum selected CSP cost (40% weight)
- **Reliability**: Penalty for low reliability (30% weight)
- **Latency**: Sum of latencies (30% weight)

**Lower fitness values are better**, as this represents lower cost, higher reliability, and lower latency.

## CSP Dataset

Each CSP has three attributes:

| CSP     | Cost ($) | Reliability | Latency (s) |
|---------|----------|-------------|-------------|
| CSP_1   | 4.6      | 0.80        | 0.6         |
| CSP_2   | 5.0      | 0.85        | 0.4         |
| CSP_3   | 6.0      | 0.90        | 0.5         |
| CSP_4   | 4.8      | 0.82        | 0.6         |
| CSP_5   | 6.2      | 0.92        | 0.4         |
| CSP_6   | 6.5      | 0.94        | 0.5         |
| CSP_7   | 5.5      | 0.88        | 0.6         |
| CSP_8   | 7.0      | 0.98        | 0.4         |

## Parameters

| Parameter        | Value     |
|------------------|-----------|
| Population Size  | 5         |
| Generations      | 10,000    |
| Mutation Rate    | 30%       |
| Crossover Type   | Single-point |
| Selection Method | Tournament (k = 3) |

## How It Works

1. **Initialization**: Randomly generate a population of binary chromosomes.
2. **Selection**: Use tournament selection to choose the fittest parents.
3. **Crossover**: Generate new offspring using single-point crossover.
4. **Mutation**: Flip random bits in the offspring with a given probability.
5. **Next Generation**: Combine and select the top chromosomes for the next generation.
6. **Repeat**: Iterate for a fixed number of generations.

## Sample Output

