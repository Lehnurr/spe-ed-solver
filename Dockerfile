FROM maven:3.6.1-jdk-11-slim as DEPS

WORKDIR /app
COPY spe-ed-solver/core/pom.xml core/pom.xml
COPY spe-ed-solver/player/pom.xml player/pom.xml
COPY spe-ed-solver/simulation/pom.xml simulation/pom.xml
COPY spe-ed-solver/utility/pom.xml utility/pom.xml
COPY spe-ed-solver/visualisation/pom.xml visualisation/pom.xml
COPY spe-ed-solver/web-communication/pom.xml web-communication/pom.xml


COPY spe-ed-solver/pom.xml .

RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline

FROM maven:3.6.1-jdk-11-slim as BUILDER
WORKDIR /app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /app/ /app
COPY spe-ed-solver/core/src /app/core/src 
COPY spe-ed-solver/player/src /app/player/src 
COPY spe-ed-solver/simulation/src /app/simulation/src 
COPY spe-ed-solver/utility/src /app/utility/src 
COPY spe-ed-solver/visualisation/src /app/visualisation/src 
COPY spe-ed-solver/web-communication/src /app/web-communication/src 

RUN mvn -B -e clean install -DskipTests=true


FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/core/target/core-0.0.1-SNAPSHOT.jar .
COPY --from=builder /app/player/target/player-0.0.1-SNAPSHOT.jar .
COPY --from=builder /app/simulation/target/simulation-0.0.1-SNAPSHOT.jar .
COPY --from=builder /app/utility/target/utility-0.0.1-SNAPSHOT.jar .
COPY --from=builder /app/visualisation/target/visualisation-0.0.1-SNAPSHOT.jar .
COPY --from=builder /app/web-communication/target/web-communication-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app/core-0.0.1-SNAPSHOT.jar", "Main.java" ]
# dependencies und project class files missing