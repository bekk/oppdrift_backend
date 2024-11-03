package no.bekk.oppdrift.backend

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Repository
class UserRepository {

    private val users = ConcurrentHashMap<Int, User>()
    private val idGenerator = AtomicInteger(1)

    fun getUsers(): List<User> = users.values.toList()

    fun getUser(id: Int): User? = users[id]

    fun addUser(user: User): User {
        val newUser = user.copy(id = idGenerator.getAndIncrement())
        users[newUser.id] = newUser
        return newUser
    }

    fun removeUser(id: Int): Boolean = users.remove(id) != null

    fun updateUser(user: User): Boolean {
        return if (users.containsKey(user.id)) {
            users[user.id] = user
            true
        } else {
            false
        }
    }
}

@Schema(description = "Model for a User")
data class User(
    @field:Schema(description = "ID of the user", example = "1")
    val id: Int = 0,  // Default to 0; ID will be assigned when added to the repository
    val title: String,
    val username: String,
)