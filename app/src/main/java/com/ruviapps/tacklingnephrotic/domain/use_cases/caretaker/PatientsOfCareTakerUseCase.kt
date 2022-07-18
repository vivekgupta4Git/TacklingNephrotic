package com.ruviapps.tacklingnephrotic.domain.use_cases.caretaker

import com.ruviapps.tacklingnephrotic.database.dto.QueryResult
import com.ruviapps.tacklingnephrotic.database.entities.CareTakerWithPatients
import com.ruviapps.tacklingnephrotic.repository.CareTakerLocalRepository
import javax.inject.Inject


class PatientsOfCareTakerUseCase @Inject constructor (
    private val repository: CareTakerLocalRepository,
) {

    suspend operator fun invoke(
        careTakerId : String
        ): QueryResult<List<CareTakerWithPatients>> {
        return  repository.getPatientsOfCareTaker(careTakerId)

      }


}

