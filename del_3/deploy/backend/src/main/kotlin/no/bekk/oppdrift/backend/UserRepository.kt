package no.bekk.oppdrift.backend

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class UserRepository(val dataSource: DataSource) {
    fun getUsers(): List<User> = dataSource.connection.use { connection ->
        val sql = "SELECT * FROM Users"
        val rs = connection.createStatement().executeQuery(sql)
        val users = mutableListOf<User>()
        while (rs.next()) {
            val user = User(rs.getInt("id"), rs.getString("username"), rs.getString("title"))
            users.add(user)
        }
        users
    }

    fun getUser(id: Int): User?  = dataSource.connection.use {connection ->
        val rs = connection
            .prepareStatement("SELECT * FROM Users WHERE id = ?")
            .also { it.setInt(1, id) }
            .executeQuery()
        if(rs.next()){
            User(rs.getInt("id"), rs.getString("username"), rs.getString("title"))
        }else null
    }

    fun addUser(user: User): Unit = dataSource.connection.use {connection ->
        connection.prepareStatement("INSERT INTO Users (id, title, username) VALUES (?, ?, ?)")
            .also {
                it.setInt(1, user.id)
                it.setString(2, user.title)
                it.setString(3, user.username)
            }
            .executeUpdate()
    }

    fun removeUser(id: Int): Unit = dataSource.connection.use {connection ->
        connection.prepareStatement("DELETE FROM Users WHERE id = ?")
            .also { it.setInt(1, id) }
            .executeUpdate()
    }

    fun updateUser(user: User): Unit = dataSource.connection.use {connection ->
        connection.prepareStatement("UPDATE Users SET title = ?, username = ? WHERE id = ?")
            .also {
                it.setString(1, user.title)
                it.setString(2, user.username)
                it.setInt(3, user.id)
            }
    }
}

@Schema(description = "Model for a User")
data class User(
    @field:Schema(description = "ID of the user to update", example = "1")
    val id: Int,
    val title: String,
    val username: String,
)