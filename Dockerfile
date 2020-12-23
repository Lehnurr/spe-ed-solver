#FROM openjdk:8-jdk-alpine
#VOLUME /tmp
#ARG JAVA_OPTS
#ENV JAVA_OPTS=$JAVA_OPTS
#COPY target/spe-ed-solver-0.0.1-SNAPSHOT.jar speedsolverjava.jar
#EXPOSE 3000
#ENTRYPOINT exec java $JAVA_OPTS -jar speedsolverjava.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar speedsolverjava.jar


FROM maven:latest as DEPS

WORKDIR /opt/app
COPY core/pom.xml core/pom.xml
COPY player/pom.xml player/pom.xml
COPY simulation/pom.xml simulation/pom.xml
COPY utility/pom.xml utility/pom.xml
COPY visualisation/pom.xml visualisation/pom.xml
COPY web-communication/pom.xml web-communication/pom.xml


COPY pom.xml .
RUN mvn -B package --file spe-ed-solver/pom.xml -DskipTests

#RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline
# if you have modules that depends each other, you may use -DexcludeArtifactIds as follows
# RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -DexcludeArtifactIds=module1

# Copy the dependencies from the DEPS stage with the advantage
# of using docker layer caches. If something goes wrong from this
# line on, all dependencies from DEPS were already downloaded and
# stored in docker's layers.
FROM maven:latest as BUILDER
WORKDIR /opt/app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /opt/app/ /opt/app
COPY core/src /opt/app/core/src 
COPY player/src /opt/app/player/src 
COPY simulation/src /opt/app/simulation/src 
COPY utility/src /opt/app/utility/src 
COPY visualisation/src /opt/app/visualisation/src 
COPY web-communication/src /opt/app/web-communication/src 


# use -o (--offline) if you didn't need to exclude artifacts.
# if you have excluded artifacts, then remove -o flag
RUN mvn -B -e -o clean install -DskipTests=true

# At this point, BUILDER stage should have your .jar or whatever in some path
FROM openjdk:8-alpine
WORKDIR /opt/app
COPY --from=builder /opt/app/<path-to-target>/my-1.0.0.jar .
EXPOSE 8080
CMD [ "java", "-jar", "/opt/app/my-1.0.0.jar" ]