package com.ruviapps.tacklingnephrotic.utility

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

fun LiveData<Event<NavigationCommand>>.observeAndHandleEvent(fragment: Fragment){
    this.observe(fragment.viewLifecycleOwner){
            event ->
        event.getContentIfNotHandled()?.let { command ->
            when (command) {
                is NavigationCommand.ToDirection ->
                   fragment.findNavController().navigate(command.directions)
                is NavigationCommand.ShowError -> {
                    Toast.makeText(fragment.requireContext(), command.errMsg, Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        }
    }
}
