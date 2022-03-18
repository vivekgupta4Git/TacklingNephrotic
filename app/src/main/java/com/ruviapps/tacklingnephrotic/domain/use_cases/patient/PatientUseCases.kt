package com.ruviapps.tacklingnephrotic.domain.use_cases.patient

import javax.inject.Inject

data class PatientUseCases @Inject constructor(
    val addPatientUseCase: AddPatientUseCase,
    val removePatientUseCase: RemovePatientUseCase
)
