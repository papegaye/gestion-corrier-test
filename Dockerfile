# ================================
# Étape 1 : Build du projet avec Maven
# ================================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copier seulement le fichier pom.xml
COPY pom.xml .

# 2. Télécharger toutes les dépendances Maven à l'avance
#    Cela permet d'utiliser le cache Docker pour accélérer les rebuilds
RUN mvn dependency:go-offline

# 3. Copier le code source et compiler le projet
COPY src ./src

# permet de compiler plus rapidement
RUN mvn clean package -DskipTests

# ================================
# Étape 2 : Création de l'image finale légère avec Java 21 JRE
# ================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copier le JAR généré depuis l'étape de build et le renommer
COPY --from=build /app/target/*.jar backend-courrier.jar

# Exposer le port 8080 pour accéder à l'application Spring Boot
EXPOSE 8084

# Lancer l'application au démarrage du conteneur
ENTRYPOINT ["java", "-jar", "backend-courrier.jar"]