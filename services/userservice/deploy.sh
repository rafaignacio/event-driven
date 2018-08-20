gradle clean
gradle build
docker build -t userservice .
docker tag userservice gcr.io/event-driven-example/userservice
docker push gcr.io/event-driven-example/userservice