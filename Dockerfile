FROM openjdk:8-jdk-alpine

ADD build/libs/*.jar /beertap.jar

CMD ["java", "-Dserver.port=80", "-jar", "/beertap.jar"]
