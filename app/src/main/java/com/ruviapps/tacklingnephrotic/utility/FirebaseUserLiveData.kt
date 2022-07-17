package com.ruviapps.tacklingnephrotic.utility

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseUserLiveData : LiveData<FirebaseUser?>() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val authStateChangeListener = FirebaseAuth.AuthStateListener {
        value = firebaseAuth.currentUser
    }

    override fun onActive() =firebaseAuth.addAuthStateListener(authStateChangeListener)
    override fun onInactive() = firebaseAuth.removeAuthStateListener(authStateChangeListener)
}