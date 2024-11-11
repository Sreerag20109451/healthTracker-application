package org.agileSoftDev.repositiries

import org.agileSoftDev.domain.HealthIndicator
import org.agileSoftDev.domain.db.HealthIndicators
import org.agileSoftDev.domain.db.Users
import org.agileSoftDev.domain.repository.HealthIndicatorDAO
import org.agileSoftDev.domain.repository.UserDAO
import org.agileSoftDev.helpers.healthindicators
import org.agileSoftDev.helpers.users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested

class healthIndicatorDAOTest {

    private val healthIndicatorDAO = HealthIndicatorDAO()
    private val userDAO = UserDAO()

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    fun setUpScheamas() : HealthIndicatorDAO{


        SchemaUtils.create(Users)
        SchemaUtils.create(HealthIndicators)
        for (user in users) {

            userDAO.addUser(user)

        }
        healthIndicatorDAO.addHealthIndicators(healthindicators)
        return  healthIndicatorDAO


    }



}