FROM openjdk
EXPOSE 8080
COPY /target/restaurant-rest-api-0.0.1-SNAPSHOT.jar /restaurant-rest-api
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=docker","restaurant-rest-api"]