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
    ): View? {
        observerNavigation()
        viewModel.verifyUser()

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
            viewModel.saveCareTaker()
        }



    }//end of onView Created

    private fun observerNavigation() {
        viewModel.navigation.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled().let { navigationCommand ->
                when (navigationCommand) {
                    is NavigationCommand.ToDirection ->
                        findNavController().navigate(navigationCommand.directions)
                    is NavigationCommand.ShowError -> {
                        Toast.makeText(requireContext(),
                            navigationCommand.errMsg,
                            Toast.LENGTH_SHORT).show()
                    }
                    else -> {

                    }

                }
            }
        }
    }
}