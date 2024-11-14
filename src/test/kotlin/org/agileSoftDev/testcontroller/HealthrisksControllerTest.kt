package org.agileSoftDev.testcontroller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kong.unirest.core.Unirest
import org.agileSoftDev.config.DBConfig
import org.agileSoftDev.domain.User
import org.agileSoftDev.helpers.ServerContainer
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HealthrisksControllerTest {
    private  val db = DBConfig().getConnection()
    private  val app = ServerContainer.instance
    private val domain = "http://localhost:"+ app.port()

    val adminUser =  User(name= "Sreerag Sathian", email = "20109451@mail.wit.ie", id = 5, role = "admin", password = "admin")

    private fun Login(email: String, password: String): String?{

        var resp = Unirest.post("$domain/api/login").body("{\"email\":\"$email\", \"password\":\"$password\"}")
            .asJson()

        var mapper = jacksonObjectMapper()
        val resultMap : HashMap<String, String> = mapper.readValue(resp.body.toString())

        return resultMap["token"]
    }


    @Nested
    inner class `HealthriskOperations`{

        @Test
        fun `Get health risks successfully from a unhealthy user`(){
            var token = Login("healthAdmin@hospital.com", "admin") //Admin User
            var response = Unirest.get(domain + "/api/users/2/getrisks").header("Authorization", "Bearer " + token).asString()
            assertEquals(200, response.status)
            assert(response.body.toString().contains("Health risks found for user"))
        }
        @Test
        fun `Get no health risks from a healthy user`(){
            var token = Login("healthAdmin@hospital.com", "admin") //Admin User
            var response = Unirest.get(domain + "/api/users/1/getrisks").header("Authorization", "Bearer " + token).asString()
            assertEquals(200, response.status)
            assert(response.body.toString().contains("No health risks found for user"))

        }


    }
    @Nested
    inner class diets{
      @Test
        fun `Get diets successfully for a unhealthy user`(){
            var token = Login("healthAdmin@hospital.com", "admin") //Admin User
            var response = Unirest.get(domain + "/api/users/2/getdiets").header("Authorization", "Bearer " + token).asString()
            assertEquals(200, response.status)
            assert(response.body.toString().contains("Diets are found"))
        }
        @Test
        fun `Get no diets for a healthy user`(){
            var token = Login("healthAdmin@hospital.com", "admin") //Admin User
            var response = Unirest.get(domain + "/api/users/1/getdiets").header("Authorization", "Bearer " + token).asString()
            assertEquals(200, response.status)
            assert(response.body.toString().contains("No diets required"))

        }

    }

}