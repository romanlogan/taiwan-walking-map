FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/DBProject-1.0.jar taiwan-walking-map.jar
ENTRYPOINT ["java","-jar","taiwan-walking-map.jar"]