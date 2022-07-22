package com.ruviapps.tacklingnephrotic.domain.use_cases.patient

import android.util.Log
import com.ruviapps.tacklingnephrotic.database.dto.QueryResult
import com.ruviapps.tacklingnephrotic.domain.Patient
import com.ruviapps.tacklingnephrotic.repository.PatientLocalRepository
import javax.inject.Inject

class GetAllPatientForCareTakerUseCase @Inject constructor(
    private val repository: PatientLocalRepository
) {
    suspend operator fun invoke(careTakerId: String): QueryResult<List<Patient>> {
        return repository.getAllPatientForCareTaker(careTakerId)
    }
}