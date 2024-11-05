package org.agileSoftDev.utills.Enums

enum class UserRoles(var value: String) {

    ADMIN("admin"),
    USER("user")
}
enum class Activities(var value: String) {

    RUNNING("running"),
    WALKING("walking"),
    JUMPROPING("jump roping"),
    SPRINTING("sprinting"),

}
fun checkRole(role : String): Boolean{
    var validRoles = UserRoles.entries.map{it -> it.value}
    println(validRoles)
    return validRoles.contains(role.lowercase())
}

fun checkActivities(activitYDes: String): Boolean{
    var validActivities = Activities.entries.map{it -> it.value}
    return validActivities.contains(activitYDes.lowercase())
}
