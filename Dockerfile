# Wähle ein OpenJDK-Image aus
FROM eclipse-temurin:21-alpine

# Setze das Arbeitsverzeichnis
WORKDIR /app

# Kopiere das fertige JAR-File in das Arbeitsverzeichnis
COPY target/Product-API-0.0.1-SNAPSHOT.jar /app/myapp.jar
#COPY app.config /app
# Exponiere den Port, den die Anwendung verwendet
EXPOSE 8080

# Definiere den Befehl, um die Anwendung zu starten
ENTRYPOINT ["java", "-jar", "myapp.jar"]
