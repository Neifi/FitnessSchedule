FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ADD  target/FitnessClassSchedule-0.0.1-SNAPSHOT.jar AppFitnessClassSchedule.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/AppFitnessClassSchedule.jar"]
