FROM openjdk:17-jdk-slim
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} crab_back_end.jar
ENTRYPOINT ["java", "-jar", "/crab_back_end.jar"]