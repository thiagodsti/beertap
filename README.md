# BEERTAP #

This is the backend of BeerTap project.

### Requirements

- JDK 8

### Live

You can check the API and test the backend here https://rd-beertap.herokuapp.com/swagger-ui.html

If you need to check the database you can access here http://rd-beertap.herokuapp.com/h2 and use `JDBC URL` = `jdbc:h2:mem:testdb`

### How do I get set up? ###

#### Development

- Build
`./gradlew build`

- Run
`./gradlew bootRun`

- Tests
`./gradlew test`

#### Docker

- build
`docker build -t beertap .`

- run
`docker run -d -p 80:80 beertap`

--------

### Contacts ###

Thiago Diniz da Silveira

+55 48 988416541

thiagods.ti@gmail.com