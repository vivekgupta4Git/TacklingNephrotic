package com.ruviapps.tacklingnephrotic.domain.use_cases.patient

import com.ruviapps.tacklingnephrotic.database.dto.QueryResult
import com.ruviapps.tacklingnephrotic.domain.Patient
import com.ruviapps.tacklingnephrotic.domain.use_cases.caretaker.RegisterCareTakerUseCase.Companion.VALIDATION_ERROR
import com.ruviapps.tacklingnephrotic.extension.toDatabasePatient
import com.ruviapps.tacklingnephrotic.repository.PatientLocalRepository
import javax.inject.Inject

class AddPatientUseCase @Inject constructor(
    private val repository: PatientLocalRepository
) {
    suspend operator fun invoke  (
        patientName : String,
        patientAge : Int?,
        patientWeight : Float?,
        patientPicUri : String = "",
        underCareTakerId: String
    ) : QueryResult<Long>
    {
        if(patientName.isEmpty()||patientName.isBlank())
            return QueryResult.Error("Name Cannot be null",VALIDATION_ERROR)

        if(patientWeight==null){
            return QueryResult.Error("Weight Cannot be null", VALIDATION_ERROR)
        }
        else if( patientWeight < 0){
                return QueryResult.Error("Weight Cannot be Negative", VALIDATION_ERROR)
        }

        val patient = Patient(0,patientName,patientAge,patientWeight,patientPicUri,underCareTakerId)

       return repository.savePatient(patient.toDatabasePatient())
    }
}