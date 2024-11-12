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

class HealthIndicatorTest {

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
    inner class HealthIndicatorsReadOperations{

        @Test
        fun `Successfully return user health indicators with the same user`(){

            var token = Login("20109451@mail.wit.ie", "test")
            var response = Unirest.get(domain + "/api/users/1/healthindicators").header("Authorization", "Bearer " + token).asString()
            assertEquals(200, response.status)

        }
        @Test
        fun `Successfully return user health indicators with admin`(){
            var token = Login("healthAdmin@hospital.com", "admin") //Admin User
            var response = Unirest.get(domain + "/api/users/1/healthindicators").header("Authorization", "Bearer " + token).asString()
            assertEquals(200, response.status)
        }
        @Test
        fun `Return 403 error when accessing health indicator data for different user`(){

            var token = Login("20109451@mail.wit.ie", "test")
            var response = Unirest.get(domain + "/api/users/2/healthindicators").header("Authorization", "Bearer " + token).asString()
            assertEquals(403, response.status)
        }
    }

    @Nested
    inner class HealthIndicatorsPostOperations{
         @Test
        fun `Return 403 error when trying to add health indicator by the same non admin user`(){
            var body = "{\"userid\":1,\"age\":26,\"height\":181,\"weight\":70,\"boxygen\":98,\"hdl\":61,\"ldl\":121,\"alt\":34,\"ast\":34,\"gfr\":80}"
             var token = Login("20109451@mail.wit.ie", "test")
             var response = Unirest.post(domain + "/api/users/1/healthindicators").body(body).header("Authorization", "Bearer " + token).asString()
             assertEquals(403, response.status)
        }
        @Test
        fun `Return 201  when trying to add health indicator by admin user`(){
            var body = "{\"userid\":1,\"age\":26,\"height\":181,\"weight\":70,\"boxygen\":98,\"hdl\":63,\"ldl\":80,\"alt\":34,\"ast\":34,\"gfr\":80}"
            var token = Login("healthAdmin@hospital.com", "admin") //Admin User
            var response = Unirest.post(domain + "/api/users/1/healthindicators").body(body).header("Authorization", "Bearer " + token).asString()
            assertEquals(201, response.status)
        }

        @Test
        fun `Return 404  when trying to add health indicator for an invalid user `(){
            var body = "{\"userid\":40,\"age\":26,\"height\":181,\"weight\":70,\"boxygen\":98,\"hdl\":61,\"ldl\":121,\"alt\":34,\"ast\":34,\"gfr\":80}"
            var token = Login("healthAdmin@hospital.com", "admin") //Admin User
            var response = Unirest.post(domain + "/api/users/40/healthindicators").body(body).header("Authorization", "Bearer " + token).asString()
            assertEquals(404, response.status)
        }



    }

}