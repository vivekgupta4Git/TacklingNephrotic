package com.ruviapps.tacklingnephrotic

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.google.android.material.color.DynamicColors;

@HiltAndroidApp
class NephApplication  : Application() {

    override fun onCreate() {
        super.onCreate()
            DynamicColors.applyToActivitiesIfAvailable(this)
    }
}