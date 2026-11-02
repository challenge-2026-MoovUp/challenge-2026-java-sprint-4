# Etapa de build: o Render baixa o codigo do GitHub sem a pasta target, entao gera o .jar aqui
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY . .
RUN sh mvnw -DskipTests package

# Daqui para baixo e o Dockerfile da professora (o .jar vem da etapa de build)
FROM eclipse-temurin:25-jre

ENV LANGUAGE='en_US:en'

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
