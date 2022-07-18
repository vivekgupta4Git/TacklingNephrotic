package com.ruviapps.tacklingnephrotic.domain.use_cases.caretaker

import com.ruviapps.tacklingnephrotic.database.dto.onFailure
import com.ruviapps.tacklingnephrotic.database.dto.onSuccess
import com.ruviapps.tacklingnephrotic.repository.CareTakerLocalRepository
import javax.inject.Inject

class IsValidCareTaker@Inject constructor(
    private val repository: CareTakerLocalRepository
){
    suspend operator fun invoke(id : String) : Boolean {
        val qResult = repository.getCareTaker(id)
    var validCareTaker = false

        qResult.onSuccess { data, _ ->
            validCareTaker = data.ctId != id
        }
        qResult.onFailure { _, _ ->
            validCareTaker = false
        }

        return validCareTaker
    }
}
