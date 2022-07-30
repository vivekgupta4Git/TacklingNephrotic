package com.ruviapps.tacklingnephrotic.ui.patient_profile

import android.net.Uri
import androidx.lifecycle.*
import com.firebase.ui.auth.AuthUI
import com.google.android.material.imageview.ShapeableImageView
import com.ruviapps.tacklingnephrotic.R
import com.ruviapps.tacklingnephrotic.database.dto.onFailure
import com.ruviapps.tacklingnephrotic.database.dto.onSuccess
import com.ruviapps.tacklingnephrotic.domain.Patient
import com.ruviapps.tacklingnephrotic.domain.use_cases.patient.PatientUseCases
import com.ruviapps.tacklingnephrotic.utility.Event
import com.ruviapps.tacklingnephrotic.utility.NavigationCommand
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PatientProfileViewModel @Inject constructor(
    private val patientUseCases: PatientUseCases,
): ViewModel() {

    /*
    logged in userId to be used while saving patient details
     */
   private val uid = AuthUI.getInstance().auth.currentUser?.uid ?: ""

    /*
    temporary profile pic uri
     */
    private var tempProfilePicUri: Uri? = null


   val patient : Patient = Patient(0,"",0,0f,"",uid)


    fun setTemporaryProfilePic(uri: Uri?){
        tempProfilePicUri = uri
        if(uri!=null)
        setProfilePic(uri.toString())
    }
    fun getTemporaryProfilePic() : Uri?{
        return tempProfilePicUri
    }

    fun onPickImageResult(resultUri: Uri?, targetView: ShapeableImageView) {
        Picasso.get()
            .load(resultUri)
            .resize(200,200)
            .centerCrop()
            .placeholder(R.mipmap.patient)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(targetView)
    }

    private fun setProfilePic(uri : String){
        patient.patientPicUri = uri
    }

    private var _insertionComplete = MutableLiveData<Boolean>().apply {
        value = false
    }

    private var _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation : LiveData<Event<NavigationCommand>>
    get() = _navigation



   fun savePatientDetails() {
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
                            .ToDirection(PatientProfileFragmentDirections
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