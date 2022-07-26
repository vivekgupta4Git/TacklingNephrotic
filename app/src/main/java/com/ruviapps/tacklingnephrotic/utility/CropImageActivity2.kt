package com.ruviapps.tacklingnephrotic.utility

import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import com.canhub.cropper.*
import com.canhub.cropper.databinding.CropImageActivityBinding

open class CropImageActivity2 : CropImageActivity() {

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         val binding = CropImageActivityBinding.inflate(layoutInflater)
        val root = binding.root as FrameLayout
        val button = Button(this)
        button.text ="Crop Image"
        button.setOnClickListener {
            cropImage()
        }
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER_HORIZONTAL
        params.setMargins(10,10,10,10)
        root.addView(button,params)
        setContentView(binding.root)
         super.setCropImageView(binding.cropImageView)

    }



}