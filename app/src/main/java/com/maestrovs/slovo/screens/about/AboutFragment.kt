package com.maestrovs.slovo.screens.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maestrovs.slovo.R
import com.maestrovs.slovo.databinding.FragmentAboutBinding
import com.maestrovs.slovo.databinding.FragmentGameEndBinding
import com.maestrovs.slovo.screens.base.BaseFragment
import com.maestrovs.slovo.screens.extensions.applyFont
import com.maestrovs.slovo.screens.MainActivity
import com.maestrovs.slovo.screens.MainViewModel
import com.maestrovs.slovo.screens.main.MainScreenFragmentDirections


class AboutFragment : BaseFragment() {


    private val mainScreenViewModel by lazy {
        ViewModelProvider(activity as MainActivity).get(MainViewModel::class.java).apply {
            lifecycle.addObserver(this)
        }
    }

    private val aboutViewModel by lazy {
        ViewModelProvider(this).get(AboutViewModel::class.java).apply {
            lifecycle.addObserver(this)
        }
    }
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity



    var canShowMeaningOfWord = true;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        aboutViewModel.navController = findNavController()
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        val root: View = binding.root
       // applyFont(requireContext(), root, "fonts/Comfortaa-Regular.ttf")

            /*binding.backButton.setOnClickListener{

            findNavController().popBackStack()//popBackStack(R.id.fragment_main_screen, false)
        }*/


        return root
    }




    override fun onDestroyView() {
        super.onDestroyView()
        // binding.clMessage.animate().cancel()
        _binding = null
    }


}