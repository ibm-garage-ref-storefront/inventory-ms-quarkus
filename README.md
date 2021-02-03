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
    + [Run the Inventory application](#run-the-inventory-application)
    + [Validating the application](#validating-the-application)
    + [Exiting the application](#exiting-the-application)
* [Conclusion](#conclusion)

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

### Run the Inventory application

#### Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev -Dquarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/inventorydb?useSSL=true -Dquarkus.datasource.username=dbuser -Dquarkus.datasource.password=password 
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
2021-02-03 19:05:06,707 WARN  [io.qua.res.com.dep.ResteasyCommonProcessor] (build-17) Quarkus detected the need of REST JSON support but you have not provided the necessary JSON extension for this. You can visit https://quarkus.io/guides/rest-json for more information on how to set one.
2021-02-03 19:05:07,442 INFO  [io.quarkus] (Quarkus Main Thread) inventory-ms-quarkus 1.0.0-SNAPSHOT on JVM (powered by Quarkus 1.11.1.Final) started in 1.418s. Listening on: http://localhost:8080
2021-02-03 19:05:07,444 INFO  [io.quarkus] (Quarkus Main Thread) Profile dev activated. Live Coding activated.
2021-02-03 19:05:07,444 INFO  [io.quarkus] (Quarkus Main Thread) Installed features: [agroal, cdi, jdbc-mysql, mutiny, narayana-jta, resteasy, smallrye-context-propagation]
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
java -jar target/inventory-ms-quarkus-1.0.0-SNAPSHOT-runner.jar -Dquarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/inventorydb?useSSL=true -Dquarkus.datasource.username=dbuser -Dquarkus.datasource.password=password
```

If it is run successfully, you will see something like below.

```
$ java -jar -Dquarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/inventorydb?useSSL=true -Dquarkus.datasource.username=dbuser -Dquarkus.datasource.password=password -jar target/inventory-ms-quarkus-1.0.0-SNAPSHOT-runner.jar
__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2021-02-03 19:17:22,404 INFO  [io.quarkus] (main) inventory-ms-quarkus 1.0.0-SNAPSHOT on JVM (powered by Quarkus 1.11.1.Final) started in 6.010s. Listening on: http://0.0.0.0:8080
2021-02-03 19:17:22,423 INFO  [io.quarkus] (main) Profile prod activated. 
2021-02-03 19:17:22,423 INFO  [io.quarkus] (main) Installed features: [agroal, cdi, jdbc-mysql, mutiny, narayana-jta, resteasy, smallrye-context-propagation]
```
