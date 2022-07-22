package com.ruviapps.tacklingnephrotic.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruviapps.tacklingnephrotic.database.dto.onFailure
import com.ruviapps.tacklingnephrotic.database.dto.onSuccess
import com.ruviapps.tacklingnephrotic.domain.Patient
import com.ruviapps.tacklingnephrotic.domain.use_cases.patient.PatientUseCases
import com.ruviapps.tacklingnephrotic.utility.Event
import com.ruviapps.tacklingnephrotic.utility.NavigationCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(private val patientUseCases: PatientUseCases): ViewModel() {

    private var _insertionComplete = MutableLiveData<Boolean>().apply {
        value = false
    }

    private var _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation : LiveData<Event<NavigationCommand>>
    get() = _navigation

   fun savePatientDetails(patient : Patient) {
        viewModelScope.launch {
            if (_insertionComplete.value == false) {
                val query = patientUseCases.addPatientUseCase(patient.patientName,
                    patient.patientAge,
                    patient.patientWeight,
                    patient.patientPicUri,
                    patient.underCareTakerId)
                query.onSuccess { patientId, _ ->
                    _insertionComplete.value = true
                    _navigation.value =
                        Event(NavigationCommand
                            .ToDirection(GalleryFragmentDirections
                                .actionNavGalleryToNavResult(
                                    patientId
                                )))
                }
                query.onFailure { message, _ ->
                    _insertionComplete.value = false
                    _navigation.value = Event(NavigationCommand.ShowError(message))
                }
            }
        }
    }
}