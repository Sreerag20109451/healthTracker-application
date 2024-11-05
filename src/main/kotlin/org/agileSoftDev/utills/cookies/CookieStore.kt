package org.agileSoftDev.utills.cookies

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import org.agileSoftDev.domain.User

class CookieStore {


     fun saveToCookieStore(ctx: Context, user: User){

         var mapper = jacksonObjectMapper()
         var user = mapper.writeValueAsString(user)
        ctx.cookieStore().set("user", user)


    }

        fun getFromCookieStore(ctx: Context, key: String): User?  {

            var stringUser: String =  ctx.cookieStore().get(key)
            if(stringUser == null) return null
            else{
                var user = jacksonObjectMapper().readValue<User>(stringUser)
                return user

            }
    }

}