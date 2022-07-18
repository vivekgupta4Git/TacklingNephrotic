package com.ruviapps.tacklingnephrotic.domain.use_cases.caretaker

import javax.inject.Inject

data class CareTakerUseCases @Inject constructor(
    val registerCareTakerUseCase: RegisterCareTakerUseCase,
    val getCareTakerUseCase: GetCareTakerUseCase,
    val getAllCareTakersUseCase: GetAllCareTakersUseCase,
    val patientsOfCareTakerUseCase: PatientsOfCareTakerUseCase,
    val deleteCareTakerUseCases: DeleteCareTakerUseCases,
    val deleteAllCareTakerUseCase: DeleteAllCareTakerUseCase,
    val listOfCareTakersWithPatients: ListOfCareTakersWithPatients,
    val isValidCareTaker: IsValidCareTaker
)
