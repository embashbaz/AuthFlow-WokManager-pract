package com.example.authflow.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.authflow.databinding.FragmentLogginBinding
import com.example.authflow.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogginFragment : Fragment() {

    private lateinit var logginBinding: FragmentLogginBinding
    val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logginBinding = FragmentLogginBinding.inflate(inflater, container, false)
        val view = logginBinding.root

        logginBinding.loginUser.setOnClickListener {
            mainViewModel.signIn(logginBinding.userEmailEt.text.toString(), logginBinding.passwordEt.text.toString())

        }

        logginBinding.registerBt.setOnClickListener {
            mainViewModel.launchRegistrationScree()
        }




        return view
    }

}