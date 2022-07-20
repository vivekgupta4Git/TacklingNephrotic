package com.ruviapps.tacklingnephrotic.domain.use_cases.caretaker

import android.util.Log
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
            validCareTaker = data.ctId.matches(id.toRegex())
            Log.d("verifyUser","validCareTaker = $validCareTaker  Data = $data")
        }
        qResult.onFailure { msg, _ ->
            validCareTaker = false
            Log.d("verifyUser","validCareTaker = $validCareTaker msg  = $msg" )

        }

        return validCareTaker
    }
}
