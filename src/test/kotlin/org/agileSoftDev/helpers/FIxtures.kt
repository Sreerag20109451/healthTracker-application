package org.agileSoftDev.helpers

import org.agileSoftDev.domain.Activity
import org.agileSoftDev.domain.User
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1, role = "user", password = "abc"),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2, role = "user", password = "abc"),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3, role ="abc" ),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4, role ="abcr" )
)

val activities = arrayListOf<Activity>(

    Activity(id = 10, description = "walking", duration = 45.1, calories = 4, started = DateTime.now(), userId = 1),

    Activity(id = 11, description = "skipping", duration = 15.1, calories = 8, started = DateTime.now(), userId = 3),

    Activity(id = 12, description = "Jump roping", duration = 15.1, calories = 9, started = DateTime.now(), userId = 3),

     Activity(id = 13, description = "running", duration = 20.1, calories = 13, started = DateTime.now(), userId = 4)

)


