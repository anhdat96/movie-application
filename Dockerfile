FROM openjdk:17-jdk-slim-buster
ADD target/*.jar movie-application.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","movie-application.jar"]