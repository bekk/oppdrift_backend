# Del 3 - løsning fra scratch - endepunkt

Ta utgangspunkt i det som ble gjort i [del 0 - nytt prosjekt](./../del_0/nytt-prosjekt.md) og [del 2 - database integrasjon](./../del_1/database-integrasjon.md)

I dag skal vi implementere REST-endepunkt i Spring-applikasjonen vår. 
Når vi er ferdig med denne delen vil vi ha en applikasjon med en database, 
database migrasjoner og REST-endepunkt for å hente ut, oppdatere, legge til og fjerne brukere.

Eksempelprosjekt og løsningsforslag for denne delen finner du på: 
https://github.com/veiset/kotlin-spring-flyway-rest-example

## Spring Dependency Injection

Spring kommer med et rammeverk for å håndtere avhengigheter, et såkalt *dependency injection* rammeverk.  
De tre mest sentrale konseptene i Spring sitt dependency injection rammeverk er `@ComponentScan`, `@Bean` og `@Autowired`. 

I Spring definerer man en avhengighet som kan bli brukt i dependency injection som en `@Bean`. 
Det kan være en f.eks tjeneste for å hente ut brukere, ett konfigurasjonsobjekt, eller en database-tilkobling. 
Eller hva som helst annet.
For å kunne oppdage alle `@Bean` som er tilgjengelig i applikasjonen kan man bruke `@ComponentScan` for å lete 
igjennom kodebasen for ting som er market som `@Bean`. 
Når man ønsker å ta i bruk en avhengighet kan man bruke `@Autowired` for å få tak i avhengiehten.


Oppsumert:
`@Bean` - Avhengigheter man kan bruke i applikasjonen

`@Autowired` - Måten man får inn en avhengighet på

`@ComponentScan` - En måte å finne avhengiehter i applikasjonen


Vi bruker Spring boot, så en del av dette er gjemt for oss. Spring Boot vil (by default) annotere applikasjonen 
vår med `@ComponentScan` og lage `Beans` av ting man typisk trenger.
Dersom vi har en avhengighet til en database og vi har satt verdier i `application.config` vil Spring Boot automatisk 
opprette en `Bean` for denne database-tilkoblingen. Denne kan vi dra inn ved hjelp av Dependency Injection.

Siden vi bruker Spring Boot trenger heller ikke å si at databasen skal bli `@Autowired`, 
da Spring Boot forstår det når `MyClass` er annotert med Spring annotasjon f.eks `@RestController`.

Mye blir gjemt, så alt vi trenger for å dra inn en database i et endepunkt er:

```kotlin
@RestController
class MyClass(val dataSource: DataSource) 
```

## javax.sql.DataSource

DataSource er et abstraksjonslag mot databaser, den inneholder logikk for å håndere tilkobling og kommunikasjon mot en database.
Under er noen eksempler på hvordan vi kan bruke `DataSource` til å snakke med en database:

```kotlin
data class User(
    val id: Int,
    val title: String,
    val username: String,
)
```

```kotlin
// Select All
val users = dataSource.connection.use { connection ->
    val sql = "SELECT * FROM Users"
    val rs = connection.createStatement().executeQuery(sql)
    val users = mutableListOf<User>()
    while (rs.next()) {
        val user = User(rs.getInt("id"), rs.getString("username"), rs.getString("title"))
        users.add(user)
    }
    users
}

```

```kotlin
// Select query
val user = dataSource.connection.use { connection ->
    val rs = connection
        .prepareStatement("SELECT * FROM Users where id = ?")
        .also { it.setInt(1, id) }
        .executeQuery()

    if (rs.next()) {
        User(r.getInt("id"), r.getString("username"), r.getString("title"))
    } else null
}
```

```kotlin
// Insert query
val user = User(5, "Utvikler", "Vegard")
dataSource.connection.use { connection ->
    connection
        .prepareStatement("INSERT INTO Users (id, title, username) VALUES ( ?, ?, ? )")
        .also {
            it.setInt(1, user.id)
            it.setString(2, user.title)
            it.setString(3, user.username)
        }
        .executeUpdate()
}
```

