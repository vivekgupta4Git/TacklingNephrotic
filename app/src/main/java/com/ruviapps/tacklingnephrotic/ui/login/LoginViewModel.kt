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
import com.google.firebase.ktx.Firebase
import com.ruviapps.tacklingnephrotic.R
import com.ruviapps.tacklingnephrotic.database.dto.onFailure
import com.ruviapps.tacklingnephrotic.database.dto.onSuccess
import com.ruviapps.tacklingnephrotic.di.module.AppModule
import com.ruviapps.tacklingnephrotic.domain.use_cases.caretaker.CareTakerUseCases
import com.ruviapps.tacklingnephrotic.utility.Event
import com.ruviapps.tacklingnephrotic.utility.NavigationCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val resourcesProvider: AppModule.ResourcesProvider
) : ViewModel() {

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
            .setIsSmartLockEnabled(false)       //for testing purpose
            .setLogo(R.mipmap.ic_launcher)
            .setTheme(R.style.Theme_MyLoginTheme)
            .setAvailableProviders(providerToUse)
            .build()
    }


    fun handleResult(result : FirebaseAuthUIAuthenticationResult){
        val  loginResponse= result.idpResponse

        if(result.resultCode == Activity.RESULT_OK){
            val action = LoginFragmentDirections.actionNavLoginFragmentToNavUserRole()
            _navigation.value =   Event(NavigationCommand.ToDirection(action))

        }else
        {
            if(loginResponse== null) {
                _navigation.value =
                    Event((NavigationCommand.ShowError(resourcesProvider.getString(R.string.login_cancelled))))
                    return
            }

            if(loginResponse.error?.errorCode == ErrorCodes.NO_NETWORK) {
                _navigation.value =
                    Event(NavigationCommand.ShowError(resourcesProvider.getString(R.string.no_internet_exclaim)))
                return
            }

            _navigation.value = Event(NavigationCommand.ShowError(resourcesProvider.getString(R.string.login_failed_msg2)))
        }
    }

}