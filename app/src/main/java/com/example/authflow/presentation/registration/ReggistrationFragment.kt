package com.example.authflow.presentation.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.authflow.R
import com.example.authflow.databinding.FragmentReggistrationBinding
import com.example.authflow.presentation.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReggistrationFragment : Fragment() {

    private lateinit var registrationBinding: FragmentReggistrationBinding
    val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        registrationBinding = FragmentReggistrationBinding.inflate(inflater, container, false)
        val view = registrationBinding.root



        registrationBinding.registerUser.setOnClickListener {
            mainViewModel.createUser(
                registrationBinding.userEmailEtReg.text.toString(),
                registrationBinding.passwordEtReg.text.toString(),
                registrationBinding.confirmPasswordEt.text.toString()
            )
        }






        return view
    }

}