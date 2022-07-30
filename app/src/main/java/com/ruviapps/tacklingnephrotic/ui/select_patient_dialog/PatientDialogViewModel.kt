package com.ruviapps.tacklingnephrotic.ui.select_patient_dialog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruviapps.tacklingnephrotic.database.dto.onSuccess
import com.ruviapps.tacklingnephrotic.domain.Patient
import com.ruviapps.tacklingnephrotic.domain.use_cases.patient.PatientUseCases
import com.ruviapps.tacklingnephrotic.utility.Event
import com.ruviapps.tacklingnephrotic.utility.NavigationCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class PatientDialogViewModel @Inject constructor(
    val patientUseCases: PatientUseCases
): ViewModel() {

    private var _patientList = MutableLiveData<List<Patient>>()
    val patientList : LiveData<List<Patient>>
    get() = _patientList

    private var _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation : LiveData<Event<NavigationCommand>>
    get() = _navigation

    fun setSelectedPatientId(id : Long){
        _navigation.value= Event(NavigationCommand.ToDirection(PatientPickerDialogFragmentDirections.actionWelcomeToNavResult(id)))
    }
    fun getPatientList(careTakerId : String){
        viewModelScope.launch {
            val query = patientUseCases.getAllPatientForCareTakerUseCase(careTakerId)
            query.onSuccess { data, _ ->
                _patientList.value = data
            }
        }
    }



}