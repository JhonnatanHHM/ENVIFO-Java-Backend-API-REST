# Etapa 1: Build con Maven Wrapper
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copiar el wrapper y archivos de configuración
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copiar el resto del código fuente
COPY src ./src

# Compilar sin tests
RUN ./mvnw package -DskipTests

# Etapa 2: Imagen final liviana
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Puerto que se expone
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
