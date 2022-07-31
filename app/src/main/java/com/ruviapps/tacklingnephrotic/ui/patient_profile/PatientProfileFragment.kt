package com.ruviapps.tacklingnephrotic.ui.patient_profile

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.fragment.app.viewModels
import com.google.android.material.imageview.ShapeableImageView
import com.ruviapps.tacklingnephrotic.R
import com.ruviapps.tacklingnephrotic.databinding.FragmentPatientProfileBinding
import com.ruviapps.tacklingnephrotic.ui.test_result.BaseFragment
import com.ruviapps.tacklingnephrotic.utility.ImageUtility
import com.ruviapps.tacklingnephrotic.utility.ImagesDirectory
import com.ruviapps.tacklingnephrotic.utility.observeAndHandleEvent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PatientProfileFragment : BaseFragment() {



    private lateinit var imageView : ShapeableImageView
    private var latestTmpUri: Uri? = null

    override var bottomAppBarVisibility: Int = View.GONE
    override var fabVisibility: Int = View.GONE
    private lateinit var _binding: FragmentPatientProfileBinding
    private  val patientProfileViewModel: PatientProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPatientProfileBinding.inflate(inflater, container, false)
        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.viewModel = patientProfileViewModel
        _binding.lifecycleOwner = this
        imageView = _binding.patientProfilePic

        _binding.profileUploadButton.setOnClickListener {
            showDialog()
        }


        patientProfileViewModel.navigation.observeAndHandleEvent(this)


    }

    override fun onResume() {
        super.onResume()
       val picUri = patientProfileViewModel.getTemporaryProfilePic()
       patientProfileViewModel.onPickImageResult(picUri,imageView)
    }

    private val pickImageGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            patientProfileViewModel.setTemporaryProfilePic(uri)
            patientProfileViewModel.onPickImageResult(uri,imageView)
        }
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            picTaken->
        if (picTaken) {
            latestTmpUri.let {
                patientProfileViewModel.setTemporaryProfilePic(it)
                patientProfileViewModel.onPickImageResult(it,imageView)
            }
        }
        else {
           patientProfileViewModel.onPickImageResult(null,imageView)
        }
    }

    private fun takePicture(){
        latestTmpUri = ImageUtility.getTmpPicture(requireContext(),ImagesDirectory.DATA)
        latestTmpUri.let {
            takePicture.launch(it)
        }

    }

    private fun showDialog(){
        AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setTitle(R.string.pick_profile_pic_title)
            .setItems(
                arrayOf(
                    getString(R.string.pick_image_camera),
                    getString(R.string.pick_image_gallery),
                )
            ) { _, position ->
                if (position == 0)
                    takePicture()
                else
                    pickImageGallery.launch("image/*")
            }
            .show()
    }




}

