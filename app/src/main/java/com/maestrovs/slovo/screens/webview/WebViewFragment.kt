package com.maestrovs.slovo.screens.webview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maestrovs.slovo.R
import com.maestrovs.slovo.databinding.FragmentWebViewBinding
import com.maestrovs.slovo.screens.MainActivity
import com.maestrovs.slovo.screens.MainViewModel
import com.maestrovs.slovo.screens.base.BaseFragment
import com.maestrovs.slovo.screens.extensions.applyFont
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewFragment : BaseFragment() {

    companion object {
        fun newInstance() = WebViewFragment()
    }

    private val mainScreenViewModel by lazy {
        ViewModelProvider(activity as MainActivity).get(MainViewModel::class.java).apply {
            lifecycle.addObserver(this)
        }
    }

    private val webViewModel by lazy {
        ViewModelProvider(this).get(WebViewViewModel::class.java).apply {
            lifecycle.addObserver(this)
        }
    }


    private lateinit var viewModel: WebViewViewModel

    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    private val safeArgs: WebViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        webViewModel.navController = findNavController()
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        val root: View = binding.root
        applyFont(requireContext(), root, "fonts/Comfortaa-Regular.ttf")



      binding.backButton.setOnClickListener{

          findNavController().popBackStack(R.id.fragment_main_screen, false)
      }

        val slovo = safeArgs.slovo

        binding.tvTitle.text = slovo

        binding.uiProgress.visibility = View.VISIBLE

        Log.d("Game",">>>>>>>>")
        webViewModel.getSlovo(slovo) { data ->

            val mimeType = "text/html"
            val encoding = "UTF-8"
            val html = data
            binding.mWebView.loadDataWithBaseURL("", html, mimeType, encoding, "")
            binding.uiProgress.visibility = View.GONE
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WebViewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}