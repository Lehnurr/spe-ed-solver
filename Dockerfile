FROM maven:latest as DEPS

WORKDIR /opt/app
COPY spe-ed-solver/core/pom.xml core/pom.xml
COPY spe-ed-solver/player/pom.xml player/pom.xml
COPY spe-ed-solver/simulation/pom.xml simulation/pom.xml
COPY spe-ed-solver/utility/pom.xml utility/pom.xml
COPY spe-ed-solver/visualisation/pom.xml visualisation/pom.xml
COPY spe-ed-solver/web-communication/pom.xml web-communication/pom.xml


COPY spe-ed-solver/pom.xml .

RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline

FROM maven:latest as BUILDER
WORKDIR /opt/app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /opt/app/ /opt/app
COPY spe-ed-solver/core/src /opt/app/core/src 
COPY spe-ed-solver/player/src /opt/app/player/src 
COPY spe-ed-solver/simulation/src /opt/app/simulation/src 
COPY spe-ed-solver/utility/src /opt/app/utility/src 
COPY spe-ed-solver/visualisation/src /opt/app/visualisation/src 
COPY spe-ed-solver/web-communication/src /opt/app/web-communication/src 

RUN mvn -B -e -o clean install -DskipTests=true


FROM openjdk:latest
WORKDIR /opt/app
COPY --from=builder /opt/app/core/target/core-0.0.1-SNAPSHOT.jar .
COPY --from=builder /opt/app/player/target/player-0.0.1-SNAPSHOT.jar .
COPY --from=builder /opt/app/simulation/target/simulation-0.0.1-SNAPSHOT.jar .
COPY --from=builder /opt/app/utility/target/utility-0.0.1-SNAPSHOT.jar .
COPY --from=builder /opt/app/visualisation/target/visualisation-0.0.1-SNAPSHOT.jar .
COPY --from=builder /opt/app/web-communication/target/web-communication-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/opt/app/core-0.0.1-SNAPSHOT.jar", "Main.java" ]
# Missing external jars...