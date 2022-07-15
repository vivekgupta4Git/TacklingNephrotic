package com.ruviapps.tacklingnephrotic

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.ruviapps.tacklingnephrotic.ui.BottomSheet


class LoginActivity : AppCompatActivity() {
companion object{
    const val INTENT_EXTRA_USERNAME = "userName"
    const val INTENT_EXTRA_EMAIL = "email"
    const val INTENT_EXTRA_PHONE="phone"
    const val INTENT_EXTRA_IS_NEW_USER = "newUser"
}
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ){
        result->
        handleResult(result)
    }
    private  val providers = arrayListOf( AuthUI.IdpConfig.EmailBuilder().build())

    private fun handleResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if(result.resultCode == RESULT_OK){
            val intent = Intent(this,MainActivity::class.java).apply {
                putExtra(INTENT_EXTRA_USERNAME,"${response?.user}")
                putExtra(INTENT_EXTRA_EMAIL,"${response?.email}")
                putExtra(INTENT_EXTRA_PHONE,"${response?.phoneNumber}")
                putExtra(INTENT_EXTRA_IS_NEW_USER,"${response?.isNewUser}")
            }
            startActivity(intent)
            finish()
        }else{
            if(response===null){
                    when(response?.error?.errorCode){
                        ErrorCodes.NO_NETWORK -> Toast.makeText(this,getString(R.string.no_internet_exclaim),Toast.LENGTH_LONG).show()
                        ErrorCodes.UNKNOWN_ERROR -> Toast.makeText(this,getString(R.string.unknown_error_with_error_code)
                                +  "${response.error?.errorCode}",Toast.LENGTH_SHORT).show()
                        else-> Toast.makeText(this,"Login Failed  ",Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_login)

        val signIn = findViewById<MaterialTextView>(R.id.signIn)
        val wordTwo: Spannable = SpannableString(getString(R.string.sign_in))

     /*   wordTwo.setSpan(ForegroundColorSpan(R.color.md_theme_light_onPrimary),
            0,
            wordTwo.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)*/
        wordTwo.setSpan(
            UnderlineSpan(),
            0,wordTwo.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        signIn.append(wordTwo)
        signIn.isClickable = true

        val getStartedButton = findViewById<MaterialButton>(R.id.getStartedButton)
        getStartedButton.setOnClickListener {
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(true)       //for testing purpose
                .setLogo(R.mipmap.ic_launcher)
                .setTheme(R.style.Theme_MyLoginTheme)
                .setAvailableProviders(providers)
                .build()
            lifecycleScope.launchWhenStarted {
                signInLauncher.launch(signInIntent)
            }
        }

        signIn.setOnClickListener {
            val bottomFragment = BottomSheet()
            bottomFragment.show(supportFragmentManager,bottomFragment.tag)
        }


    }


}