FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY . .
RUN sh mvnw -DskipTests package

FROM eclipse-temurin:25-jre

ENV LANGUAGE='en_US:en'

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
