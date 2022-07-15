package com.ruviapps.tacklingnephrotic.ui.userrole

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ruviapps.tacklingnephrotic.databinding.UserRoleBinding
import com.ruviapps.tacklingnephrotic.utility.BaseFragment

class UserRoleFragment : BaseFragment() {

    override val isBottomBarVisible: Int = View.GONE
    override val isFabVisible: Int = View.GONE

    private lateinit var binding : UserRoleBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = UserRoleBinding.inflate(inflater,container,false)
        return  binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    }
}