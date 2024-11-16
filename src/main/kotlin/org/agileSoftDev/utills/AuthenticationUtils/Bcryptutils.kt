package org.agileSoftDev.utills.AuthenticationUtils

import at.favre.lib.crypto.bcrypt.BCrypt

class Bcryptutils {

    //https://wiki.yowu.dev/en/Knowledge-base/Kotlin/kotlin-and-bcrypt-a-better-alternative-to-simple-hashing - Bcrypt reference

    fun hashPassword(password: String): String {

        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    fun verifyPassword(password: String, hash: String): Boolean {
        val isVerified =  BCrypt.verifyer().verify(password.toCharArray(), hash)
        return  isVerified.verified
    }
}