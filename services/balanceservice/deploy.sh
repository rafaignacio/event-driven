dotnet clean
dotnet build
docker build -t balanceservice .
docker tag balanceservice gcr.io/event-driven-example/balanceservice
docker push gcr.io/event-driven-example/balanceservice