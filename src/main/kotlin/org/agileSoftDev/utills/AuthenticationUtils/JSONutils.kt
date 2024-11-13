package org.agileSoftDev.utills.AuthenticationUtils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

fun jsonObjectMapper() : ObjectMapper {

    return ObjectMapper().registerModule(JodaModule()).registerModules(KotlinModule.Builder().build()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false)

}