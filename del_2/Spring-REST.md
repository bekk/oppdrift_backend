# Del 3 - løsning fra scratch - endepunkt

Ta utgangspunkt i det som ble gjort i [del 1 - nytt prosjekt](./../del_1/nytt-prosjekt.md) og [del 2 - database integrasjon](./../del_1/database-integrasjon.md)

Eksempel prosjekt med spring, flyway og REST kan du finne her:
https://github.com/veiset/kotlin-spring-flyway-rest-example

## Dependency Injection
```kotlin
class MyClass(val dependency: MyDependency)
```

## Repository vs RestController

repo vs rest


## javax.sql.DataSource

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

