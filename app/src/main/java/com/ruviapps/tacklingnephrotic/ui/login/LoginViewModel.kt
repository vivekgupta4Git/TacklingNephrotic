package com.ruviapps.tacklingnephrotic.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseUser
import com.ruviapps.tacklingnephrotic.R
import com.ruviapps.tacklingnephrotic.database.dto.onFailure
import com.ruviapps.tacklingnephrotic.database.dto.onSuccess
import com.ruviapps.tacklingnephrotic.di.module.AppModule
import com.ruviapps.tacklingnephrotic.domain.Patient
import com.ruviapps.tacklingnephrotic.domain.use_cases.caretaker.CareTakerUseCases
import com.ruviapps.tacklingnephrotic.domain.use_cases.patient.PatientUseCases
import com.ruviapps.tacklingnephrotic.utility.Event
import com.ruviapps.tacklingnephrotic.utility.NavigationCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val patientUseCases: PatientUseCases
    ) : ViewModel() {

    @Inject
    lateinit var careTakerUseCases : CareTakerUseCases



    private var _navigation = MutableLiveData< Event<NavigationCommand>>()
    val navigation : LiveData<Event<NavigationCommand>>
    get() = _navigation


    enum class Provider{
        EMAIL_PROVIDER,
        PHONE_PROVIDER,
        GOOGLE_PROVIDER
    }

    fun getSignInIntent(provider : Provider) : Intent {
        val providerToUse = when(provider){
            Provider.PHONE_PROVIDER -> arrayListOf(AuthUI.IdpConfig.PhoneBuilder().build())
            Provider.GOOGLE_PROVIDER -> arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
            else ->
                arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
        }
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(false)       //for testing purpose set  it to false
            .setLogo(R.mipmap.ic_launcher)
            .setTheme(R.style.Theme_MyLoginTheme)
            .setAvailableProviders(providerToUse)
            .build()
    }

    private val patientList = mutableListOf<Patient>()
  //a suspend function for retrieving all the patients under care taking of current user
    private suspend fun getAllPatientForCareTaker(uid : String){
                val query = patientUseCases.getAllPatientForCareTakerUseCase(uid)
                query.onSuccess { data, _ ->
                    patientList.addAll(data)
            }
    }




    fun handleResult(result : FirebaseAuthUIAuthenticationResult){
        val  loginResponse= result.idpResponse

        //if the login is successful
        if(result.resultCode == Activity.RESULT_OK){
            viewModelScope.launch {
                //if the user is not new don't bother to save user details inside local db else save details to db
                if(loginResponse!=null && loginResponse.isNewUser){
                    saveCareTaker()
                }
                else {
                 //user is not new
                    val uid = AuthUI.getInstance().auth.currentUser?.uid ?: ""
             //by using withContext ,we are letting the result to be produced first
                   withContext(this.coroutineContext) {
                       getAllPatientForCareTaker(uid)
                   }
                //if only one patient is under care taking of user then , no need to show another screen
                    //just navigate to userReading screen with patient id found
                    if(patientList.size in 1..1) {
                        _navigation.value =
                            Event(NavigationCommand
                                .ToDirection(LoginFragmentDirections
                                    .actionNavLoginFragmentToNavResult(
                                        patientList[0].patientId)
                                ))
                    }else{
                        //if no patient found
                       if(patientList.isEmpty())
                        _navigation.value = Event(NavigationCommand
                            .ToDirection(LoginFragmentDirections.actionNavLoginFragmentToNavUserRole()))
                        else if(patientList.size>1){

                           _navigation.value = Event(NavigationCommand.ToDirection(
                               LoginFragmentDirections.actionNavLoginFragmentToNavWelcome2(uid)
                           ))
                        //show user another screen consisting of list of patient
                            //user can select any one of the patient to get started.
                        }
                    }
                }
            }
        }else
        {
            //user has pressed the back button
            if(loginResponse== null) {
                _navigation.value =
                    Event((NavigationCommand.ShowErrorInt(R.string.login_cancelled)))
                    return
            }

            //no or slow network
            if(loginResponse.error?.errorCode == ErrorCodes.NO_NETWORK) {
                _navigation.value =
                    Event(NavigationCommand.ShowErrorInt(R.string.no_internet_exclaim))
                return
            }

            //unknown error
            _navigation.value = Event(NavigationCommand.ShowErrorInt(R.string.login_failed_msg2))
        }
    }

    private suspend fun saveCareTaker() {
        val uid: String
        val auth = AuthUI.getInstance().auth
        val firebaseUser : FirebaseUser? =   auth.currentUser

        if(firebaseUser!=null)
            uid = firebaseUser.uid
        else
            return

        val name = firebaseUser.displayName ?: "Care Taker"
        val phoneNumber = firebaseUser.phoneNumber
        val phoneString :String

        if(phoneNumber==null){
            phoneString = "0000000000"
        }else
            phoneString = phoneNumber

        val email = firebaseUser.email ?: "caretaker@neph.com"

        withContext(viewModelScope.coroutineContext) {
            val query =
                careTakerUseCases.registerCareTakerUseCase(uid, name, email, phoneString, "")
            query.onSuccess { _, _ ->
                _navigation.value =
                    Event(NavigationCommand
                        .ToDirection(
                            LoginFragmentDirections.actionNavLoginFragmentToNavUserRole()))
            }

            query.onFailure { message, _ ->
                _navigation.value = Event(NavigationCommand.ShowError(message))
            }
        }

        }




}