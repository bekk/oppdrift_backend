package no.bekk.oppdrift.backend

import io.swagger.v3.oas.annotations.Operation
import org.hibernate.Remove
import org.springframework.context.annotation.Description
import org.springframework.web.bind.annotation.*
import javax.sql.DataSource

@RestController
@RequestMapping("/api/users")
class UserController(val userRepository: UserRepository) {
    @GetMapping
    @Operation(summary = "Returns all users", description = "Fetches all user objects from the database.")
    fun getUsers(): List<User> = userRepository.getUsers()

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Int): User? = userRepository.getUser(id)

    @DeleteMapping("/{id}")
    fun removeUser(@PathVariable id: Int) = userRepository.removeUser(id)

    @PostMapping
    fun addUser(@RequestBody user: User) = userRepository.addUser(user)

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Int, @RequestBody user: User) = userRepository.updateUser(user.copy(id = id))
}

