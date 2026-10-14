# Del 3 - løsning fra scratch - endepunkt

> [!NOTE]
> Hensikten med denne øvelsen er å bli bedre kjent med _dependency injection_
> i Spring. For å lage en standard web-applikasjon brukes annotasjoner (de som begynner med `@`)
> for å knytte de ulike delene av applikasjonen sammen.

Ta utgangspunkt i det som ble gjort i [del 0 - nytt prosjekt](./../del_0/nytt-prosjekt.md) og [del 2 - database integrasjon](./../del_1/database-integrasjon.md)

(Du kan finne en ferdig løsning på oppgaven fra forrige økt [her](./spring_rest/backend/).)

I dag skal vi implementere REST-endepunkt i Spring-applikasjonen vår. 
Når vi er ferdig med denne delen vil vi ha en applikasjon med en database, 
database migrasjoner og REST-endepunkt for å hente ut, oppdatere, legge til og fjerne brukere.

Løsningsforslag finner du underveis i oppgavene.

## Spring Dependency Injection

Spring kommer med et rammeverk for å håndtere avhengigheter, et såkalt *dependency injection*-rammeverk.
Spring holder styr på en samling objekter, såkalte *beans*, og sørger for at de blir opprettet og
sendt inn der de trengs.

De mest sentrale konseptene er:

`@Component` (og varianter som `@Service`, `@Repository` og `@RestController`) – Marker en klasse slik at Spring
oppretter én instans av den og gjør den tilgjengelig som en bean.

`@Bean` – Brukes på en *metode* i en `@Configuration`-klasse når du vil lage en bean selv, f.eks. av en klasse
fra et bibliotek du ikke kan annotere.

`@ComponentScan` – Får Spring til å lete igjennom kodebasen etter klasser annotert med `@Component` og variantene.

Injeksjon via konstruktør – Når Spring oppretter en klasse, fyller den automatisk inn parameterne i konstruktøren
med beans av riktig type. (I eldre kode ser du ofte `@Autowired` for dette, men det trengs ikke når klassen har én konstruktør.)

Vi bruker Spring Boot, så en del av dette er gjemt for oss. `@SpringBootApplication` inkluderer `@ComponentScan`,
og Spring Boot lager automatisk beans av ting man typisk trenger.
Dersom vi har en avhengighet til en database og har satt verdier i `application.properties`, vil Spring Boot automatisk
opprette en `DataSource`-bean for denne database-tilkoblingen. Denne kan vi dra inn ved hjelp av dependency injection.

Alt vi trenger for å dra inn en database i et endepunkt er derfor:

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

For å slippe å gjenta koden som gjør en rad fra databasen om til en `User`, kan vi lage en
*extension function* på `ResultSet`:

```kotlin
import java.sql.ResultSet

fun ResultSet.toUser() = User(
    id = getInt("id"),
    title = getString("title"),
    username = getString("username"),
)
```

```kotlin
// Select All
val users = dataSource.connection.use { connection ->
    val rs = connection.createStatement().executeQuery("SELECT * FROM Users")
    val users = mutableListOf<User>()
    while (rs.next()) {
        users.add(rs.toUser())
    }
    users
}
```

```kotlin
// Select query (id er en Int, f.eks. en parameter til funksjonen)
val user = dataSource.connection.use { connection ->
    val rs = connection
        .prepareStatement("SELECT * FROM Users WHERE id = ?")
        .also { it.setInt(1, id) }
        .executeQuery()

    if (rs.next()) rs.toUser() else null
}
```

```kotlin
// Insert query
val user = User(5, "Utvikler", "Vegard")
dataSource.connection.use { connection ->
    connection
        .prepareStatement("INSERT INTO Users (id, title, username) VALUES (?, ?, ?)")
        .also {
            it.setInt(1, user.id)
            it.setString(2, user.title)
            it.setString(3, user.username)
        }
        .executeUpdate()
}
```

> [!TIP]
> `executeUpdate()` returnerer antall rader som ble endret. Det kan du bruke til å finne ut om en bruker
> faktisk fantes når du oppdaterer eller sletter.

> [!NOTE]
> Vi bruker `?` i SQL-en og `setInt`/`setString` i stedet for å lime verdiene rett inn i strengen.
> Det beskytter mot SQL-injection.
>
> Spring har også `JdbcClient`, som fjerner mye av denne koden. Prøv den gjerne når du har fått det til å virke med `DataSource`:
> ```kotlin
> @Repository
> class UserRepository(val jdbcClient: JdbcClient) {
>     fun getUsers(): List<User> =
>         jdbcClient.sql("SELECT * FROM Users")
>             .query { rs, _ -> rs.toUser() }
>             .list()
> }
> ```

# Implementasjon og oppgaver


## UserRepository

**Oppgave**:

Lag klassen `UserRepository` og ta inn DataSource ved hjelp av dependency injection.

