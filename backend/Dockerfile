FROM dmitry4k/ratemap:openjdk-17-oracle-cert

COPY ./build/libs/*.jar /app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
