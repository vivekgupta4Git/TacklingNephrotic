package com.ruviapps.tacklingnephrotic.ui.userrole

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.ruviapps.tacklingnephrotic.database.dto.onFailure
import com.ruviapps.tacklingnephrotic.database.dto.onSuccess
import com.ruviapps.tacklingnephrotic.domain.use_cases.caretaker.CareTakerUseCases
import com.ruviapps.tacklingnephrotic.utility.Event
import com.ruviapps.tacklingnephrotic.utility.NavigationCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class UserRoleViewModel @Inject constructor(
   private val careTakerUseCases: CareTakerUseCases
) : ViewModel() {

    private val auth = AuthUI.getInstance().auth
    private val firebaseUser =   auth.currentUser
    private val uid = firebaseUser?.uid.toString()

    private var _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation : LiveData<Event<NavigationCommand>>
    get() = _navigation

    fun saveCareTaker(){
       val name = firebaseUser?.displayName.toString()
        val phoneNumber = (firebaseUser?.phoneNumber)
        val phoneString :String

        if(phoneNumber==null){
           phoneString = "0000000000"
        }else
            phoneString = phoneNumber

        val email = firebaseUser?.email.toString()

        viewModelScope.launch {
                val query =
                    careTakerUseCases.registerCareTakerUseCase(uid, name, email, phoneString, "")
                query.onSuccess { _, _ ->
                    _navigation.value =
                        Event(NavigationCommand.ToDirection(UserRoleFragmentDirections.actionNavUserRoleToNavResult()))
                }

                query.onFailure { message, _ ->
                    _navigation.value = Event(NavigationCommand.ShowError(message))
                }
            }

    }

  private suspend  fun isUserAlreadyPresent() : Boolean{
        var userPresent : Boolean
      withContext(viewModelScope.coroutineContext) {
          userPresent = careTakerUseCases.isValidCareTaker(uid)
      }
      /* viewModelScope.async {
        userPresent = careTakerUseCases.isValidCareTaker(uid)
        }.await()*/
      return userPresent
    }

    fun verifyUser(){
        viewModelScope.launch {
            if (isUserAlreadyPresent())
                _navigation.value =
                    Event(NavigationCommand.ToDirection(UserRoleFragmentDirections.actionNavUserRoleToNavResult()))
        }
    }

}
