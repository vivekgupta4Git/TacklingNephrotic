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
import com.ruviapps.tacklingnephrotic.utility.observeAndHandleEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatientPickerDialogFragment : DialogFragment() {

    private val args : PatientPickerDialogFragmentArgs by navArgs()
    private lateinit var binding : FragmentPatientDialogBinding
    private val viewModel : PatientDialogViewModel by viewModels()

    /* The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */
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

    /** The system calls this only when creating the layout in a dialog. *//*
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setTitle("Select Any one Patient to continue")
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }
*/

}
/*
Tip: If you want a custom dialog, you can instead display an Activity as a dialog
instead of using the Dialog APIs. Simply create an activity and set its theme to
Theme.Holo.Dialog in the <activity> manifest element:

<activity android:theme="@android:style/Theme.Holo.Dialog" >
That's it. The activity now displays in a dialog window instead of fullscreen.
 */