> [!IMPORTANT]
> Fra tidligere deler har du sannsynligvis allerede en `data class User(val name: String, val age: Int)`
> og en `UserController` med endepunktet `/user`. Slett dem (eller gi dem nye navn) før du går videre –
> ellers får du kompileringsfeilen `Redeclaration` når du lager de nye klassene med samme navn.
> Lag `User`-klassen fra eksempelet over i stedet.

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
    fun getUsers(): List<User> = TODO()  // En metode som returnerer alle brukerne
    fun getUser(id: Int): User? = TODO()  // En metode som returnerer en enkelt bruker, eller null om den ikke finnes
    fun addUser(user: User): Unit = TODO()  // En metode som legger til en bruker
    fun removeUser(id: Int): Boolean = TODO()  // Fjerner en bruker basert på id. Returnerer true om brukeren fantes
    fun updateUser(user: User): Boolean = TODO()  // Oppdaterer en bruker. Returnerer true om brukeren fantes
}
```

<details>
  <summary>Løsningsforslag</summary>
  <p>

```kotlin
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import javax.sql.DataSource

data class User(
    val id: Int,
    val title: String,
    val username: String,
)

fun ResultSet.toUser() = User(
    id = getInt("id"),
    title = getString("title"),
    username = getString("username"),
)

@Repository
class UserRepository(val dataSource: DataSource) {

    fun getUsers(): List<User> =
        dataSource.connection.use { connection ->
            val rs = connection.createStatement().executeQuery("SELECT * FROM Users")
            val users = mutableListOf<User>()
            while (rs.next()) {
                users.add(rs.toUser())
            }
            users
        }

    fun getUser(id: Int): User? =
        dataSource.connection.use { connection ->
            val rs = connection
                .prepareStatement("SELECT * FROM Users WHERE id = ?")
                .also { it.setInt(1, id) }
                .executeQuery()
            if (rs.next()) rs.toUser() else null
        }

    fun addUser(user: User) {
        dataSource.connection.use { connection ->
            connection
                .prepareStatement("INSERT INTO Users (id, title, username) VALUES (?, ?, ?)")
                .also {
                    it.setInt(1, user.id)
                    it.setString(2, user.title)
                    it.setString(3, user.username)
                }
                .executeUpdate()
        }
    }

    fun removeUser(id: Int): Boolean =
        dataSource.connection.use { connection ->
            val rowsDeleted = connection
                .prepareStatement("DELETE FROM Users WHERE id = ?")
                .also { it.setInt(1, id) }
                .executeUpdate()
            rowsDeleted > 0
        }

    fun updateUser(user: User): Boolean =
        dataSource.connection.use { connection ->
            val rowsUpdated = connection
                .prepareStatement("UPDATE Users SET title = ?, username = ? WHERE id = ?")
                .also {
                    it.setString(1, user.title)
                    it.setString(2, user.username)
                    it.setInt(3, user.id)
                }
                .executeUpdate()
            rowsUpdated > 0
        }
}
```
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
henter ut alle brukerne (`UserRepository` er den du lagde over, her vist med bare `getUsers`):

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
                users.add(resultSet.toUser())
            }
            users
        }
}

```

**Oppgave**:

Implementer funksjoner som er annotert med `@PostMapping`, `@PutMapping` og `@DeleteMapping` for å oppfylle 
funksjonene i http-fila under (kopier koden under og legg den til som `users.http` i prosjektet).
I IntelliJ kan du trykke på den grønne pila ved siden av hver request for å kjøre den.

> [!NOTE]
> Databasen lagres til fil (fra del 2), så endringene blir liggende mellom hver kjøring. Kjører du fila to ganger,
> finnes ikke bruker 1 lenger (404), og POST av bruker 3 feiler fordi id-en allerede er i bruk.
> Stopp applikasjonen og slett `testdb.mv.db` for å starte på nytt.

Tips: `@PathVariable` henter verdier fra URL-en (som `id` i `/api/users/{id}`), og `@RequestBody` gjør om JSON i
requesten til en `User`. Bruk `ResponseEntity` for å svare med riktig statuskode, f.eks. `404 Not Found` når brukeren ikke finnes.


```
### Get all the users
GET http://localhost:8080/api/users

### Get one user
GET http://localhost:8080/api/users/1

### Add new user
POST http://localhost:8080/api/users
Content-Type: application/json

{ "id": 3, "title": "Manager", "username": "User" }

### Update user
PUT http://localhost:8080/api/users/1
Content-Type: application/json

{ "id": 1, "title": "Manager", "username": "Vegard" }

### Remove a user
DELETE http://localhost:8080/api/users/1

```

<details>
  <summary>Løsningsforslag</summary>
  <p>

```kotlin
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/users")
class UserController(val userRepository: UserRepository) {

    @GetMapping
    fun getUsers(): List<User> = userRepository.getUsers()

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Int): ResponseEntity<User> {
        val user = userRepository.getUser(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user)
    }

    @PostMapping
    fun addUser(@RequestBody user: User): ResponseEntity<User> {
        userRepository.addUser(user)
        return ResponseEntity.created(URI.create("/api/users/${user.id}")).body(user)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Int, @RequestBody user: User): ResponseEntity<Unit> {
        if (id != user.id) return ResponseEntity.badRequest().build()
        return if (userRepository.updateUser(user)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun removeUser(@PathVariable id: Int): ResponseEntity<Unit> =
        if (userRepository.removeUser(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
}
```
  </p>
</details>

## Oppsett av OpenAPI

Legg til avhengigheten til springdoc i `build.gradle.kts`. springdoc 3.x er versjonen som virker med Spring Boot 4.

```kotlin
implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.1.1")
```

Start applikasjonen på nytt. Swagger-siden når du på: http://localhost:8080/swagger-ui.html
