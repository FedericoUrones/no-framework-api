# Java No-Framework API

This is an API developed in Java 17 using NO-Framework (like Springboot).


# Libraries used

Some libraries were needed to make code more readable.

## SQLite

To store, retrieve, update and delete records in an embedded database.

## Jackson

For serialization of Java Objects to JSON and vice-versa.

## Lombok

For avoiding boilerplate code

## Vavr

Functional library for Java 8+ that provides immutable data types and functional control structures.


# Docker

There's a Dockerfile in the project. Execute following statements if needed to have a running container of the app:

    docker build -t tasks .
    docker run -it --rm -p 8000:8000 tasks

## Tests

You can try executing Postman requests to check if service is working ok. You will find them in postman directory.