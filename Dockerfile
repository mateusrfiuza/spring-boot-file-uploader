FROM openjdk:17-slim
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar", "-server", "-XX:+UseParallelGC", "-XX:MaxRAMPercentage=50.0"]