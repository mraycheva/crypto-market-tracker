FROM amazoncorretto:19
LABEL maintainer="monicaraycheva@gmail.com"
EXPOSE 8080
WORKDIR /market
COPY ./build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/market/app.jar"]
