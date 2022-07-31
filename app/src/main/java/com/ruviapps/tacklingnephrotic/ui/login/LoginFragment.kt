package com.ruviapps.tacklingnephrotic.ui.login

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.ruviapps.tacklingnephrotic.R
import com.ruviapps.tacklingnephrotic.ui.login.LoginViewModel.*
import com.ruviapps.tacklingnephrotic.ui.select_patient_dialog.PatientPickerDialogFragment
import com.ruviapps.tacklingnephrotic.utility.BaseFragment
import com.ruviapps.tacklingnephrotic.utility.observeAndHandleEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    override val isBottomBarVisible: Int
        get() = View.GONE
    override val isFabVisible: Int
        get() = View.GONE

    private val viewModel : LoginViewModel by viewModels()

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
            viewModel.handleResult(result)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.activity_login,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val signIn = view.findViewById<MaterialTextView>(R.id.signIn)
        val wordTwo: Spannable = SpannableString(getString(R.string.sign_in))

        wordTwo.setSpan(
            UnderlineSpan(),
            0, wordTwo.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        signIn.append(wordTwo)
        signIn.isClickable = true

        val getStartedButton = view.findViewById<MaterialButton>(R.id.getStartedButton)
        getStartedButton.setOnClickListener {
            signInLauncher.launch(viewModel.getSignInIntent(Provider.GOOGLE_PROVIDER))
        }

        signIn.setOnClickListener {
            val bottomFragment = BottomSheet()
            bottomFragment.show(childFragmentManager, bottomFragment.tag)
        }
        viewModel.navigation.observeAndHandleEvent(this)


    }

}


