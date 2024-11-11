package org.agileSoftDev.domain



data class User(var id: Int, var name: String, var email: String, var password: String, val role: String){

}