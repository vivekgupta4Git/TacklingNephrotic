package com.ruviapps.tacklingnephrotic.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.contract.ActivityResultContract
import com.canhub.cropper.*

class CropImageContract2 : ActivityResultContract<CropImageContractOptions, CropImageView.CropResult>() {
    override fun createIntent(context: Context, input: CropImageContractOptions): Intent {
        input.cropImageOptions.validate()
        return Intent(context, CropImageActivity2::class.java).apply {
            val bundle = Bundle()
            bundle.putParcelable(CropImage.CROP_IMAGE_EXTRA_SOURCE, input.uri)
            bundle.putParcelable(CropImage.CROP_IMAGE_EXTRA_OPTIONS, input.cropImageOptions)
            putExtra(CropImage.CROP_IMAGE_EXTRA_BUNDLE, bundle)
        }
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ): CropImageView.CropResult {
        val result = intent?.getParcelableExtra<Parcelable>(CropImage.CROP_IMAGE_EXTRA_RESULT) as? CropImage.ActivityResult?

        return if (result == null || resultCode == Activity.RESULT_CANCELED)
            CropImage.CancelledResult
        else result
    }
}