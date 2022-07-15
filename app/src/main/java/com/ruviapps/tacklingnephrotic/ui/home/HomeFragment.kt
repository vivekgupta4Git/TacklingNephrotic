package com.ruviapps.tacklingnephrotic.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.ruviapps.tacklingnephrotic.databinding.FragmentHomeBinding
import com.ruviapps.tacklingnephrotic.ui.test_result.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null

    private val args: HomeFragmentArgs by navArgs()

     val homeViewModel : HomeViewModel by viewModels()
        // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        val textView2 = binding.textView2
        val btton2 = binding.button2
        btton2.setOnClickListener {
            textView2.text = ""
            homeViewModel.getEntriesByDate()
        }

        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        lifecycleScope.launch {
            homeViewModel.list.collect {
                it.forEach { res->
                    textView2.append("\n")
                    textView2.append(res.toString())
                    textView2.append("\n")

                }

            }
        }



        val reading = args.userTest
        homeViewModel.changeText(reading)
        return root
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getEntriesByDate()
        homeViewModel.missedReadings()

        val textView4 = binding.textView4
        homeViewModel.date.observe(viewLifecycleOwner){
        textView4.text = ""
            it.forEach { d->
                textView4.append(d.dayOfMonth.toString()  + " "+  d.month.toString() + " " + d.year.toString())
                textView4.append("\n")
            }
        }

        val textView3 = binding.textView3
        homeViewModel.state.observe(viewLifecycleOwner){
            textView3.text = it.name
        }
        val btton = binding.button
        btton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                homeViewModel.revealState(1, LocalDate.now())
            }

        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}