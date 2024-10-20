package no.bekk.oppdrift.backend

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Repository

@Configuration
@EnableScheduling
class JobConfig (@Autowired private val repository: UserRepository) {

    @Scheduled(fixedDelay = 60000, initialDelay = 500)
    fun reportNumberOfUsersEveryMinute() {
        val allUsers = repository.getUsers()
        println("Users in database: ${allUsers.count()}")
    }
}