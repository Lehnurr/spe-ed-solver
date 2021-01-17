# reachable points solver

This solver is based on calculating the possible paths in order to make decisions based on them. Different types are available, but they follow the same solution approach and differ only in their performance.

## graph

Uses its own board that represents the graph. This is updated at the beginning of each round with the new information. To enable multithreading, a basic set of paths is first calculated and then distributed to several threads.

## multithreaded

Performs a forward search. One thread is used for each possible first action.

## singlethreaded

Performs a forward search with a thread.
