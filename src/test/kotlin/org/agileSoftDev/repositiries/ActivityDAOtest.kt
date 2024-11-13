package org.agileSoftDev.repositiries

import org.agileSoftDev.domain.db.Activities
import org.agileSoftDev.domain.db.Users
import org.agileSoftDev.domain.repository.ActivityDAO
import org.agileSoftDev.domain.repository.UserDAO
import org.agileSoftDev.helpers.activities
import org.agileSoftDev.helpers.users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ActivityDAOtest {
    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    fun populateDatabase(): ActivityDAO {
       SchemaUtils.create(Users)
        val userDAO =UserDAO()
        for(user in users){
            userDAO.addUser(user)
        }
        SchemaUtils.create(Activities)
        val activityDAO = ActivityDAO()
        for (activity in activities) {
            activityDAO.saveActivity(activity)
        }
        return activityDAO
    }
    @Nested
    inner class ActivitiesReadOperations{
        @Test
        fun `retrieve all activities`(){
            transaction {
                var activitiesPopulated = populateDatabase()
                assertEquals(4, activitiesPopulated.getAllActivities().size)
            }
        }
        @Test
        fun `Succesfully retrieve all activities by userID`() {
            transaction {
                var activitiesPopulated = populateDatabase()
                var act3 = activitiesPopulated.getActivityByUser(3).size
                var act1 = activitiesPopulated.getActivityByUser(1).size
                var act2 = activitiesPopulated.getActivityByUser(2).size
                assertEquals(2, act3)
                assertEquals(0, act2)
                assertEquals(1, act1)
            }
        }
    }

    @Nested
    inner class  ActivitiesDeleteOperations{

        @Test
        fun `delete activities by userID`(){
            transaction {
                var activitiesPopulated = populateDatabase()
                assertEquals(2, activitiesPopulated.getActivityByUser(3).size)
                activitiesPopulated.deleteActivityByUser(3,3)
                assertEquals(1,activitiesPopulated.getActivityByUser(3).size)
            }
        }
    }
}