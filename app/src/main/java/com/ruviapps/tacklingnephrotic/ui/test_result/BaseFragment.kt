package com.ruviapps.tacklingnephrotic.ui.test_result

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ruviapps.tacklingnephrotic.MainActivity

abstract class BaseFragment : Fragment() {

    protected open var bottomAppBarVisibility = View.VISIBLE
    protected open var fabVisibility = View.VISIBLE


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(activity is MainActivity){
            val mainActivity = activity as MainActivity

            mainActivity.setBottomBarVisibility(bottomAppBarVisibility)
            mainActivity.setFabVisibility(fabVisibility)
        }
    }


    override fun onResume() {
        super.onResume()
        if(activity is MainActivity){
            val mainActivity = activity as MainActivity

            mainActivity.setBottomBarVisibility(bottomAppBarVisibility)
            mainActivity.setFabVisibility(fabVisibility)
        }
    }
}