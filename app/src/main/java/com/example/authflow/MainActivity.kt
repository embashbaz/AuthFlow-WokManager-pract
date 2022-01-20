package com.example.authflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.navigateUp
import com.example.authflow.presentation.MainViewModel
import com.example.authflow.presentation.loggedin_page.LoggedinFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleNavigation()
    }


    fun handleNavigation(){
        val navController = findNavController(R.id.myNavHostFragment)

       collectLatestLifecycleFlowActivity(mainViewModel.authFlowState){
           when(it){
               is MainViewModel.AuthState.LoggedIn -> {
                   navController.navigateUp()
                    navController.navigate(R.id.verifyEmailFragment)
               }

               is MainViewModel.AuthState.EmailVerified -> {
                   navController.navigateUp()
                   navController.navigate(R.id.loggedinFragment)
               }

               is MainViewModel.AuthState.LoggedOut -> {
                   navController.navigateUp()
                   navController.navigate(R.id.logginFragment)
               }

               is MainViewModel.AuthState.OpenRegistration -> {
                   navController.navigateUp()
                   navController.navigate(R.id.reggistrationFragment)
               }


           }
       }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val lsActiveFragments: List<Fragment> = supportFragmentManager.fragments
        for (fragmentActive in lsActiveFragments) {
            if (fragmentActive is NavHostFragment) {
                val lsActiveSubFragments: List<Fragment> =
                    fragmentActive.getChildFragmentManager().getFragments()
                for (fragmentActiveSub in lsActiveSubFragments) {
                    if (fragmentActiveSub is LoggedinFragment) {
                        fragmentActiveSub.onActivityResult(requestCode, resultCode, data)
                    }
                }
            }
        }

    }
}