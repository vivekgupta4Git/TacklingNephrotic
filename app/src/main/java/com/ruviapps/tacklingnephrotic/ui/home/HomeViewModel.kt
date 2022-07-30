package com.ruviapps.tacklingnephrotic.ui.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ruviapps.tacklingnephrotic.database.dto.QueryResult
import com.ruviapps.tacklingnephrotic.database.dto.onFailure
import com.ruviapps.tacklingnephrotic.database.dto.onSuccess
import com.ruviapps.tacklingnephrotic.database.entities.ResultCode
import com.ruviapps.tacklingnephrotic.database.entities.UrineResult
import com.ruviapps.tacklingnephrotic.domain.NephroticState
import com.ruviapps.tacklingnephrotic.domain.Patient
import com.ruviapps.tacklingnephrotic.domain.use_cases.patient.PatientUseCases
import com.ruviapps.tacklingnephrotic.domain.use_cases.result.GetMissedReadingDatesUseCase
import com.ruviapps.tacklingnephrotic.domain.use_cases.result.UrineResultUseCases
import com.ruviapps.tacklingnephrotic.domain.use_cases.stateCalculator.CalculateStateUseCases
import com.ruviapps.tacklingnephrotic.extension.toDomainPatient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor
    (
    val resultUseCases: UrineResultUseCases,
    val patientUseCases : PatientUseCases,
    val stateUseCases: CalculateStateUseCases
) : ViewModel() {

    fun changeText(s : String){
        _text.value = s
    }
    private val _date = MutableLiveData<List<LocalDate>>()
    val date : LiveData<List<LocalDate>>
    get() = _date

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    private var _list = MutableSharedFlow<List<UrineResult>>()
    val list = _list.asSharedFlow()

    private var _state = MutableLiveData<NephroticState>().apply {
        value = NephroticState.REMISSION
    }
    val state : LiveData<NephroticState>
    get() = _state

    fun getEntryList(){
        viewModelScope.launch {
            var p = Patient(0, "",0,0.0f,"", 0.toString())
            val pqery = patientUseCases.getPatientUseCases(1)
            pqery.onSuccess { data, _ ->
                p =data.toDomainPatient()
            }
            val rqery = resultUseCases.allResultForPatientUseCase(p)
            rqery.onSuccess { data, _ ->
               _list.emit(data)
            }
        }
    }
    fun missedReadings(id : Long){
        viewModelScope.launch {
            val q =         resultUseCases.getMissedReadingDatesUseCase(id)
            q.onSuccess { data, _ ->
                _date.value = data
            }
          q.onFailure { message, _ ->
              _text.value = message
          }

        }

    }



    fun getEntriesByDate(id : Long) {
        viewModelScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val from = LocalDate.now().minusDays(2)
                val end = LocalDate.now()
                val queryResult = resultUseCases.getReadingsByDateUseCase(id, from, end)
                queryResult.onSuccess { data, _ ->
                    _list.emit( data)
                }
            }
        }
    }

    fun revealState(patientId : Long, date : LocalDate) {
        viewModelScope.launch {
            _state.value = stateUseCases.calculateStateFromReadingUseCae(patientId, date)
        }
    }


   /* fun calculateState(){

        val yesterday = _list.value?.get(1)
        val dayBefore = _list.value?.get(2)
        val reading = _list.value?.get(0)

       when(reading?.resultCode?.value){
           in -1..1 ->
           {
                when(yesterday?.resultCode?.value){
                    in -1..1   ->
                    {
                        when(dayBefore?.resultCode?.value){
                            in -1..1 ->
                                _state.value= NephroticState.REMISSION
                            in 2..4 ->
                                _state.value = NephroticState.OBSERVATION
                        }

                    }
                    in 2..4 ->
                    {
                        _state.value = NephroticState.OBSERVATION
                    }

                }

           }
           in 2..4 ->{

               when(yesterday?.resultCode?.value){
                   in -1..1   ->
                   {
                       when(dayBefore?.resultCode?.value){
                           in -1..1 ->
                               _state.value= NephroticState.REMISSION
                           in 2..4 ->
                               _state.value = NephroticState.OBSERVATION
                       }

                   }
                   in 2..4 ->
                   {
                       when(dayBefore?.resultCode?.value){
                           in -1..1 ->
                               _state.value= NephroticState.OBSERVATION
                           in 2..4 ->
                               _state.value = NephroticState.RELAPSE
                       }

                   }

               }



           }
       }
    }*/

}