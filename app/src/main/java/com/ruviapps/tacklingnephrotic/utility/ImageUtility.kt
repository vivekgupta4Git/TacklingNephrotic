package com.ruviapps.tacklingnephrotic.utility

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.ruviapps.tacklingnephrotic.BuildConfig
import java.io.File
import java.util.*

enum class ImagesDirectory{
    CACHE,
    DATA
}
object ImageUtility {

    fun getTmpPicture(context: Context,directoryToUse : ImagesDirectory= ImagesDirectory.CACHE) : Uri?{
     val directory =   when(directoryToUse){
            ImagesDirectory.CACHE -> context.cacheDir
            ImagesDirectory.DATA -> context.filesDir
        }
        val tmpFile = File.createTempFile(Calendar.getInstance().timeInMillis.toString(),".jpeg",directory).apply {
            createNewFile()
            if(directoryToUse== ImagesDirectory.CACHE)
            deleteOnExit()

        }
        return   FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }
}