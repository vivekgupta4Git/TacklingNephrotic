package com.ruviapps.tacklingnephrotic.ui.userrole

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ruviapps.tacklingnephrotic.databinding.UserRoleBinding
import com.ruviapps.tacklingnephrotic.utility.BaseFragment
import com.ruviapps.tacklingnephrotic.utility.NavigationCommand
import com.ruviapps.tacklingnephrotic.utility.observeAndHandleEvent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserRoleFragment : BaseFragment() {

    override val isBottomBarVisible: Int = View.GONE
    override val isFabVisible: Int = View.GONE
    private val viewModel: UserRoleViewModel by viewModels()

    private lateinit var binding: UserRoleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        observerNavigation()
        binding = UserRoleBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val continueButton = binding.continueButton
        val cTCard = binding.careTakerCard
        val pCard = binding.patientCard

        cTCard.setOnClickListener {
            cTCard.isChecked = true
            pCard.isChecked = false
        }
        pCard.setOnClickListener {
            pCard.isChecked = true
            cTCard.isChecked = false
        }

        continueButton.setOnClickListener {
            if(pCard.isChecked || cTCard.isChecked)
            {
                if(pCard.isChecked){
                 //user is also a patient
                    //instead of saving details here, I should show user another screen to fill the necessary details like weight
                    viewModel.savePatient()
                }else
                {
                    //user is only a care taker to patient
                    viewModel.saveCareTaker()
                }
            }else
            {
                //user hasn't selected any role
                viewModel.showSnackBar()
            }
        }


    }//end of onView Created

    private fun observerNavigation() {
        viewModel.navigation.observeAndHandleEvent(this)
    }
}