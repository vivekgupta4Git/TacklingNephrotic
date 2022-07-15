package com.ruviapps.tacklingnephrotic.utility

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ruviapps.tacklingnephrotic.MainActivity

open class BaseFragment : Fragment() {

    open val isBottomBarVisible = View.VISIBLE
    open val isFabVisible =View.VISIBLE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val activity = requireActivity() as MainActivity
        activity.setFabVisibility(isFabVisible)
        activity.setBottomBarVisibility(isBottomBarVisible)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val activity = requireActivity() as MainActivity
        activity.setFabVisibility(isFabVisible)
        activity.setBottomBarVisibility(isBottomBarVisible)

    }
}