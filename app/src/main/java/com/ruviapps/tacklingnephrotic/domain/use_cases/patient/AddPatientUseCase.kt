package com.ruviapps.tacklingnephrotic.domain.use_cases.patient

import android.app.DownloadManager
import com.ruviapps.tacklingnephrotic.R
import com.ruviapps.tacklingnephrotic.database.dto.QueryResult
import com.ruviapps.tacklingnephrotic.database.dto.onSuccess
import com.ruviapps.tacklingnephrotic.di.module.AppModule
import com.ruviapps.tacklingnephrotic.domain.Patient
import com.ruviapps.tacklingnephrotic.domain.use_cases.caretaker.RegisterCareTakerUseCase.Companion.VALIDATION_ERROR
import com.ruviapps.tacklingnephrotic.extension.toDatabasePatient
import com.ruviapps.tacklingnephrotic.repository.PatientLocalRepository
import javax.inject.Inject

class AddPatientUseCase @Inject constructor(
    private val repository: PatientLocalRepository,
    private val resourcesProvider: AppModule.ResourcesProvider
) {
    suspend operator fun invoke  (
        patientName : String,
        patientAge : Int,
        patientWeight : Float,
        patientPicUri : String = "",
        underCareTakerId: String
    ) : QueryResult<Long>
    {
        if(patientName.isEmpty()||patientName.isBlank())
            return QueryResult.Error(resourcesProvider.getString(R.string.error_name),VALIDATION_ERROR)

       /* if(patientWeight==null){
            return QueryResult.Error(resourcesProvider.getString(R.string.error_weight), VALIDATION_ERROR)
        }*/
        if(patientAge <= 0) return QueryResult.Error(resourcesProvider.getString(R.string.invalid_age),
            VALIDATION_ERROR)

            if( patientWeight <= 0){
                return QueryResult.Error(resourcesProvider.getString(R.string.error_weight_negative), VALIDATION_ERROR)
            }


        val patient = Patient(0,patientName,patientAge,patientWeight,patientPicUri,underCareTakerId)

       return repository.savePatient(patient.toDatabasePatient())
    }


}