# Implementasjon og oppgaver


## UserRepository

**Oppgave**:

Lag klassen `UserRepository` og ta inn DataSource ved hjelp av dependency injection.

```kotlin
@Repository
class UserRepository(val dataSource: DataSource) {
    
}
```

**Oppgave**: 

Bruk det du har lært om `DataSource` for å implementere metodene:

```kotlin
@Repository
class UserRepository(val dataSource: DataSource) {
    fun getUsers(): List<User>  // En metode som returnerer alle brukerne
    fun getUser(): User?        // En metode som returnerer en enkelt bruker
    fun addUser(user: User)     // En metode som legger til en bruker
    fun removeUser(id: Int)     // En metode som fjerner en bruker baser på id
    fun updateUser(user: User)  // En metode som oppdaterer en bruker
}
```

<details>
  <summary>Løsningsforslag</summary>
  <p>

[UserRepository.kt](https://github.com/veiset/kotlin-spring-flyway-rest-example/blob/main/src/main/kotlin/org/veiset/kotlinspringflywayrestexample/UserRepository.kt)
  </p>
</details>

## RestController

I Spring bruker vi annotasjonen `@RestController` på en klasse for å fortelle til Spring at dette er 
en klasse som håndterer HTTP requests og HTTP responses. For å si hvilke requests vi skal håndtere
bruker vi annotasjonen `@RequestMapping(path)`. Under er et eksempel på en klasse som håndterer
requests som kommer inn på `/api/users`.

```kotlin
@RestController
@RequestMapping("/api/users")
class UserController
```

Denne klassen gjør ingenting enda, da vi ikke har definert de forskjellige metodene vi ønsker å bruke.
Dette gjør vi ved å bruke annotasjonene: `@GetMapping`, `@DeleteMapping`, `@PostMapping`, `@PutMapping`.

Ved hjelp av dependency injection og Spring annotasjoner kan vi lage et endepunkt som går mot en database og
henter ut alle brukerne:

```kotlin
@RestController
@RequestMapping("/api/users")
class UserController(val userRepository: UserRepository) {
    @GetMapping
    fun getUsers(): List<User> = userRepository.getUsers()
}

@Repository
class UserRepository(val dataSource: DataSource) {
    fun getUsers(): List<User> =
        dataSource.connection.use { connection ->
            val sql = "SELECT * FROM Users"
            val resultSet = connection.createStatement().executeQuery(sql)
            val users = mutableListOf<User>()
            while (resultSet.next()) {
                users.add(resultSet.userFromResultSet())
            }
            users
        }
}

```

**Oppgave**:

Implementer funksjoner som er annotert med `@PostMapping`, `@PutMapping` og `@DeleteMapping` for å oppfylle 
funksjonene i http-fila under (kopier koden under og legg den til som `users.http` i prosjektet):


```
### Get all the users
http://localhost:8080/api/users

### Get one user
http://localhost:8080/api/users/1

### Remove a user
DELETE http://localhost:8080/api/users/1

### Add new user
POST http://localhost:8080/api/users
Content-Type: application/json

{ "id": 3, "title": "Manager", "username": "User" }

### Update user
PUT http://localhost:8080/api/users/1
Content-Type: application/json

{ "id": 1, "title": "Manager", "username": "Vegard" }

```

<details>
  <summary>Løsningsforslag</summary>
  <p>

[UserController.kt](https://github.com/veiset/kotlin-spring-flyway-rest-example/blob/main/src/main/kotlin/org/veiset/kotlinspringflywayrestexample/UserController.kt)
  </p>
</details>

## Oppsett av OpenAPI

Legg til avhengigheter til openapi og validation i `build.gradle.kts`. 

```kotlin
implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
implementation("org.springframework.boot:spring-boot-starter-validation")
```

For å eksponere en Swagger side legg til følgende i application.properties:

```properties
springdoc.swagger-ui.path=/swagger-ui.html
```

Denne siden når du på: http://localhost:8080/swagger-ui/index.html
