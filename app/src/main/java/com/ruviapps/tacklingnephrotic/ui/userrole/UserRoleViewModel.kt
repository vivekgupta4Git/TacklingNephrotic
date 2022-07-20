package com.ruviapps.tacklingnephrotic.ui.userrole

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.ruviapps.tacklingnephrotic.R
import com.ruviapps.tacklingnephrotic.database.dto.onFailure
import com.ruviapps.tacklingnephrotic.database.dto.onSuccess
import com.ruviapps.tacklingnephrotic.di.module.AppModule
import com.ruviapps.tacklingnephrotic.domain.use_cases.patient.PatientUseCases
import com.ruviapps.tacklingnephrotic.utility.Event
import com.ruviapps.tacklingnephrotic.utility.NavigationCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class UserRoleViewModel @Inject constructor(
   private val resourcesProvider: AppModule.ResourcesProvider,
   val patientUseCases: PatientUseCases
   ) : ViewModel() {

    private val auth = AuthUI.getInstance().auth
    private val firebaseUser =   auth.currentUser
    private val uid = firebaseUser?.uid.toString()

    private var _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation : LiveData<Event<NavigationCommand>>
    get() = _navigation

    fun showSnackBar(){
        _navigation.value = Event(NavigationCommand.ShowError(resourcesProvider.getString(R.string.select_any_one)))
    }

    fun saveCareTaker(){
        //we have already stored user details to db we don't need to do anything else
        //in future I could let user fill its profile but at present I see it as bad user experience
     _navigation.value = Event(NavigationCommand.ToDirection(UserRoleFragmentDirections.actionNavUserRoleToNavResult()))
        //instead of navigating to reading screen, I should let user add patient first or open dashboard ,
    // where I will notify user to add patient first before using this app
    }

    fun savePatient(){
        //user is also a patient, we will use caretaker details as patient
        val name = firebaseUser?.displayName ?: "patient"
        viewModelScope.launch {
    withContext(this.coroutineContext){
        val query =
            patientUseCases.addPatientUseCase(patientName = name,
                patientAge = null,
                patientWeight = 1f ,            /*  by default using 1f as a weight */
                patientPicUri = "",
                underCareTakerId = uid )
        query.onSuccess { _, _ ->
            _navigation.value =
                Event(NavigationCommand.ToDirection(UserRoleFragmentDirections.actionNavUserRoleToNavResult()))
                    }

        query.onFailure { message, _ ->
            _navigation.value = Event(NavigationCommand.ShowError(message))
                }
            }
        }
    }


}
