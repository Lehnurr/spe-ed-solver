# Submodule `simulation`

This package provides a simulation of the game spe-ed.
The actual simulation takes place in the [`Game`-class](src/main/java/simulation/Game.java).

The communication is not performed via JSON as with the web service, but directly via the Java objects, which are also provided by the web service module.

The [`SimulationPlayer`](src/main/java/simulation/SimulationPlayer.java) and the [`SimulationDeadline`](src/main/java/simulation/SimulationDeadline.java) are used to store the state of the simulation and to perform operations on the players.

The [`PlayerDeadline`](src/main/java/simulation/PlayerDeadline.java) behaves differently than a normal deadline. For this deadline the time starts to run as soon as it is requested for the first time. This is done so that all solvers can calculate the actions one after another and thus use the full performance of the system.
