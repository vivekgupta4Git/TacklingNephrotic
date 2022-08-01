package com.ruviapps.tacklingnephrotic.domain.use_cases.patient

import android.content.Context
import com.ruviapps.tacklingnephrotic.R
import com.ruviapps.tacklingnephrotic.database.dto.QueryResult
import com.ruviapps.tacklingnephrotic.domain.Patient
import com.ruviapps.tacklingnephrotic.domain.use_cases.caretaker.RegisterCareTakerUseCase.Companion.VALIDATION_ERROR
import com.ruviapps.tacklingnephrotic.extension.toDatabasePatient
import com.ruviapps.tacklingnephrotic.repository.PatientLocalRepository
import java.sql.SQLException
import javax.inject.Inject

class AddPatientUseCase @Inject constructor(
    private val repository: PatientLocalRepository,
) {
    suspend operator fun invoke  (
        patientName: String,
        patientAge: Int,
        patientWeight: Float,
        patientPicUri: String = "",
        underCareTakerId: String
    ) : QueryResult<Long>
    {

        val patient = Patient(0,patientName,patientAge,patientWeight,patientPicUri,underCareTakerId)
        return   try {
      repository.savePatient(patient.toDatabasePatient())
    }catch (ex : SQLException){
     QueryResult.Error("$ex", VALIDATION_ERROR)
    }

    }


}