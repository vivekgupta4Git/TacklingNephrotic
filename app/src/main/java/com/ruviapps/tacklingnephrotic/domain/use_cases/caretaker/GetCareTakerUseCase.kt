package com.ruviapps.tacklingnephrotic.domain.use_cases.caretaker

import com.ruviapps.tacklingnephrotic.repository.CareTakerLocalRepository
import javax.inject.Inject

class GetCareTakerUseCase @Inject constructor(
    private val repository: CareTakerLocalRepository
){
    suspend operator fun invoke(id : String) = repository.getCareTaker(id)
}
