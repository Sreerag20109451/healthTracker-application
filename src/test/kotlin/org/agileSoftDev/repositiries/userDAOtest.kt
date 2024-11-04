package org.agileSoftDev.repositiries

import org.agileSoftDev.domain.User
import org.agileSoftDev.domain.db.Users
import org.agileSoftDev.domain.repository.UserDAO
import org.agileSoftDev.helpers.users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


val user1 = users[0]
val user2 = users[1]
val user3 = users[2]

class userDAOtest {
    companion object {
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class ReadUsers {
        @Test
        fun `getting all users from a populated table returns all rows`() {
            transaction {
                val userDAO = populateUserTable()
                //Assertions
                assertEquals(3, userDAO.allUsers().size)
            }

        }

        @Test
        fun `getting null for an invalid user id`() {

            transaction {
                val userDAO = populateUserTable()
                //Assertion
                assertEquals(null, userDAO.getUserById(17))


            }

        }

        @Test
        fun `Getting correct users on user IDs`() {

            transaction {

                val userDAO = populateUserTable()
                //Assertions0
                assertEquals(user1, userDAO.getUserById(user1.id))
                assertEquals(user2, userDAO.getUserById(user2.id))
                assertEquals(user3, userDAO.getUserById(user3.id))
            }
        }
        @Test
        fun `Empty database returns no rows`(){

            transaction {
                val userDAO = UserDAO()
                SchemaUtils.create(Users)
                assertEquals(0, userDAO.allUsers().size)

            }

        }
    }


    @Nested
    inner class CreateUsers {
        @Test
        fun `Users can be added into the table and retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                assertEquals(3, userDAO.allUsers().size)
                assertEquals(user1, userDAO.getUserById(user1.id))
                assertEquals(user2, userDAO.getUserById(user2.id))
                assertEquals(user3, userDAO.getUserById(user3.id))
            }
        }
    }

    @Nested
    inner class deleteUsers() {
        @Test
        fun `Deleting a user by a non existent id results in no deletion`() {

            transaction {

                val userDAO = populateUserTable()
                val size = userDAO.allUsers().size
                userDAO.deleteUserById(17)
                assertEquals(size, userDAO.allUsers().size)
            }
        }

        @Test
        fun `Deleting an existing user results in deletion `() {
            transaction {
                val userDAO = populateUserTable()
                val size = userDAO.allUsers().size
                userDAO.deleteUserById(user1.id)
                assertEquals(null, userDAO.getUserById(user1.id))
                assertEquals(size - 1, userDAO.allUsers().size)
            }
        }
    }

    @Nested
    inner class UpdateUser {

        @Test
        fun `Updating a non existent user results in no updation`() {

            transaction {
                var userDAO = populateUserTable()
                var size = userDAO.allUsers().size

                var user = User(id = 34, name = "my_name", email = "myEmail@email.com", role = "user")

                userDAO.updateUser(45, user)
                assertEquals(size, userDAO.allUsers().size)
            }
        }

        @Test
        fun `updating an existing user works fine`() {
     transaction {
         var userDAO = populateUserTable()
         var size = userDAO.allUsers().size

         var user = User(user1.id, name = "UpdateName", email = "UpdateEmail@email.com", role = "user")

         userDAO.updateUser(user1.id, user)

         assertEquals("UpdateName", userDAO.getUserById(user1.id)?.name)
         assertEquals("UpdateEmail@email.com", userDAO.getUserById(user1.id)?.email)
     }}
    }

     fun populateUserTable(): UserDAO {
        SchemaUtils.create(Users)
        val userDAO = UserDAO()
        userDAO.addUser(user1)
        userDAO.addUser(user2)
        userDAO.addUser(user3)
        return userDAO
    }
}