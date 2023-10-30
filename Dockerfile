FROM eclipse-temurin:17
RUN mkdir /opt/app
COPY /target/tasks-1.0-jar-with-dependencies.jar /opt/app
CMD ["java", "-jar", "/opt/app/tasks-1.0-jar-with-dependencies.jar"]
EXPOSE 8000