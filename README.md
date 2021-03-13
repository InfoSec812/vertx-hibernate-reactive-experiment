# Vert.x + Hibernate Reactive Proof Of Concept

This is a small proof-of-concept on how one might be able to use Vert.x in
conjunction with Hibernate Reactive to leverage a modern ORM with the reactive
toolkit that is Vert.x. 

## Overview

1. This repository leverages OpenAPI and OpenAPI Generator to create the JPA entities from an OpenAPI Specification file.
   * You will not find any Entity/Pojo class files until **AFTER** you run `mvn generate-sources`
   * You can view the spec file [here](openapi.yml)
1. The repository has an example of how you can implement a Repository Pattern with Mutiny and Hibernate Reactive
   * The Repository base class can be found [here](src/main/java/com/zanclus/vertx/db/Repository.java)
   * An example implementation can be found [here](src/main/java/com/zanclus/vertx/db/TodoRepository.java)
