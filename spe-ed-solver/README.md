# spe-ed-solver Multi-Module Project

Further information about the submodules are contained in their seperated README files!

## Submodule [`core`](core)

This is the entry point of the application. It deals with the command line parameters and the management of the solvers.

## Submodule [`simulation`](simulation)

This package provides a simulation of the game [spe-ed](https://github.com/informatiCup/informatiCup2021). The board dimensions, players/solvers and the deadline limits can be passed via the cli.

## Submodule [`solver`](solver)

This is the heart of the application. Here are the different solvers to win the game [spe-ed](https://github.com/informatiCup/informatiCup2021) as often as possible.

## Submodule [`utility`](utility)

Essential functions that are used across the entire application. Examples are [logging](utility/src/main/java/utility/logging) or [geometric calculations](utility/src/main/java/utility/geometry).

## Submodule [`visualisation`](visualisation)

Anything that concerns the Viewer and its functions.

## Submodule [`web-communication`](web-communication)

Everything that communicates with another server. Provides the received data as Java objects for the other modules.
