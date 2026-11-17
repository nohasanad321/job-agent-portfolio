FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace
COPY . .
RUN chmod +x mvnw && ./mvnw -q -DskipTests package
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/job-agent-portfolio-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8083
USER 10001
ENTRYPOINT ["java","-jar","/app/app.jar"]
