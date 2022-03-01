package com.ruviApps.tacklingnephrotic.domain

import com.ruviApps.tacklingnephrotic.database.entities.ContactInfo
import com.ruviApps.tacklingnephrotic.database.entities.DatabaseCareTaker
import com.ruviApps.tacklingnephrotic.database.entities.FullName
import java.util.*

data class Patient(
    val patientId : Long,
    val patientName : String,
    val patientAge : Int?,
    val patientWeight : Float?,
    val patientPicUri : String = "",
    val underCareTakerId: Long
)
data class CareTaker(
    val careTakerId : Long,
    val careTakerName : String?,
    val email : String?,
    val primaryContact:Long?,
    val secondaryContact:Long?
)



data class Result(
    val resultId : Long,
    val resultCode : String,
    val remarks : String?,
    val recordedDate : Date
)
