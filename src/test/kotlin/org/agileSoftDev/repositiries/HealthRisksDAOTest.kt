package org.agileSoftDev.repositiries

import org.agileSoftDev.domain.db.Diets
import org.agileSoftDev.domain.db.HealthIndicators
import org.agileSoftDev.domain.db.HealthRisks
import org.agileSoftDev.domain.db.Users
import org.agileSoftDev.domain.repository.HealthIndicatorDAO
import org.agileSoftDev.domain.repository.HealthRiskDAO
import org.agileSoftDev.domain.repository.UserDAO
import org.agileSoftDev.helpers.*
import org.agileSoftDev.utills.Healthrisks.DietsAdd
import org.agileSoftDev.utills.Healthrisks.deptAdd
import org.agileSoftDev.utills.Healthrisks.healthriskAdd
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HealthRisksDAOTest {

    private val healthRiskDAO =  HealthRiskDAO()
    private val healthIndicatorDAO = HealthIndicatorDAO()
    private val userDAO = UserDAO()

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }


    fun setupAllSchemas() {

        SchemaUtils.create(Users)
        SchemaUtils.create(Diets)
        SchemaUtils.create(HealthRisks)
        SchemaUtils.create(HealthIndicators)

        for(user in users){
            userDAO.addUser(user)

        }
        for(diet in diets){
            DietsAdd(diet)
        }
        for(dept in departments){

            deptAdd(dept)
        }

        for(healthrisk in healthrisks){
            healthriskAdd(healthrisk)
        }
        healthIndicatorDAO.addHealthIndicators(healthindicators)
    }

    @Nested
    inner class HealthRiskReadOperations {

        @Test
        fun `get possible healthRisks for users`() {

            transaction {
                setupAllSchemas()
                HealthRisks.selectAll()
                var healthrisks = healthRiskDAO.getPossibleHealthRisks(3)

                println(healthrisks)
                assertEquals(5, healthrisks?.size)

            }


        }
        @Test
        fun `get empty list for healthy users`() {

            transaction {
                setupAllSchemas()

                var healthrisks = healthRiskDAO.getPossibleHealthRisks(2)

                println(healthrisks)
                assertEquals(0, healthrisks?.size)

            }


        }
        @Test
        fun `Successfully retrieve a renal failure risk, where gfr is less than 30`(){

            //User with id  3 has gfr less than 30
            transaction {
                setupAllSchemas()
                var healthrisks = healthRiskDAO.getPossibleHealthRisks(3)
                assert(healthrisks!!.contains("Renal failure"))
            }

        }

        @Test
        fun `get null  for users with no indicators`() {

            transaction {
                setupAllSchemas()

                var healthrisks = healthRiskDAO.getPossibleHealthRisks(1)

                println(healthrisks)
                assertEquals(null, healthrisks)

            }


        }

    }


}