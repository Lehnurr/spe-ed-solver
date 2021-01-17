![banner](.github/images/banner.png)
# spe-ed-solver von Team Lehnurr

[English Readme](README.md)

Dieses Repository enthält die Lösung des Teams Lehnurr für den [InformatiCup:2021](https://github.com/informatiCup/informatiCup2021).

## Verfügbare Daten

| Beschreibung                               | Daten                                                                                              |
| ------------------------------------------ | -------------------------------------------------------------------------------------------------- |
| Quellcode                                  | [spe-ed-solver verzeichnis](spe-ed-solver)                                                         |
| Docker Files mit Anleitung                 | [.docker verzeichnis](.docker)                                                                     |
| Dokumentation und Benutzerhandbuch         | [ausarbeitung_informaticup2021_lehnurr.pdf](elaboration/ausarbeitung_informaticup2021_lehnurr.pdf) |
| Projekt Dokumentation                      | [spe-ed-docs.lehnurr.de](https://spe-ed-docs.lehnurr.de/index.html)                        |
| Quellcode Dokumentation                    | [spe-ed-docs.lehnurr.de/apidocs](https://spe-ed-docs.lehnurr.de/apidocs/index.html)                |
| Branch für den Quellcode der Dokumentation | [gh-pages branch](https://github.com/Lehnurr/spe-ed-solver/tree/gh-pages)                          |
| Python Prototyp                            | [spe-ed-solver-prototype](https://github.com/Lehnurr/spe-ed-solver-prototype)                      |

## Build

Es werden das Java 11 JDK und maven benötigt, um das Projekt zu kompilieren. Andere Abhängigkeiten werden automatisch von maven bereitgestellt.
Der Befehl um das Projekt zu kompilieren lautet:

`mvn package -f spe-ed-solver/pom.xml`.

Um ein Docker-Image zu erstellen, kann der folgende Befehl verwendet werden:

`docker build --pull --rm -f ".docker/Dockerfile" -t teamlehnurr/spe-ed-solver:latest "."`

## Run

Sie können das Projekt mit Docker oder Java 11 ausführen.

- Für Docker können Sie das Image mit `docker pull teamlehnurr/spe-ed-solver:latest` abrufen
- Für Java können Sie eine fertig gebaute [`core-<Version>-jar-with-dependencies.jar`](https://github.com/Lehnurr/spe-ed-solver/releases) herunterladen

Weitere informationen bezüglich der Ausführung sind in dem [Benutzerhandbuch](elaboration/ausarbeitung_informaticup2021_lehnurr.pdf) und der [Docker README DAtei](/.docker) enthalten.

## Weitere Informationen

Um eine Informationsflut zu vermeiden, sind in dieser README-Datei nur projektbezogene Informationen enthalten. Weitere, technische Informationen finden Sie in den README's der Unterverzeichnisse.

## Kontakt

Sie können uns gerne per E-Mail kontaktieren: [team@lehnurr.de](mailto:team@lehnurr.de)

[Team Lehnurr](https://team.lehnurr.de)
