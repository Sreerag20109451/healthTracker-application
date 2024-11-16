package org.agileSoftDev.utills.AuthenticationUtils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import io.javalin.http.Context
import javalinjwt.JWTProvider
import javalinjwt.JavalinJWT
import org.agileSoftDev.domain.User


//https://github.com/kmehrunes/javalin-jwt - JWT reference

class JWTutils {
    val algorithm = Algorithm.HMAC256("secret")

    val generator : (user : User , alg: Algorithm) -> String = { user, alg ->
        val token :JWTCreator.Builder = JWT.create().withClaim("id",user.id).withClaim("role" ,user.role  )
        token.sign(alg)
    }
    val verifier = JWT.require(algorithm).build()
    val ProviderJWT = JWTProvider(algorithm,generator,verifier)

    fun verifyTokens(ctx: Context) : Boolean {
        val decodedJWT = JavalinJWT.getTokenFromHeader(ctx).flatMap(ProviderJWT::validateToken )
        return decodedJWT.isPresent()
    }

    fun generateToken(user: User) : String {

        return ProviderJWT.generateToken(user)


    }

    var decodeHandler = JavalinJWT.createHeaderDecodeHandler(ProviderJWT)
}



