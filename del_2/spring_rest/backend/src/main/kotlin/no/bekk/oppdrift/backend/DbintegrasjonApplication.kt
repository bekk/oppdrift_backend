package no.bekk.oppdrift.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.sql.DataSource

@SpringBootApplication
class DbintegrasjonApplication

fun main(args: Array<String>) {
    runApplication<DbintegrasjonApplication>(*args)
}

@RestController
class UserController(val dataSource: DataSource) {

    @GetMapping("hei")
    fun hei(): List<String> {
        val users = dataSource.connection.use { connection ->
            val sql = "SELECT * FROM Users";
            val resultSet = connection.createStatement().executeQuery(sql)
            val usernames = mutableListOf<String>()
            while (resultSet.next()) {
                usernames.add(resultSet.getString("username"))
            }
            usernames
        }
        return users
    }
}

@Configuration
@EnableScheduling
class JobConfig {

    @Scheduled(fixedDelay = 2000, initialDelay = 500)
    fun letsGo() {
        //println("Hei")
    }
}