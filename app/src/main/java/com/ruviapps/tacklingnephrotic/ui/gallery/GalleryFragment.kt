package com.ruviapps.tacklingnephrotic.ui.gallery

import android.app.AlertDialog
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.fragment.app.viewModels
import com.google.android.material.imageview.ShapeableImageView
import com.ruviapps.tacklingnephrotic.R
import com.ruviapps.tacklingnephrotic.databinding.FragmentGalleryBinding
import com.ruviapps.tacklingnephrotic.ui.test_result.BaseFragment
import com.ruviapps.tacklingnephrotic.utility.ImageUtility
import com.ruviapps.tacklingnephrotic.utility.ImagesDirectory
import com.ruviapps.tacklingnephrotic.utility.observeAndHandleEvent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GalleryFragment : BaseFragment() {



    private lateinit var imageView : ShapeableImageView
    private var latestTmpUri: Uri? = null

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
        _binding.viewModel = galleryViewModel
        _binding.lifecycleOwner = this
        imageView = _binding.patientProfilePic

        _binding.profileUploadButton.setOnClickListener {
            showDialog()
        }


        galleryViewModel.navigation.observeAndHandleEvent(this)


    }

    override fun onResume() {
        super.onResume()
       val picUri = galleryViewModel.getTemporaryProfilePic()
       galleryViewModel.onPickImageResult(picUri,imageView)
    }

    private val pickImageGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            galleryViewModel.setTemporaryProfilePic(uri)
            galleryViewModel.onPickImageResult(uri,imageView)
        }
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            picTaken->
        if (picTaken) {
            latestTmpUri.let {
                galleryViewModel.setTemporaryProfilePic(it)
                galleryViewModel.onPickImageResult(it,imageView)
            }
        }
        else {
           galleryViewModel.onPickImageResult(null,imageView)
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

/**
 * Imageview doesn't have get property defined for android:src , so defining one
 */

@InverseBindingAdapter(attribute = "android:src", event = "android:srcAttrChanged")
fun getImageUri(view: ImageView): String {
return ""
}


/*
listener for change in attribute value of android:src
 */
@BindingAdapter(value= ["android:srcAttrChanged"],requireAll = false)
fun setImageUriChangedWatcher(view: ImageView,listener : InverseBindingListener?){

/*
val newListener : OnImageUriChanged?

    if(listener==null)
        newListener= null
    else
     newListener = object : OnImageUriChanged {
        override fun onImageUriChanged() {
            Log.d("ruviApps","On imageUri changed")
        }
    }
 */

}

/*
interface OnImageUriChanged{
    fun onImageUriChanged()
}*/

