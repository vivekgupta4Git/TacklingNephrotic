package com.ruviapps.tacklingnephrotic.ui.select_patient_dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ruviapps.tacklingnephrotic.databinding.FragmentPatientDialogBinding
import com.ruviapps.tacklingnephrotic.domain.Patient
import com.ruviapps.tacklingnephrotic.utility.BaseFragment
import com.ruviapps.tacklingnephrotic.utility.observeAndHandleEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatientPickerDialogFragment : BaseFragment() {

    override val isBottomBarVisible: Int
        get() = View.GONE
    override val isFabVisible: Int
        get() = View.GONE

    private val args : PatientPickerDialogFragmentArgs by navArgs()
    private lateinit var binding : FragmentPatientDialogBinding
    private val viewModel : PatientDialogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPatientDialogBinding.inflate(inflater)
        return binding.root
    }


       override fun onResume() {
        super.onResume()
        val recyclerView = binding.patientRecyclerView
           val adapter = PatientListAdapter(PatientClickListener { id->
               viewModel.setSelectedPatientId(id)
           })
           viewModel.getPatientList(args.careTakerId)
        viewModel.patientList.observe(viewLifecycleOwner){patientList->
            adapter.submitList(patientList)
            recyclerView.adapter = adapter
        }
           viewModel.navigation.observeAndHandleEvent(this)
    }



}
