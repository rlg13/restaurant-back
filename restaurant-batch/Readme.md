# RestaurantBatch

## Restaurant dishes ordering & management Batch Operations

## Features
 * Orders state workflow (delivered)

### Requirements

 * `java`  >= 1.8.0
 * `maven` >= 3.6

### Docker build
>Project must be packaged in _jar_ format is required before docker creation

```bash
#From parent directory
mvn clean
mvn package
```

### Create docker image

```bash
docker build -f .\Dockerfile -t restaurant-batch .
```

### Create volatile container from image listening on port 8080
```bash
docker run -d --link restaurant-api:restaurant-api --hostname=restaurant-batch --name restaurant-batch restaurant-batch
```
#### Description
> All days at 11 in the morning automatically changes the status of the orders received to delivered
