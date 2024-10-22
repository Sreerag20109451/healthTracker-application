package org.agileSoftDev.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.postgresql.util.PSQLException

class DBConfig {



    private  lateinit var dbConfig: Database

    private val logger = KotlinLogging.logger{}

    fun getConnection(): Database{

        val PGHOST = "dpg-cs76pci3esus73chaj8g-a.frankfurt-postgres.render.com"
        val PGPORT = "5432"
        val PGUSER = "healthtrackerdb_aijc_user"
        val PGPASSWORD = "V2Vh1144KcHN9TTMJv0dddfjJpVAQSY6"
        val PGDATABASE = "healthtrackerdb_aijc"

        val dbUrl ="jdbc:postgresql://$PGHOST:$PGPORT/$PGDATABASE"

        try {
            logger.info { "Connecting to db: $dbUrl" }
            dbConfig = Database.connect(dbUrl, driver = "org.h2.Driver", user = PGUSER, password = PGPASSWORD)
        }
        catch (e : PSQLException) {
            logger.error { "Error connecting to database: $e" }
            println("Error connecting to database")
        }

        return dbConfig




    }


}