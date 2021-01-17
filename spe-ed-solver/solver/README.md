# Submodule `solver`

The solver takes on the task of making decisions to win the game spe-ed as often as possible!

For this purpose, [fundamental analysis functions](src/main/java/solver/analysis) are available.

There are different types of solvers available, the currently best technology is [reachablepoints](src/main/java/solver/reachablepoints).

Each solver has to implement the interface [ISPeedSolver](src/main/java/solver/ISpeedSolver.java) and register as [SolverType](src/main/java/solver/SolverType.java).

For more information about the solvers, please refer to the corresponding README files.
