FROM amazoncorretto:21

WORKDIR .

COPY . .

ENTRYPOINT exec ./gradlew clean build