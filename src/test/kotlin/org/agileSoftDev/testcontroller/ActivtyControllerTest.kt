package org.agileSoftDev.testcontroller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kong.unirest.core.Unirest
import org.agileSoftDev.config.DBConfig
import org.agileSoftDev.domain.User
import org.agileSoftDev.helpers.ServerContainer
import org.joda.time.DateTime
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ActivtyControllerTest {

    private  val db = DBConfig().getConnection()
    private  val app = ServerContainer.instance
    private val domain = "http://localhost:"+ app.port()

    val adminUser =  User(name= "Sreerag Sathian", email = "20109451@mail.wit.ie", id = 5, role = "admin", password = "admin")

    private fun Login(email: String, password: String): String?{

        var resp = Unirest.post("$domain/api/login").body("{\"email\":\"$email\", \"password\":\"$password\"}")
            .asJson()

        var mapper = jacksonObjectMapper()
        val resultMap: HashMap<String, Any> = mapper.readValue(resp.body.toString())
        return resultMap["token"]  as String
    }

    @Nested
    inner class ActivitiesReadOperations{

        @Test
        fun `Successfully get all activities bu an admin user `(){
            var token = Login("healthAdmin@hospital.com", "admin") //Admin User
            var response = Unirest.get(domain + "/api/activities").header("Authorization", "Bearer " + token).asString()

            assertEquals(200, response.status)
        }

        @Test
        fun `Return 403 error when retrieving activities bu a normal user `(){
            var token = Login("20109451@mail.wit.ie", "test")
            var response = Unirest.get(domain + "/api/activities").header("Authorization", "Bearer " + token).asString()
            assertEquals(403, response.status)
        }

        @Test
        fun  `Get activities by user successfullly by an admin user`(){
            var token = Login("healthAdmin@hospital.com", "admin") //Admin User
            var response = Unirest.get(domain + "/api/users/1/activities").header("Authorization", "Bearer " + token).asString()

            assertEquals(200, response.status)

        }
        @Test
        fun  `Get activities by user successfullly by the same user`(){
            var token = Login("20109451@mail.wit.ie", "test")
            var response = Unirest.get(domain + "/api/users/1/activities").header("Authorization", "Bearer " + token).asString()

            assertEquals(200, response.status)

        } @Test
        fun  `Return 403 error when retrieving activities by a different non-admin user`(){
            var token = Login("20109451@mail.wit.ie", "test")
            var response = Unirest.get(domain + "/api/users/2/activities").header("Authorization", "Bearer " + token).asString()

            assertEquals(403, response.status)

        }

    }

    @Nested
    inner class ActivityPostOperations{

        @Test
        fun `Add an activity for an existing user by an admin and return 201`(){
            var token = Login("healthAdmin@hospital.com", "admin") //Admin User
            var body = "{\"description\":\"Walking\",\"duration\":1, \"calories\":18,\"started\":\"${DateTime.now()}\",\"userId\":1}"
            var response = Unirest.post("$domain/api/users/1/activities").body(body).header("Authorization", "Bearer " + token).asString()
            assertEquals(201, response.status)
        }

    }

    @Nested
    inner  class DeleteActivityOperations(){

        @Test
        fun `return 403 when deleting a different user with a normal user`(){
            var token = Login("20109451@mail.wit.ie", "test")
            var response = Unirest.delete(domain + "/api/users/2/activities/2").header("Authorization", "Bearer " + token).asString()
            assertEquals(403, response.status)

        }
        @Test
        fun `return 200 when deleting a user with the same  normal user`(){
            var token = Login("20109451@mail.wit.ie", "test")
            var response = Unirest.delete(domain + "/api/users/1/activities/3").header("Authorization", "Bearer " + token).asString()
            assertEquals(200, response.status)

        }
        @Test
        fun `return 200 when deleting a user with admin`(){
            var token = Login("healthAdmin@hospital.com", "admin") //Admin User
            var response = Unirest.delete(domain + "/api/users/1/activities/0").header("Authorization", "Bearer " + token).asString()
            assertEquals(200, response.status)

        }
    }


}