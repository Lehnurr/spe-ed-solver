# Docker

In this directory you will find everything you need to build and run the spe-ed-solver as Docker Image.

You have 2 options:

- Use the deafult version with 4 threads
  - build the image yourself
  - or use the latest release from DockerHub via `docker pull teamlehnurr/spe-ed-solver:latest`
  - or use latest release from GitHub
- use the version for low performance systems with only one thread

## Default version

### Build

You can simply run the `imageBuild.bat` file to create a Docker Image.
It is also possible to create the Docker Image by hand:
The `Dockerfile` file must be used. Run the command `docker build --pull --rm -f "Dockerfile" -t teamlehnurr/spe-ed-solver:latest ".."` in this directory to create a Docker Image. The command can also be executed in another directory, please specify the correct PATH context and Dockerfile when doing so.

### Run

Please note that environment variables are required for the execution. You can find more information in the notes.

To run the created image, you can use the file `imageRun.bat`. Alternatively you can run the included command `docker run --env-file="variables.local.env" teamlehnurr/spe-ed-solver` manually.

## Version for low performance systems

### Build

You can simply run the `imageBuildOneThread.bat` file to create a Docker Image.
It is also possible to create the Docker Image by hand:
The `DockerfileOneThread` file must be used. Run the command `docker build --pull --rm -f "DockerfileOneThread" -t teamlehnurr/spe-ed-solver-one-thread:latest ".."` in this directory to create a Docker Image. The command can also be executed in another directory, please specify the correct PATH context and Dockerfile when doing so.

### Run

Please note that environment variables are required for the execution. You can find more information in the notes.

To run the created image, you can use the file `imageRunOneThread.bat`. Alternatively you can run the included command `docker run --env-file="variables.local.env" teamlehnurr/spe-ed-solver-one-thread` manually.

## Notes

- For the execution some environment variables are needed. For more information, see the [main README file](https://github.com/Lehnurr/spe-ed-solver). To send the environment variables to Docker, follow these steps:

  1. make a copy `variables.local.env` of the `variables.repo.env file`.
  2. in `variables.local.env` modify the values of the environment variables

- For all provided versions logging is disabled. If logging should be enabled, you need to modify the Dockerfile.
- The .dockerignore file is not used in the provided \*.bat files. Include the file, if you want to reduce the memory of the DockerImages.
- The application provides some extensions, you can view them in the [main README file](https://github.com/Lehnurr/spe-ed-solver). To use the extensions in combination with Docker, you need to modify the Dockerfile or add the required arguments when running a Docker Image. Please note that the graphical user interface is not available in combination with Docker.
