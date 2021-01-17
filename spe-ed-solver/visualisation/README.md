# Submodule `visualisation`

This module manages the graphical user interface.

It provides the [`Viewer`](src/main/java/visualisation/Viewer.java) to display one [`ViewerSlice`](src/main/java/visualisation/ViewerSlice.java) per round. A separate [`ViewerWindow`](src/main/java/visualisation/ViewerWindow.java) is used for each controlled solver.

In addition, the module provides the visualization of matrices, so that they can be displayed as analyzable images. For this, the images are created using [`ColorGradient`](src/main/java/visualisation/ColorGradient.java) and the [`ImageGeneration` class](src/main/java/visualisation/ImageGeneration.java). The package [`visualisation.files`](src/main/java/visualisation/files) is responsible for the optional saving of the images via the UI.
