# Getting Started

### Building and starting application 

- Docker:

    Application can be run both on docker, or inside IDE. 

    Out of the box application is suitable for docker, simply go into /docker/ folder and run
    `docker-compose up --build -d`
    to build and deploy both postgres database and application. After application has started, it will be accepting http requests @ http://localhost:4002 url.


- Docker postgres + IDE:

  In `docker-compose.yml` file, service `product-complaint-service` can be commented out in order to run only the postgres database on docker, and service on local env.
  It is also required to run the application with `dev` profile.


- IDE + postgres

  In `application-local.properties` file correct datasource config should be provided pointing to existing postgres database.
  Also it is required to run the application with `local` profile.


- IDE + h2

  It is also possible to run entire service on H2 database. Simply run the application with `h2` profile.

### Ready to use HTTP requests

In `/api-requests` folder ready to use http-request are provided. It contains three files, each designed for specific http request provided with examples.

### API specification

SwaggerUI available @ http://localhost:4002/swagger-ui.html

OpenApi description available @ http://localhost:4002/v3/api-docs
