package org.agileSoftDev.utills

import at.favre.lib.crypto.bcrypt.BCrypt
import io.javalin.http.servlet.getBasicAuthCredentials

class Bcryptutils {

    fun hashPassword(password: String): String {

        return BCrypt.withDefaults().hashToString(12, password.toCharArray())

    }

    fun verifyPassword(password: String, hash: String): Boolean {
        val isVerified =  BCrypt.verifyer().verify(password.toCharArray(), hash)
        return  isVerified.verified
    }
}