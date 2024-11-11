package org.agileSoftDev.repositiries

import org.agileSoftDev.domain.db.HealthIndicators
import org.agileSoftDev.domain.db.Users
import org.agileSoftDev.domain.repository.HealthIndicatorDAO
import org.agileSoftDev.domain.repository.UserDAO
import org.agileSoftDev.helpers.healthindicators
import org.agileSoftDev.helpers.users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HealthIndicatorDAOTest {

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

    @Nested
     inner class ReadIndicators{

         @Test
         fun `health indicators can be read for users`(){

             transaction {
                 var indicatorDAO = setUpScheamas()
                 var health4 = indicatorDAO.getHealthIndicatorsByUser(4)
                 var health3 = indicatorDAO.getHealthIndicatorsByUser(3)

                 if(health4 != null)   assertEquals(4, userDAO.getUserById(health4.userid)?.id)
                 if(health3 != null) assertEquals(3, userDAO.getUserById(health3.userid)?.id)

             }

         }

        @Test
        fun `health indicators return null for invalid user`(){

            transaction {
                var indicatorDAO = setUpScheamas()
                var healthnull = indicatorDAO.getHealthIndicatorsByUser(211)

                assertEquals(null, healthnull)


            }
        }





    }




}