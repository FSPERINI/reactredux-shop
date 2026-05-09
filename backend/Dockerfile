# Stage 1 : Build
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copier le Maven Wrapper et le pom.xml d'abord (cache des dépendances)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Télécharger les dépendances (couche cachée si pom.xml ne change pas)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copier le code source et builder
COPY src/ src/
RUN ./mvnw package -DskipTests -B

# Stage 2 : Runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# Créer le répertoire pour la base H2
RUN mkdir -p /app/data

# Copier le JAR buildé
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8095

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
