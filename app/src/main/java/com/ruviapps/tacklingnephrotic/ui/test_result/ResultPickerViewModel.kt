package com.ruviapps.tacklingnephrotic.ui.test_result

import android.app.Application
import android.util.Log

import androidx.lifecycle.*
import com.ruviapps.tacklingnephrotic.database.dto.onFailure
import com.ruviapps.tacklingnephrotic.database.dto.onSuccess
import com.ruviapps.tacklingnephrotic.domain.TestResult
import com.ruviapps.tacklingnephrotic.domain.use_cases.caretaker.CareTakerUseCases
import com.ruviapps.tacklingnephrotic.domain.use_cases.patient.PatientUseCases
import com.ruviapps.tacklingnephrotic.domain.use_cases.result.UrineResultUseCases
import com.ruviapps.tacklingnephrotic.utility.Event
import com.ruviapps.tacklingnephrotic.utility.NavigationCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "myTag"

@HiltViewModel
class ResultPickerViewModel
    @Inject constructor
    (
    val resultUseCases: UrineResultUseCases
) : ViewModel() {

    private val _navigateToDashBoard = MutableLiveData<Event<NavigationCommand>>()
    val navigateToDashBoard : LiveData<Event<NavigationCommand>>
        get() = _navigateToDashBoard


    fun saveResult(result: TestResult) {
        viewModelScope.launch {
           val query = resultUseCases.insertUrineResultUseCase(result)
            query.onSuccess { _, _ ->
                val action =   ResultPickerFragmentDirections
                    .actionNavResultToNavHome(result.resultCode,result.patientId)

              _navigateToDashBoard.value = Event(NavigationCommand.ToDirection(action))
            }
            query.onFailure { message, code ->
                Log.d(TAG,"Insert Error : $message + Code : $code")
                _navigateToDashBoard.value = Event(NavigationCommand.ShowError(message))
            }
        }
    }

}






