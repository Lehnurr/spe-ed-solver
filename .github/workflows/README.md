# GitHub workflows

These GitHub Actions ensure the quality in the sense of the continuous integration.

## buildTestMacOsX.yml

Builds the project once a day on a MacOs machine and runs all jUnit tests.

## buildTestWindowsx64.yml

Builds the project once a day on a Windows machine and runs all jUnit tests.

## buildUbuntux64.yml

Builds the project when a change is pushed on an Ubuntu machine.

You can view the state of the Action in the main [README-File](https://github.com/Lehnurr/spe-ed-solver)!

## dockerImageDockerHub.yml

Builds a DockerImage when a change is pushed and uploads it to [DockerHub](https://hub.docker.com/r/teamlehnurr/spe-ed-solver).

You can view the state of the Action in the main [README-File](https://github.com/Lehnurr/spe-ed-solver)!

## testUbuntux64.yml

Runs all jUnit tests  on an Ubuntu machine when a change is pushed.

You can view the state of the Action in the main [README-File](https://github.com/Lehnurr/spe-ed-solver)!

## updateDocs.yml

Updates the code documentation on [spe-ed-docs.lehnurr.de](https://spe-ed-docs.lehnurr.de) when a change is pushed.

You can view the state of the Action in the main [README-File](https://github.com/Lehnurr/spe-ed-solver)!
