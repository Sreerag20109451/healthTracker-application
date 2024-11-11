package org.agileSoftDev.testcontroller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kong.unirest.core.Unirest
import org.agileSoftDev.config.DBConfig
import org.agileSoftDev.domain.User
import org.agileSoftDev.helpers.ServerContainer
import org.agileSoftDev.utills.AuthenticationUtils.jsonObjectMapper
import org.eclipse.jetty.util.security.Password
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UserController {
    private  val db = DBConfig().getConnection()
    private  val app = ServerContainer.instance
    private val domain = "http://localhost:"+ app.port()

    val adminUser =  User(name= "Sreerag Sathian", email = "20109451@mail.wit.ie", id = 5, role = "admin", password = "admin")

    private fun Login(email: String, password: String): String?{

        var resp = Unirest.post("$domain/api/login").body("{\"email\":\"$email\", \"password\":\"$password\"}")
            .asJson()

        println(resp.body)
        var mapper = jacksonObjectMapper()
        val resultMap : HashMap<String, String> = mapper.readValue(resp.body.toString())

        return resultMap["token"]

    }

    @Nested
    inner class ReadUserOperations {

        @Test
        fun `get all users successfully with 200 statusCode with an admin user`() {

            var token = Login("healthAdmin@hospital.com", "admin") //Admin User
            var response = Unirest.get(domain + "/api/users").header("Authorization", "Bearer " + token).asString()

            assertEquals(200, response.status)
        }
        @Test
        fun `get an error when accessing all users without a jwt token` (){

            var response = Unirest.get(domain + "/api/users").asString()
            assertEquals(401, response.status)
        }

        @Test
        fun `get an error when accessing all users with a normal user(Not admin)` (){
            var token = Login("20109451@mail.wit.ie", "test") //Normal User
            var response = Unirest.get(domain + "/api/users").header("Authorization", "Bearer " + token).asString()
            assertEquals(403, response.status)
        }
    }

    @Nested
    inner class GetAUserResourcesByID {

        @Test
        fun `get a userdetail successfully with 200 statusCode with an admin user`() {

            var token = Login("healthAdmin@hospital.com", "admin") //Admin User
            var response = Unirest.get(domain + "/api/users/1").header("Authorization", "Bearer " + token).asString()

            assertEquals(200, response.status)
        }
        @Test
        fun `get user resource when accessing with the same user` (){

            var token = Login("20109451@mail.wit.ie", "test") //Normal User
            var response = Unirest.get(domain + "/api/users/1").header("Authorization", "Bearer " + token).asString()
            assertEquals(200, response.status)
        }
        @Test
        fun `get an error when accessing all users with different and normal user(Not admin)` (){
            var token = Login("20109451@mail.wit.ie", "test") //Normal User
            var response = Unirest.get(domain + "/api/users/15").header("Authorization", "Bearer " + token).asString()
            assertEquals(403, response.status)
        }
        @Test
        fun `get a 404 error when accessing an user that does not exist with an admin user` (){
            var token = Login("healthAdmin@hospital.com", "admin") //Admin User
            var response = Unirest.get(domain + "/api/users/150").header("Authorization", "Bearer " + token).asString()
            assertEquals(404, response.status)
        }
    }







}