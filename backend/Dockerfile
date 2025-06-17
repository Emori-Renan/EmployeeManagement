# Use a slim Java 17 JDK image for building and running
# 'jdk-alpine' is fine for build, 'jre-alpine' can be used for smaller final image (multi-stage)
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built Spring Boot JAR file from your host machine into the container.
# Ensure 'crudapp-0.0.1-SNAPSHOT.jar' matches the actual name of your JAR file
# found in your backend project's 'target/' directory after `mvn package`.
COPY target/crudapp-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot application runs on (8080 by default)
EXPOSE 8080

# Command to run your Spring Boot application using the executable JAR
# This is the correct way to run a Spring Boot fat JAR.
ENTRYPOINT ["java", "-jar", "app.jar"]
