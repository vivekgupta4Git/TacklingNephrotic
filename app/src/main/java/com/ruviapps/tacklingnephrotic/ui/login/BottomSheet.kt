package com.ruviapps.tacklingnephrotic.ui.login

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ruviapps.tacklingnephrotic.MainActivity
import com.ruviapps.tacklingnephrotic.R
import com.ruviapps.tacklingnephrotic.databinding.FragmentBottomSheetBinding
import com.ruviapps.tacklingnephrotic.utility.NavigationCommand


class BottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetBinding
    private val sharedViewModel: LoginViewModel by activityViewModels()

    private val signInLaunch = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        sharedViewModel.handleResult(result)
        observeNavigation()
    }

    private fun observeNavigation() {
        sharedViewModel.navigation.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { command ->
                when (command) {
                    is NavigationCommand.ToDirection ->
                        findNavController().navigate(command.directions)
                    is NavigationCommand.ShowError -> {
                        Toast.makeText(requireContext(), command.errMsg, Toast.LENGTH_SHORT).show()
                    }
                    else -> {

                    }
                }
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            signInLaunch.launch(sharedViewModel.getSignInIntent(LoginViewModel.Provider.GOOGLE_PROVIDER))
        }

        val phoneButton = binding.phoneBtn
        phoneButton.setOnClickListener {
            signInLaunch.launch(sharedViewModel.getSignInIntent(LoginViewModel.Provider.PHONE_PROVIDER))

        }


    }
}