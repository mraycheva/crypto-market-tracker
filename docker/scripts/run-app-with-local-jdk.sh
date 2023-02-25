./gradlew clean bootJar -x test
docker-compose -f ./docker/docker-compose.yaml up
