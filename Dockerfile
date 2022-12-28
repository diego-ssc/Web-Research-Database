FROM openjdk:11
COPY target/myp-0.0.1-SNAPSHOT.jar myp-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/myp-0.0.1-SNAPSHOT.jar"]
