package com.ruviapps.tacklingnephrotic.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.ruviapps.tacklingnephrotic.R
import com.ruviapps.tacklingnephrotic.databinding.FragmentGalleryBinding
import com.ruviapps.tacklingnephrotic.domain.Patient
import com.ruviapps.tacklingnephrotic.ui.test_result.BaseFragment
import com.ruviapps.tacklingnephrotic.utility.NavigationCommand
import com.ruviapps.tacklingnephrotic.utility.observeAndHandleEvent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GalleryFragment : BaseFragment() {

    override var bottomAppBarVisibility: Int = View.GONE
    override var fabVisibility: Int = View.GONE
    private lateinit var _binding: FragmentGalleryBinding
    private  val galleryViewModel: GalleryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = AuthUI.getInstance().auth.currentUser?.uid ?: ""
        _binding.newPatient = Patient(0,"",1,0f,"",uid)
        _binding.viewModel = galleryViewModel
        _binding.lifecycleOwner = this

      galleryViewModel.navigation.observeAndHandleEvent(this)


    }


}

