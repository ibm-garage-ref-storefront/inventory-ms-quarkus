##### inventory-ms-quarkus

# Microservice Apps Integration with MySQL Database

*This project is part of the 'IBM Cloud Native Reference Architecture' suite, available at
https://cloudnativereference.dev/*

## Table of Contents

* [Introduction](#introduction)
    + [APIs](#apis)
* [Pre-requisites](#pre-requisites)
* [Running the application](#running-the-application)
    + [Get the Inventory application](#get-the-inventory-application)
    + [Run the MySQL Docker Container](#run-the-mysql-docker-container)
    + [Populate the MySQL Database](#populate-the-mysql-database)
    + [Run the Jaeger Docker Container](#run-the-jaeger-docker-container)
    + [Run the Inventory application](#run-the-inventory-application)
    + [Validating the application](#validating-the-application)
    + [Exiting the application](#exiting-the-application)
* [Conclusion](#conclusion)
* [References](#references)

## Introduction

This project will demonstrate how to deploy a Quarkus Application with a MySQL database.

![Application Architecture](static/inventory.png?raw=true)

Here is an overview of the project's features:
- Leverages [`Quarkus`](https://quarkus.io/), the Supersonic Subatomic Java Framework.
- Uses [`MySQL`](https://www.mysql.com/) as the inventory database.

### APIs

* Get all items in inventory:
    + `http://localhost:8080/micro/inventory`

## Pre-requisites:

* [Java](https://www.java.com/en/)

## Running the application

### Get the Inventory application

- Clone inventory repository:

```bash
git clone https://github.com/ibm-garage-ref-storefront/inventory-ms-quarkus.git
cd inventory-ms-quarkus
```

### Run the MySQL Docker Container

Run the below command to get MySQL running via a Docker container.

```bash
# Start a MySQL Container with a database user, a password, and create a new database
docker run --name inventorymysql \
    -e MYSQL_ROOT_PASSWORD=admin123 \
    -e MYSQL_USER=dbuser \
    -e MYSQL_PASSWORD=password \
    -e MYSQL_DATABASE=inventorydb \
    -p 3306:3306 \
    -d mysql
```

If it is successfully deployed, you will see something like below.

```
$ docker ps
CONTAINER ID   IMAGE     COMMAND                  CREATED       STATUS       PORTS                               NAMES
e87f041c7da7   mysql     "docker-entrypoint.s…"   2 hours ago   Up 2 hours   0.0.0.0:3306->3306/tcp, 33060/tcp   inventorymysql
```

### Populate the MySQL Database

Now let us populate the MySQL with data.

- Firstly, `ssh` into the MySQL container.

```
docker exec -it inventorymysql bash
```

- Now, run the below command for table creation.

```
mysql -udbuser -ppassword
```

- This will take you to something like below.

```
root@e87f041c7da7:/# mysql -udbuser -ppassword
mysql: [Warning] Using a password on the command line interface can be insecure.
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 13
Server version: 8.0.23 MySQL Community Server - GPL

Copyright (c) 2000, 2021, Oracle and/or its affiliates.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql>
```

- Go to `scripts > mysql_data.sql`. Copy the contents from [mysql_data.sql](./scripts/mysql_data.sql) and paste the contents in the console.

- You can exit from the console using `exit`.

```
mysql> exit
Bye
```

- To come out of the container, enter `exit`.

```
root@d88a6e5973de:/# exit
```

### Run the Jaeger Docker Container

Set up Jaegar for opentracing. This enables distributed tracing in your application.

```
docker run -d -p 5775:5775/udp -p 6831:6831/udp -p 6832:6832/udp -p 5778:5778 -p 16686:16686 -p 14268:14268 jaegertracing/all-in-one:latest
```

If it is successfully run, you will see something like this.

```
$ docker run -d -p 5775:5775/udp -p 6831:6831/udp -p 6832:6832/udp -p 5778:5778 -p 16686:16686 -p 14268:14268 jaegertracing/all-in-one:latest
1c127fd5dfd1f4adaf892f041e4db19568ebfcc0b1961bec52a567f963014411
```

### Run the Inventory application

#### Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev -Dquarkus.datasource.jdbc.url=jdbc:tracing:mysql://localhost:3306/inventorydb?useSSL=true -Dquarkus.datasource.username=dbuser -Dquarkus.datasource.password=password -DJAEGER_AGENT_HOST=localhost -DJAEGER_AGENT_PORT=6831 -DJAEGER_SERVICE_NAME=inventory-ms-quarkus -DJAEGER_SAMPLER_TYPE=const -DJAEGER_SAMPLER_PARAM=1
```

If it is successful, you will see something like this.

```
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- quarkus-maven-plugin:1.11.1.Final:dev (default-cli) @ inventory-ms-quarkus ---
Listening for transport dt_socket at address: 5005
__  ____  __  _____   ___  __ ____  ______
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
17:15:40 INFO  traceId=, spanId=, sampled= [io.quarkus] (Quarkus Main Thread) inventory-ms-quarkus 1.0.0-SNAPSHOT on JVM (powered by Quarkus 1.11.1.Final) started in 1.442s. Listening on: http://localhost:8080
17:15:40 INFO  traceId=, spanId=, sampled= [io.quarkus] (Quarkus Main Thread) Profile dev activated. Live Coding activated.
17:15:40 INFO  traceId=, spanId=, sampled= [io.quarkus] (Quarkus Main Thread) Installed features: [agroal, cdi, jaeger, jdbc-mysql, mutiny, narayana-jta, resteasy, resteasy-jsonb, smallrye-context-propagation, smallrye-opentracing]
17:16:15 INFO  traceId=626cce48d0f630d3, spanId=626cce48d0f630d3, sampled=true [ib.cn.ap.InventoryResource] (executor-thread-1) /inventory endpoint
```

#### Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `inventory-ms-quarkus-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using the below command.

```
java -jar -Dquarkus.datasource.jdbc.url=jdbc:tracing:mysql://localhost:3306/inventorydb?useSSL=true -Dquarkus.datasource.username=dbuser -Dquarkus.datasource.password=password -DJAEGER_AGENT_HOST=localhost -DJAEGER_AGENT_PORT=6831 -DJAEGER_SERVICE_NAME=inventory-ms-quarkus -DJAEGER_SAMPLER_TYPE=const -DJAEGER_SAMPLER_PARAM=1 -jar target/inventory-ms-quarkus-1.0.0-SNAPSHOT-runner.jar
```

If it is run successfully, you will see something like below.

```
$ java -jar -Dquarkus.datasource.jdbc.url=jdbc:tracing:mysql://localhost:3306/inventorydb?useSSL=true -Dquarkus.datasource.username=dbuser -Dquarkus.datasource.password=password -DJAEGER_AGENT_HOST=localhost -DJAEGER_AGENT_PORT=6831 -DJAEGER_SERVICE_NAME=inventory-ms-quarkus -DJAEGER_SAMPLER_TYPE=const -DJAEGER_SAMPLER_PARAM=1 -jar target/inventory-ms-quarkus-1.0.0-SNAPSHOT-runner.jar
__  ____  __  _____   ___  __ ____  ______
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
17:42:43 INFO  traceId=, spanId=, sampled= [io.quarkus] (main) inventory-ms-quarkus 1.0.0-SNAPSHOT on JVM (powered by Quarkus 1.11.1.Final) started in 5.814s. Listening on: http://0.0.0.0:8080
17:42:43 INFO  traceId=, spanId=, sampled= [io.quarkus] (main) Profile prod activated.
17:42:43 INFO  traceId=, spanId=, sampled= [io.quarkus] (main) Installed features: [agroal, cdi, jaeger, jdbc-mysql, mutiny, narayana-jta, resteasy, resteasy-jsonb, smallrye-context-propagation, smallrye-opentracing]
```

#### Creating a native executable

Note: In order to run the native executable, you need to install GraalVM. For instructions on how to install it, refer [this](https://quarkus.io/guides/building-native-image).

You can create a native executable using:
```shell script
./mvnw package -Pnative
```

If you run into any errors, make sure you configured your environment properly. For clear instructions, refer this [doc](https://quarkus.io/guides/building-native-image).

You can then execute your native executable with the below command:

```
./target/inventory-ms-quarkus-1.0.0-SNAPSHOT-runner -Dquarkus.datasource.jdbc.url=jdbc:tracing:mysql://localhost:3306/inventorydb?useSSL=true -Dquarkus.datasource.username=dbuser -Dquarkus.datasource.password=password -DJAEGER_AGENT_HOST=localhost -DJAEGER_AGENT_PORT=6831 -DJAEGER_SERVICE_NAME=inventory-ms-quarkus -DJAEGER_SAMPLER_TYPE=const -DJAEGER_SAMPLER_PARAM=1
```

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

#### Running the application using docker

- Build the JVM docker image and run the application.

Package the application.
```shell script
./mvnw package -Dquarkus.native.container-build=true
```

Build the docker image using `Dockerfile.jvm`.
```shell script
docker build -f src/main/docker/Dockerfile.jvm -t inventory-ms-quarkus .
```

Run the application.
```shell script
docker run -it -d --rm -e quarkus.datasource.jdbc.url=jdbc:tracing:mysql://host.docker.internal:3306/inventorydb?useSSL=true -e quarkus.datasource.username=dbuser -e quarkus.datasource.password=password -e JAEGER_AGENT_HOST=host.docker.internal -e JAEGER_AGENT_PORT=6831 -e JAEGER_SERVICE_NAME=inventory-ms-quarkus -e JAEGER_SAMPLER_TYPE=const -e JAEGER_SAMPLER_PARAM=1 -p 8082:8080 inventory-ms-quarkus
```

- Build the native docker image and run the application.

For native docker image, package the application using native profile.
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

Build the docker image using `Dockerfile.native`.
```shell script
docker build -f src/main/docker/Dockerfile.native -t inventory-ms-quarkus-native .
```

Run the application.
```shell script
docker run -it -d --rm -e quarkus.datasource.jdbc.url=jdbc:tracing:mysql://host.docker.internal:3306/inventorydb?useSSL=true -e quarkus.datasource.username=dbuser -e quarkus.datasource.password=password -e JAEGER_AGENT_HOST=host.docker.internal -e JAEGER_AGENT_PORT=6831 -e JAEGER_SERVICE_NAME=inventory-ms-quarkus -e JAEGER_SAMPLER_TYPE=const -e JAEGER_SAMPLER_PARAM=1 -p 8082:8080 inventory-ms-quarkus-native
```

### Validating the application

Now, you can validate the application as follows.

Note: If you are running using docker, use `8082` instead of `8080` as port.

- Try to hit http://localhost:8080/micro/inventory and you should be able to see a list of items.

- You can also do it using the below command.

```
curl http://localhost:8080/micro/inventory
```

![Inventory api](static/inventory_api_result.png?raw=true)

- You can access the swagger api at http://localhost:8080/q/swagger-ui/

![Inventory swagger api](static/inventory_swagger_api.png?raw=true)

Note: If you are running using docker, use `8082` instead of `8080` as port.

- To access Jaeger UI, use http://localhost:16686/ and point the service to `inventory-ms-quarkus` to access the traces.

![Inventory Jaeger traces](static/inventory_jaeger_traces.png?raw=true)

![Inventory Jaeger trace details](static/inventory_jaeger_trace_details.png?raw=true)

### Exiting the application

To exit the application, just press `Ctrl+C`.

If using docker, use `docker stop <container_id>`

## Conclusion

You have successfully developed and deployed the Inventory Microservice and a MySQL database locally using Quarkus framework.

## References

- https://quarkus.io/guides/getting-started
- https://quarkus.io/guides/config
- https://quarkus.io/guides/building-native-image
- https://quarkus.io/guides/opentracing
- https://quarkus.io/guides/openapi-swaggerui
