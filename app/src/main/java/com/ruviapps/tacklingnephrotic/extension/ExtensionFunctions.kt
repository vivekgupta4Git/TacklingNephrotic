package com.ruviapps.tacklingnephrotic.extension

import com.ruviapps.tacklingnephrotic.database.entities.*
import com.ruviapps.tacklingnephrotic.domain.*

//extension function on List of DatabaseCareTaker(Database Object) to convert it into List of CareTaker(Domain Object)
fun List<DatabaseCareTaker>.toDomainCareTaker() : List<CareTaker>{
    return map{
        CareTaker(it.ctId,
            it.fullName.firstName + " " + it.fullName.lastName,
            it.contact.email,
            it.contact.primaryContact,
            it.contact.secondaryContact)
    }
}

//extension function to convert Database object to Domain Object
fun DatabaseCareTaker.toDomainCareTaker() : CareTaker {
    return CareTaker(ctId,
        fullName.firstName+" "+ fullName.lastName,
        contact.email,
        contact.primaryContact,
        contact.secondaryContact)
}


//extension function to convert list of  domain object to  list of database object
fun List<CareTaker>.toDatabaseCareTaker() : List<DatabaseCareTaker>{
    return map{
        val posOfSpace = it.careTakerName?.indexOf(" ") ?: -1
        val firstName= if(posOfSpace!=-1 )  it.careTakerName?.substring(0,posOfSpace) else it.careTakerName
        val lastName = if(posOfSpace!=-1) it.careTakerName?.substring(posOfSpace+1) else  " "
        val fullName  = if(firstName!=null) FullName(firstName,lastName) else FullName("Default","")

        val email = it.email ?: ""
        val primaryContact = it.primaryContact
        val secondaryContact = it.secondaryContact
        val contact = ContactInfo(primaryContact,secondaryContact,email)
        DatabaseCareTaker(it.careTakerId,fullName,contact)
    }
}

//extension function to convert   domain object to database object
fun CareTaker.toDatabaseCareTaker() : DatabaseCareTaker{
    val posOfSpace = careTakerName?.indexOf(" ") ?: -1
    val firstName= if(posOfSpace!=-1 )  careTakerName?.substring(0,posOfSpace) else careTakerName
    val lastName = if(posOfSpace!=-1) careTakerName?.substring(posOfSpace+1) else  " "

    val fullName  = if(firstName!=null) FullName(firstName,lastName) else FullName("Default","")

    val primaryContact = primaryContact
    val secondaryContact = secondaryContact
    val contact = ContactInfo(primaryContact,secondaryContact,email)

    return DatabaseCareTaker(careTakerId,fullName,contact)
}


//extension function to convert patient domain object to database object
fun Patient.toDatabasePatient() : DatabasePatient{
    val posOfSpace = patientName.indexOf(" ")
    val firstName= if(posOfSpace!=-1 )  patientName.substring(0,posOfSpace) else patientName
    val lastName = if(posOfSpace!=-1) patientName.substring(posOfSpace+1) else  " "
    val fullName =  FullName(firstName,lastName)          //if first name is null, We throw NPE

    return DatabasePatient(patientId,
        fullName,
        patientAge,
    patientWeight,
    patientPicUri,underCareTakerId)

}

//extension function to convert list of patient domain objects to database object list
fun List<Patient>.toDatabasePatient() : List<DatabasePatient>{

    return map {
        val posOfSpace = it.patientName.indexOf(" ")
        val firstName= if(posOfSpace!=-1 )  it.patientName.substring(0,posOfSpace) else it.patientName
        val lastName = if(posOfSpace!=-1) it.patientName.substring(posOfSpace+1) else  " "
        val fullName =  FullName(firstName,lastName)          //if first name is null, We throw NPE

        DatabasePatient(it.patientId,
            fullName,
            it.patientAge,
            it.patientWeight,
            it.patientPicUri,
            it.underCareTakerId)
    }
}

//extension function to convert database patient object into domain patient object
fun DatabasePatient.toDomainPatient() : Patient{

    val firstName = fullName?.firstName ?: ""
    val lastName = fullName?.lastName ?: ""
    val patientName = "$firstName $lastName"

    return Patient(
        patientId,
        patientName,
        age,
        weight,
        snapUri,
        patientCaretakerId
    )

}

//extension function to convert List of Patient Database Objects to list of domain Patient Objects
fun List<DatabasePatient>.toDomainPatient() : List<Patient>{
    return map{
        val firstName = it.fullName?.firstName ?: ""
        val lastName = it.fullName?.lastName ?: ""
        val patientName = "$firstName $lastName"

        Patient(
            it.patientId,
            patientName,
            it.age,
            it.weight,
            it.snapUri,
            it.patientCaretakerId
        )
    }
}


//extension function to convert List of state database objects to list of domain state objects
fun List<DatabasePatientState>.toDomainState() : List<State>{
 return map{
     State(it.stateId,it.patientId,it.onDate,it.patient_state)
 }
}

fun DatabasePatientState.toDomainState() : State{
    return State(stateId,patientId,onDate,patient_state)
}

fun State.toDatabaseRelapse() : DatabasePatientState{
    return DatabasePatientState(stateId,patient_state,patientId,onDate)
}
fun List<State>.toDatabaseStates() : List<DatabasePatientState>{
    return map{
        DatabasePatientState(it.stateId,it.patient_state,it.patientId,it.onDate)
    }
}


//extension function to convert List of Database Urine Result into domain Result
fun List<UrineResult>.toDomainResult() : List<TestResult>{
    return map{
     TestResult(it.recordedDate,it.resultCode.name,it.remarks,it.urineResultOfPatientId)
    }
}

fun UrineResult.toDomainResult() : TestResult{
    return TestResult(recordedDate,resultCode.name,remarks,urineResultOfPatientId)
}

fun TestResult.toDatabaseUrineResult() : UrineResult{
    val resultCode = ResultCode.valueOf(resultCode)
    return UrineResult(recordedDate,resultCode,remarks, patientId)
}

fun List<TestResult>.toDatabaseUrineResult() : List<UrineResult>{
    return map{
        val resultCode = ResultCode.valueOf(it.resultCode)
        UrineResult(it.recordedDate,resultCode,it.remarks,it.patientId)
    }
}


//Extension function to convert Database Consultation to domain Consultation
fun DatabaseConsultation.toDomainConsultation() : Consultation{
    return Consultation(consultId,patientId,visitDate,consultedDoctorId)
}
fun List<DatabaseConsultation>.toDomainConsultation() : List<Consultation>{
    return map{
        Consultation(it.consultId,
            it.patientId,
            it.visitDate,
            it.consultedDoctorId)
    }
}

fun Consultation.toDatabaseConsultation() : DatabaseConsultation{
    return DatabaseConsultation(consultId,patientId,visitDate,consultedDoctorId)
}
fun List<Consultation>.toDatabaseConsultation() : List<DatabaseConsultation>{
    return map{
        DatabaseConsultation(it.consultId,
            it.patientId,
            it.visitDate,
            it.consultedDoctorId)
    }
}



