package com.ruviapps.tacklingnephrotic.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.facebook.*
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ruviapps.tacklingnephrotic.LoginActivity
import com.ruviapps.tacklingnephrotic.MainActivity
import com.ruviapps.tacklingnephrotic.R
import com.ruviapps.tacklingnephrotic.databinding.FragmentBottomSheetBinding

/**
 * A simple [Fragment] subclass.
 * Use the [BottomSheet.newInstance] factory method to
 * create an instance of this fragment.
 */

class BottomSheet : BottomSheetDialogFragment() {

  //  private lateinit var auth: FirebaseAuth
    /*private val callbackManager = CallbackManager.Factory.create()   */
    private lateinit var binding : FragmentBottomSheetBinding

    private val signInLaunch = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ){
            result ->
        handleResult(result)
      /*  if(result!=null) {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                dismiss()
                requireActivity().finish()
            }else
            {
                Snackbar.make(binding.root,"Login Failed",Snackbar.LENGTH_LONG).show()
                dismiss()
            }
*/

    }
    private fun handleResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if(result.resultCode == AppCompatActivity.RESULT_OK){
            val intent = Intent(requireActivity(),MainActivity::class.java).apply {
                putExtra(LoginActivity.INTENT_EXTRA_USERNAME,"${response?.user}")
                putExtra(LoginActivity.INTENT_EXTRA_EMAIL,"${response?.email}")
                putExtra(LoginActivity.INTENT_EXTRA_PHONE,"${response?.phoneNumber}")
                putExtra(LoginActivity.INTENT_EXTRA_IS_NEW_USER,"${response?.isNewUser}")
            }
            startActivity(intent)
            requireActivity().finish()
        }else{
            if(response == null) {
                Toast.makeText(requireContext(), getString(R.string.login_failed_msg), Toast.LENGTH_SHORT).show()
            }else{
                when(response.error?.errorCode){
                    ErrorCodes.NO_NETWORK -> Toast.makeText(requireContext(),getString(R.string.no_internet_exclaim),Toast.LENGTH_LONG).show()
                    ErrorCodes.UNKNOWN_ERROR -> Toast.makeText(requireContext(),getString(R.string.unknown_error_with_error_code)
                            +  "${response.error?.errorCode}",Toast.LENGTH_SHORT).show()
                    else-> Toast.makeText(requireContext(),getString(R.string.login_failed_msg2),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBottomSheetBinding.inflate(layoutInflater)
//        auth = Firebase.auth
/*
        FacebookSdk.sdkInitialize(requireContext())
*/
        // Inflate the layout for this fragment
        return binding.root
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       binding.googleButton.setOnClickListener {
           val sigInIntent = AuthUI.getInstance().createSignInIntentBuilder()
               .setIsSmartLockEnabled(false)
               .setAvailableProviders(arrayListOf( AuthUI.IdpConfig.GoogleBuilder().build()))
               .setLogo(R.mipmap.ic_launcher)
               .setTheme(R.style.Theme_App_MyTheme)
               .build()
           signInLaunch.launch(sigInIntent)

       }

       val phoneButton = binding.phoneBtn
       phoneButton.setOnClickListener {
           val signInIntent = AuthUI.getInstance().createSignInIntentBuilder()
               .setAvailableProviders(arrayListOf(AuthUI.IdpConfig.PhoneBuilder().build()))
               .setIsSmartLockEnabled(false)
               .setLogo(R.mipmap.ic_launcher)
               .setTheme(R.style.Theme_App_MyTheme)
               .build()
           signInLaunch.launch(signInIntent)

       }

/*
       val faceBookLoginButton = binding.facebookBtn
       faceBookLoginButton.setReadPermissions("email", "public_profile")

       faceBookLoginButton.registerCallback(callbackManager,object :
                FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Log.d("facebook", "facebook:onSuccess:$loginResult")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d("facebook", "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d("facebook", "facebook:onError", error)
                }
            })

*/

       }


    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("facebook", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("facebook", "signInWithCredential:success")
                    val user = auth.currentUser
                    Log.d("facebook","User = $user")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("facebook", "signInWithCredential:failure", task.exception)
                    Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    Log.d("facebook","Error login using facebook")
                }
            }
    }*/
}