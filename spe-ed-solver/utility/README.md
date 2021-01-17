# Submodule `utility`

The utility module provides various utility functions throughout the application. The various functions are divided into several packages.

## package [`utility.extensions`](src/main/java/utility/extensions)

This package contains useful static functions for operations on built-in java types / classes / enums

## package [`utility.game.board`](src/main/java/utility/game/board)

Functions and representations for a game board. The classes are generic, so that the game board can be adapted for specific use cases.

## package [`utility.game.player`](src/main/java/utility/game/player)

Static and dynamic representations of players to use them as an exchange format or to perform operations on them.

## package [`utility.game.step`](src/main/java/utility/game/step)

Representation of a single round of the game as [`GameStep`](src/main/java/utility/game/step/GameStep.java)

## package [`utility.geometry`](src/main/java/utility/geometry)

Objects which are related to geometry.

## package [`utility.logging`](src/main/java/utility/logging)

Manages console output and the LOG file writing. The [`GameLogger`](src/main/java/utility/logging/GameLogger.java) is responsible for outputting game-related information and the [`ApplicationLogger`](src/main/java/utility/logging/ApplicationLogger.java) is responsible for application-related output. The output displayed on the console depends on the logging level specified via the command line parameters.
