# RestaurantRestApi

## Restaurant dishes ordering & management Rest API

## Features
 * Users management
 * Create dishes
 * Create orders (for today or future dates)
 * Orders state workflow (create, pay and cancel)

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
docker build -f .\Dockerfile -t restaurant-rest-api .
```

### Create volatile container from image listening on port 8080
```bash
docker run -d -p 8080:8080 --rm --name restaurant-api --hostname=restaurant-api restaurant-rest-api
```
#### Endpoints aviable
> Api context path: /restaurant-api
* Dishes 
>* /dishes/{id} &rarr; Get for dish by id
>* /dishes/type/{type} &rarr; Get for dish by type
>* /dishes &rarr; Post for creation dish
* Orders
>* /orders/{id} &rarr; Get for order by id
>* /orders &rarr; Post Creation for order
>* /orders &rarr; Get for list order by filter params (Start date, end date and user )
>* /orders/{id}/process/{newState} &rarr; Get enpoint for orders state transitions 
>* /orders/batch &rarr; Get for list orders to receive (Restrict use for User system)
* Users
>* /users/logout &rarr; Delete for erase session stablished
>* /users/login &rarr; Post for stablish new user session
>* /users &rarr; Post for create new User