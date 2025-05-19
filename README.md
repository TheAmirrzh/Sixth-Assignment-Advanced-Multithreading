# Sixth-Assignment-Advanced-Multithreading

This repository contains the solution to the "Sixth Assignment: Advanced Multithreading" project, focusing on implementing multi-threaded applications in Java. The project explores theoretical aspects of thread safety and practical applications, including Monte Carlo π estimation and a banking system, with bonus visualizations and performance benchmarking.

## Project Objectives

The primary objectives of this project are:
- Demonstrate an understanding of advanced multithreading concepts in Java, including thread safety and synchronization.
- Implement a single-threaded and multi-threaded Monte Carlo method to estimate the value of π.
- Develop a thread-safe banking system capable of handling concurrent transactions.
- Enhance the project with bonus features such as live visualizations and performance metrics.

## Features

### Theoretical Questions (20 points)
- **Analysis of Thread Safety**: Provides a detailed report (`Report.md`) analyzing the behavior of a multi-threaded counter implementation using `AtomicInteger` versus a regular `int`, addressing thread-safety guarantees and use cases.

### Practical Questions (80 points)
1. **Monte Carlo π Estimation (50 points)**:
    - **Single-threaded Implementation**: Estimates π using a Monte Carlo simulation with a large number of random points.
    - **Multi-threaded Implementation**: Distributes the computation across multiple threads using `ExecutorService` and `AtomicLong` for thread-safe counting.
    - **Performance Comparison**: Measures and compares execution times between single-threaded and multi-threaded approaches.
    - **Bonus Features**:
        - **Visualization**: A JavaFX-based visual representation of points inside and outside a unit circle.
        - **Benchmark Export**: Exports performance data to a CSV file (`monte_carlo_benchmark.csv`).

2. **Banking System (30 points)**:
    - **Thread-Safe Accounts**: Implements `BankAccount` with `ReentrantLock` for safe concurrent access to account balances.
    - **Transaction Processing**: Supports deposit, withdraw, and transfer operations across multiple threads using `TransactionProcessor`.
    - **Bonus Feature**: A JavaFX-based live chart displaying account balances over time.

### Bonus Tasks (Up to 10 points)
- Integration of JavaFX for real-time visualizations in both Monte Carlo and banking components.
- Export of benchmark results to facilitate performance analysis.

## Theoretical Questions and Answers

The following answers are documented in `Report.md` and address the theoretical questions posed in the assignment:

1. **What output do you get from the program? Why?**
    - **Atomic Counter**: Approximately 2,000,000 (e.g., with 2 threads each incrementing 1,000,000 times), due to `AtomicInteger` ensuring atomic updates despite thread scheduling variations.
    - **Normal Counter**: Less than 2,000,000 (e.g., 1,998,xxx) due to race conditions in `normalCounter++`, leading to lost updates.
    - **Reason**: `AtomicInteger` provides thread-safe increments, while a regular `int` does not, resulting in inconsistent outcomes in multi-threaded environments.

2. **What is the purpose of `AtomicInteger` in this code?**
    - `AtomicInteger` ensures thread-safe integer operations without explicit locks, preventing data corruption during concurrent modifications.

3. **What thread-safety guarantees does `atomicCounter.incrementAndGet()` provide?**
    - `incrementAndGet()` atomically reads the current value, increments it, and writes it back, guaranteeing no interference between threads and ensuring consistent results.

4. **In which situations would using a lock be a better choice than an atomic variable?**
    - **Complex Logic**: When multiple operations (e.g., updating multiple variables) must be atomic.
    - **Conditional Updates**: When updates depend on conditions requiring synchronization with `Condition`.
    - **Coarse-Grained Control**: When synchronizing larger code blocks, though atomic variables are more efficient for fine-grained operations.

5. **Besides `AtomicInteger`, what other data types are available in the `java.util.concurrent.atomic` package?**
    - `AtomicBoolean`
    - `AtomicLong`
    - `AtomicReference`
    - `AtomicIntegerArray`
    - `AtomicLongArray`
    - `AtomicReferenceArray`

## Installation

### Prerequisites
- **Java Development Kit (JDK)**: Version 17 (recommended for compatibility with JavaFX 17).
- **Gradle**: Version 8.x or later (included via wrapper `./gradlew`).
- **Operating System**: macOS, Linux, or Windows.
