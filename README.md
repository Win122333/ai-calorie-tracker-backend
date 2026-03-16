🛠 Технологический стек
Java 21 & Spring Boot 3.4+

Spring Security (OAuth2 Client & Resource Server)

Keycloak (Identity Provider)

PostgreSQL (у каждого сервиса своя БД)

Liquibase (миграции схем БД)

Docker & Docker Compose

Thymeleaf (UI менеджера)

Структура проекта 

```text
ai-calorie-tracker-backend/
|-- .gitignore
|-- README.md
|-- docker-compose.yml
|-- fitassistant-realm.json
|-- pom.xml
|
|-- common/
|   |-- pom.xml
|   `-- src/main/java/ru/vsu/cs/dto/
|       |-- Gender.java
|       |-- LoginDto.java
|       |-- ProfileRegistrationDto.java
|       |-- RegistrationDto.java
|       |-- RequestProfileDto.java
|       `-- ResponseProfileDto.java
|
`-- profile-service/
    |-- pom.xml
    `-- src/main/
        |-- java/ru/vsu/cs/fitAssistant/profile/
        |   |
        |   |-- config/
        |   |   |-- KeycloakRoleConverter.java
        |   |   `-- Security.java
        |   |
        |   |-- controller/
        |   |   |-- AuthController.java
        |   |   `-- ProfileController.java
        |   |
        |   |-- entity/
        |   |   |-- ActivityTarget.java
        |   |   |-- Gender.java
        |   |   |-- ProfileEntity.java
        |   |   |-- TargetEntity.java
        |   |   `-- TokenResponseDto.java
        |   |
        |   |-- mapper/
        |   |   `-- Mapper.java
        |   |
        |   |-- repository/
        |   |   |-- ProfileRepository.java
        |   |   `-- TargetRepository.java
        |   |
        |   `-- service/
        |       |-- AuthService.java
        |       `-- ProfileService.java
        |
        `-- resources/
            |-- application-standalone.yml
            `-- db/
                |-- changelog/
                |   |-- master-changelog.xml
                |   `-- V1/
                |       |-- accumulator-changelog.xml
                |       |-- V1-create-table-changelog.sql
                |       `-- V1-init-target-table.xml
                `-- data/
                    `-- init-target.csv
```