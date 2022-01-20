package com.example.authflow.presentation.verify_email

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.authflow.databinding.FragmentVerifyEmailBinding
import com.example.authflow.presentation.MainViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyEmailFragment : Fragment() {

    private lateinit var verifyEmailBinding: FragmentVerifyEmailBinding
    val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        verifyEmailBinding = FragmentVerifyEmailBinding.inflate(inflater, container, false)
        val view = verifyEmailBinding.root


        //val user = requireArguments().getParcelable<FirebaseUser>("firebase_user")!!



        verifyEmailBinding.verifyUserBt.setOnClickListener {
            mainViewModel.sendVerificationEmail()

        }




        return view
    }

}