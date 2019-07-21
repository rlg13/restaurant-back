FROM openjdk
COPY /target/restaurant-batch-0.0.1-SNAPSHOT.jar /restaurant-batch-0.0.1.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=docker","restaurant-batch-0.0.1.jar"]