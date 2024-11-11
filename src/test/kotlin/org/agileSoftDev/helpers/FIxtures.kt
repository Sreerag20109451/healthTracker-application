package org.agileSoftDev.helpers

import org.agileSoftDev.domain.*
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1, role = "user", password = "abc"),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2, role = "user", password = "abc"),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3, role ="user", password = "abc"),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4, role ="user" , password = "abc"),
    User(name= "Sreerag Sathian", email = "20109451@mail.wit.ie", id = 5, role = "admin", password = "abc"),
)

val activities = arrayListOf<Activity>(

    Activity(id = 10, description = "walking", duration = 45.1, calories = 4, started = DateTime.now(), userId = 1),

    Activity(id = 11, description = "skipping", duration = 15.1, calories = 8, started = DateTime.now(), userId = 3),

    Activity(id = 12, description = "Jump roping", duration = 15.1, calories = 9, started = DateTime.now(), userId = 3),

    Activity(id = 13, description = "running", duration = 20.1, calories = 13, started = DateTime.now(), userId = 4)

)


val healthindicators = arrayListOf<HealthIndicator>(

    HealthIndicator(indicatorid = 1, userid = 4, age=30, height = 177, weight = 68, boxygen = 98, hdl = 60, ldl = 121, alt = 40, ast = 50, gfr = 80) ,
    HealthIndicator(indicatorid = 2, userid = 3, age=30, height = 177, weight = 68, boxygen = 98, hdl = 60, ldl = 121, alt = 40, ast = 50, gfr = 20),
    HealthIndicator(indicatorid = 3, userid = 2, age=30, height = 177, weight = 68, boxygen = 98, hdl = 62, ldl = 97, alt = 33, ast = 33, gfr = 80)


)

var healthrisks = arrayListOf<HealthRisk>(
    HealthRisk(riskid = 1, risk = "Obesity", bmi = 30.00, boxygen = null, hdl = 50, ldl = 106, alt = 40 , ast = 40, gfr = null, dept = "Bariatrics", dietid = 1 ),
    HealthRisk(riskid = 2, risk = "Heart Diseases", boxygen = null, bmi = 25.5, hdl = 59, ldl = 100, alt = 36, ast = 36, gfr =null, dept = "Cardiology", dietid = 1 ),
    HealthRisk(riskid = 3, risk = "Renal failure",bmi = null,boxygen = null, hdl = null,ldl = null, alt = null, ast = null, gfr = 30, dept = "Nephrology", dietid = 2),
    HealthRisk(riskid = 4, risk = "Fatty Liver", bmi = 25.0, boxygen = null, hdl = 50, ldl = 120, ast = 36, alt = 36, gfr = null, dept = "Hepatology", dietid = 1 ),
    HealthRisk(riskid = 5, risk = "NAFLD", bmi = 25.0, boxygen=null, hdl = 50, ldl = 120, ast = 36, alt = 36, gfr = null, dept = "Hepatology", dietid = 1 ),

)

var departments =  arrayListOf<HealthDepartment>(
    HealthDepartment(deptid = 1, dept = "Bariatrics"),
    HealthDepartment(deptid = 2, dept = "Cardiology"),
    HealthDepartment(deptid = 3, dept = "Nephrology"),
   HealthDepartment(deptid = 4, dept = "Gastroenterology"),
    HealthDepartment(deptid = 5, dept = "Hepatology"),
//

)

var diets = arrayListOf<Diet>(

     Diet(1, "Low fat diet", content = "Fruits, Vegetables, Fibres, Nuts"),
    Diet(2, "Low minerals, high fluids diet", content = "Fruits,water and fluids, Breads, Cereals, Pasta,soup")



)

