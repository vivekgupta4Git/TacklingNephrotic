package com.ruviapps.tacklingnephrotic.domain

import java.time.LocalDate
import java.util.*

data class Patient(
    val patientId : Long,
    val patientName : String,
    val patientAge : Int?,
    val patientWeight : Float?,
    val patientPicUri : String = "",
    val underCareTakerId: String
)
data class CareTaker(
    val careTakerId : String,
    val careTakerName : String?,
    val email : String?,
    val primaryContact:String,
    val secondaryContact:String?
)



data class TestResult(
    val recordedDate : LocalDate,
    var resultCode : String,
    val remarks : String?,
    val patientId : Long
)


data class State(
    val stateId : Long,
    val patientId : Long,
    val onDate : Date,
    val patient_state : NephroticState,
)

data class Consultation(
    val consultId : Long,
    val patientId : Long,
    val visitDate : Date,
    val consultedDoctorId : Long
)


enum class NephroticState {
    REMISSION,
    RELAPSE,
    OBSERVATION
}