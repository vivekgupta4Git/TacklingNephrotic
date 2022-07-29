package com.ruviapps.tacklingnephrotic.domain


import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import java.time.LocalDate
import java.util.*

//changing val to var to enable two-way data binding
data class Patient(
    var patientId : Long,
    var patientName : String,
    var patientAge :  Int,
    var patientWeight : Float,
    var patientPicUri : String = "",
    var underCareTakerId: String

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