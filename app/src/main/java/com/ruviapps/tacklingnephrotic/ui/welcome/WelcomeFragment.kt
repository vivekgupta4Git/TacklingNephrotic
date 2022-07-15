package com.ruviapps.tacklingnephrotic.ui.welcome

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.registerForActivityResult
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.ruviapps.tacklingnephrotic.R
import com.ruviapps.tacklingnephrotic.databinding.FragmentWelcomeBinding
import com.ruviapps.tacklingnephrotic.ui.test_result.BaseFragment

class WelcomeFragment : BaseFragment() {

    companion object {
        const val TAG = "WelcomeFragment"
    }
    private lateinit var binding : FragmentWelcomeBinding
    private lateinit var viewModel: WelcomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater)
        return  binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //first time user screen
        /**
         * Greet user and Ask for its role
         * CareTaker/Parent or Patient
         *
         */
        //second time user screen
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WelcomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}