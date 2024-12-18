package org.agileSoftDev.testcontroller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kong.unirest.core.Unirest
import org.agileSoftDev.config.DBConfig
import org.agileSoftDev.domain.User
import org.agileSoftDev.helpers.ServerContainer
//import org.graalvm.compiler.lir.LIRInstruction.Use
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UserControllerTest {
    private  val db = DBConfig().getConnection()
    private  val app = ServerContainer.instance
    private val domain = "http://localhost:"+ app.port()

    val adminUser =  User(name= "Sreerag Sathian", email = "20109451@mail.wit.ie", id = 5, role = "admin", password = "admin")

    private fun Login(email: String, password: String): Map<String, String> {

        var resp = Unirest.post("$domain/api/login").body("{\"email\":\"$email\", \"password\":\"$password\"}")
            .asJson()

        var mapper = jacksonObjectMapper()
        val resultMap: HashMap<String, Any> = mapper.readValue(resp.body.toString())
        var user = mapper.convertValue<User>(resultMap["user"] ,User::class.java)
        println(user)
        val sessionId = user.id.toString()
        val token = resultMap["token"] as String

        var loginReturns  =  mapOf(Pair("token", token),Pair("sessionId", sessionId))
        return loginReturns

    }

    @Nested
    inner class ReadUserOperations {

        @Test
        fun `get all users successfully with 200 statusCode with an admin user`() {

            val map = Login("healthAdmin@hospital.com", "admin") //
            val token = map["token"]
            var sessionId = map["sessionId"]
            // Admin User
            var response = Unirest.get(domain + "/api/users").header("Authorization", "Bearer " + token).header("Sessionid", sessionId).asString()

            assertEquals(200, response.status)
        }
        @Test
        fun `get an error when accessing all users without a jwt token` (){

            var response = Unirest.get(domain + "/api/users").asString()
            assertEquals(401, response.status)
        }

        @Test
        fun `get an error when accessing all users with a normal user(Not admin)` (){

            var map = Login("20109451@mail.wit.ie", "test") //Normal User
            val token = map["token"]
            var sessionId = map["sessionId"]
            var response = Unirest.get(domain + "/api/users").header("Authorization", "Bearer " + token).header("Sessionid", sessionId).asString()
            assertEquals(403, response.status)
        }
    }

    @Nested
    inner class GetAUserResourcesByID {

        @Test
        fun `get a user detail successfully with 200 statusCode with an admin user`() {

            var map = Login("healthAdmin@hospital.com", "admin") //Admin User
            val token = map["token"]
            var sessionId = map["sessionId"]
            var response = Unirest.get(domain + "/api/users/1").header("Authorization", "Bearer " + token).header("Sessionid",sessionId).asString()

            assertEquals(200, response.status)
        }
        @Test
        fun `get user resource when accessing with the same user` (){

            var map = Login("20109451@mail.wit.ie", "test") //Normal User
            val token = map["token"]
            var sessionId = map["sessionId"]
            var response = Unirest.get(domain + "/api/users/1").header("Authorization", "Bearer " + token).header("Sessionid",sessionId).asString()
            assertEquals(200, response.status)
        }
        @Test
        fun `get an error when accessing all users with different and normal user(Not admin)` (){
            var map = Login("20109451@mail.wit.ie", "test") //Normal User
            val token = map["token"]
            var sessionId = map["sessionId"]
            var response = Unirest.get(domain + "/api/users/15").header("Authorization", "Bearer " + token).header("Sessionid",sessionId).asString()
            assertEquals(403, response.status)
        }
        @Test
        fun `get a 404 error when accessing an user that does not exist with an admin user` (){
            var map = Login("healthAdmin@hospital.com", "admin") //Admin User
            val token = map["token"]
            var sessionId = map["sessionId"]
            var response = Unirest.get(domain + "/api/users/150").header("Authorization", "Bearer " + token).header("Sessionid",sessionId).asString()
            assertEquals(404, response.status)
        }
    }

    @Nested
    inner class UserAddOperations{

//        @Test
//        fun `add users successfully and get a 200 response coder` (){
//
//            var body = "{\"name\":\"testUser\",\"email\":\"myEmail12@abc.com\", \"password\":\"abc\",\"role\":\"user\"}"
//            var resp = Unirest.post(domain+"/api/users").body(body).asString()
//            assertEquals(201,resp.status)
//        }
        @Test
        fun `add users with invalid email and get a 400 response code` (){

            var body = "{\"name\":\"testUser\",\"email\":\"jbhbabc.com\", \"password\":\"abc\",\"role\":\"user\"}"
            var resp = Unirest.post(domain+"/api/users").body(body).asString()
            assertEquals(400,resp.status)
        }

    }

    @Nested
    inner class UserUpdateOperations {
        @Test
        fun `update users successfully and get a 200 response code with an admin user`() {

            var map = Login("healthAdmin@hospital.com", "admin") //Admin User
            val token = map["token"]
            var sessionId = map["sessionId"]
            var body = "{\"name\":\"test_user\", \"email\":\"sdsd@mail.com\"}"
            var response =
                Unirest.put(domain + "/api/users/2").body(body).header("Authorization", "Bearer " + token).header("Sessionid",sessionId).asString()
            assertEquals(200, response.status)
            assert(response.body.toString().contains("User updated"))

        }

        @Test
        fun `update a non existing user and return a 404 error`() {
            var map = Login("healthAdmin@hospital.com", "admin") //Admin User
            val token = map["token"]
            var sessionId = map["sessionId"]

            var body = "{\"name\":\"test_user\", \"email\":\"sdsd@mail.com\"}"
            var response =  Unirest.put(domain + "/api/users/150").body(body).header("Authorization", "Bearer " + token).header("Sessionid",sessionId).asString()
            assertEquals(404, response.status)


        }

    }

    @Nested
    inner class UserDeletionOperations {

//        @Test
//        fun `Delete users successfully and get a 200 response code with an admin user`() {
//
//            var map = Login("healthAdmin@hospital.com", "admin") //Admin User
//            val token = map["token"]
//            var sessionId = map["sessionId"]
//            var response = Unirest.delete(domain + "/api/users/36").header("Authorization", "Bearer " + token).header("Sessionid",sessionId).asString()
//            assertEquals(200, response.status)
//
//        }

        @Test
        fun `delete a non existing user and return a 404 error`() {
            var map = Login("healthAdmin@hospital.com", "admin") //Admin User
            val token = map["token"]
            var sessionId = map["sessionId"]

            var response = Unirest.delete(domain + "/api/users/190").header("Authorization", "Bearer " + token).header("Sessionid",sessionId).asString()
            assertEquals(404, response.status)

        }
        @Test
        fun `delete a user with another user user and return a 403 error`() {
            var map = Login("20109451@mail.wit.ie", "test") //Normal User
            val token = map["token"]
            var sessionId = map["sessionId"]

            var response = Unirest.delete(domain + "/api/users/15").header("Authorization", "Bearer " + token).header("Sessionid",sessionId).asString()
            assertEquals(403, response.status)

        }

    }








}