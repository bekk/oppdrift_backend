# Del 3 - løsning fra scratch - endepunkt

Ta utgangspunkt i det som ble gjort i [del 1 - nytt prosjekt](./../del_1/nytt-prosjekt.md) og [del 2 - database integrasjon](./../del_1/database-integrasjon.md)

I dag skal vi implementere REST-endepunkt i Spring-applikasjonen vår. 
Når vi er ferdig med denne delen vil vi ha en applikasjon med en database, database migrasjoner og et REST-endepunkt for å hente ut, oppdatere, legge til og fjerne brukere.
Eksempelprosjekt og løsningsforslag for denne delen finner du på: https://github.com/veiset/kotlin-spring-flyway-rest-example

## Spring Dependency Injection

Spring kommer med et dependency injection rammeverk. 
De tre mest sentrale konseptene i Spring sitt dependency injection rammeverk er `@ComponentScan`, `@Bean` og `@Autowired`. 

Avhengiheter som kan bli brukt i dependency injection er en `@Bean`. 
Det kan være en f.eks tjeneste for å hente ut brukere, ett konfigurasjonsobjekt, eller en database-tilkobling. Eller hva som helst annet.
For å kunne oppdage alle `@Bean` som er tilgjengelig i applikasjonen kan man bruke `@ComponentScan` for å lete igjennom kodebasen for
å ting som er market som `@Bean`. 
Når man ønsker å ta i bruk en avhengighet kan man bruke `@Autowired` for å få tak i avhengiehten.

Oppsumert:

`@Bean` - Avhengigheter man kan trekke inn

`@Autowired` - Måten man får inn en avhengighet på

`@ComponentScan` - En måte å finne avhengiehter i applikasjonen


Vi bruker Spring boot, så en del av dette er gjemt for oss. Spring Boot vil (by default) annotere applikasjonen vår med `@ComponentScan`,
og lage `Beans` av ting man typisk trenger.
Dersom vi har en avhengighet til en database og satt verdier i `application.config` vil Spring Boot automatisk opprette en `Bean` 
for denne database-tilkoblingen. Denne kan vi dra inn ved hjelp av Dependency Injection.
Videre trenger heller ikke å si at databasen skal bli `@Autowired`, da Spring Boot forstår det når `MyClass` er annotert som en `@RestController`.

Mye blir gjemt, så alt vi trenger for å dra inn en database i et endepunkt er:

```kotlin
@RestController
class MyClass(val database: Database) { }
```


## Repository vs RestController

Men hva er egentlig `@RestController`? ... `@Component` etc


## javax.sql.DataSource

Eksempel på bruk av `DataSource`

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

# UserRepository

Lag klassen `UserRepository` og ta inn DataSource ved hjelp av dependency injection.

```kotlin
@Repository
class UserRepository(val dataSource: DataSource) {
    
}
```

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

## RestController
```kotlin
@RequestMapping

@GetMapping
@DeleteMapping
@PostMapping
@PutMapping
```

```kotlin
@RestController
@RequestMapping("/api/users")
class UserController(val repository: UserRepository) {

}
```


```kotlin
@RestController
@RequestMapping("/api/users")
class UserController(val userRepository: UserRepository) {
    @GetMapping
    fun getUsers(): List<User> =
        userRepository.getUsers()
}
```

Implementer funksjoner som er annotert med `@PostMapping`, `@PutMapping` og `@DeleteMapping` for å oppfylle 
funksjonene i http-fila under (kopier koden under og legg den til som `users.http` i prosjektet):

```http request
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

