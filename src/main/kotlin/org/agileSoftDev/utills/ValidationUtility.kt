package org.agileSoftDev.utills

import org.agileSoftDev.domain.repository.UserDAO
import java.util.regex.Pattern

fun isValidInRange(valueToCheck: Int, minValue: Int, maxValue: Int): Boolean{
    return (valueToCheck >= minValue) && (valueToCheck <= maxValue)
}

fun isValidEmail(email: String): Boolean {

    var userDAO = UserDAO()
    val emailRegex = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"

    var user = userDAO.findByEmail(email)
    return (Pattern.matches(emailRegex, email) and (user == null))
}

