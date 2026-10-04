# Assignment 2 - Data Structures
Author: ABDUBEK ALINA  
Group: SE2516 (assumed; change if needed)

Java 17, Maven and Python 3 with matplotlib are required.

## Run
```sh
mvn clean test
java -cp target/classes Benchmark
python3 plot_results.py
```
The benchmark writes results/results.csv. It runs two discarded warm-ups and five measured runs per case, selecting the median. Plot generation produces four PNG files. No bonus tasks are included.

## Files
src/main/java contains the structures, counters and benchmark. src/test/java contains JUnit 5 tests. REPORT.md contains the analysis and proofs. results contains the measured CSV and charts.

## Git submission
The ZIP includes local Git history, feature branches and tag v1.0. There is no remote repository yet. Create an empty GitHub repository, then run:
```sh
git remote add origin YOUR_REPOSITORY_URL
git push -u origin main
git push origin feature/array feature/list feature/heap feature/metrics
git push origin v1.0
```
Include your actual GitHub repository link with the Moodle submission. Do not submit the placeholder URL. Rename the ZIP if your group is different.

## Measurement rules
steps counts one array cell read or one traversal to next, including traversal to null. moves counts one shifted or relocated existing int or one changed list reference (head and tail included). New value writes are not moves. Comparisons count element comparisons only, not bounds, null checks or benchmark validation. Heap property inspection does not change counters.
List setup is excluded from W1-W3 timing and counters. W4 includes insertion, extraction and the order check. Each run uses a fresh structure with the same input. Queries and indexes are generated outside the timer. Missing search values are negative; stored values are nonnegative. Counters add overhead, so these times describe the instrumented implementations.

## Test coverage
Four JUnit tests check random operations against ArrayList and PriorityQueue, empty and singleton cases, duplicates, first and last positions, invalid indexes, extreme int values, sorted heap output, heap property after every random operation and exact small counter examples.

The attached assignment limits AI use to debugging and explanations. This generated project must be reviewed against that rule before submission.
