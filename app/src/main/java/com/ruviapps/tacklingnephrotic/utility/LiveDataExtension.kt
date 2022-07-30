package com.ruviapps.tacklingnephrotic.utility

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

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
                is NavigationCommand.ShowSnackBar -> {
                    Snackbar.make(fragment.requireView(),command.msg.toString(),Snackbar.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        }
    }
}
