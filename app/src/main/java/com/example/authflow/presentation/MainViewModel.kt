package com.example.authflow.presentation

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.authflow.KEY_IMAGE_URI
import com.example.authflow.data.AuthFlowRepository
import com.example.authflow.work.UploadImageWorker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: AuthFlowRepository, private val workManager : WorkManager) : ViewModel() {

    private val _authFlowState = MutableStateFlow<AuthState>(AuthState.LoggedOut())
    val authFlowState = _authFlowState.asStateFlow()

   // private lateinit var workManager : WorkManager



    private val _registrationUser = MutableLiveData<FirebaseUser>()
    val exposeUser: LiveData<FirebaseUser>
        get() = _registrationUser

    init {
        checkAuthStatus()
    }


    fun createUser(email: String, password: String, confirmPassword: String) {

        if (email.isNotEmpty() && (password == confirmPassword))
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    repository.createUser(email, password).collect {  }

                } catch (e: Exception) {

                }
            }
    }

    fun launchRegistrationScree(){
        viewModelScope.launch(Dispatchers.Main) {
            _authFlowState.emit(AuthState.OpenRegistration())
        }
    }

    fun signIn(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    repository.login(email, password).collect {  }
                    checkAuthStatus()
                } catch (e: Exception) {

                }

            }

        }
    }

    fun sendVerificationEmail() {
        viewModelScope.launch {
            try {

                    repository.verifyEmail().collect {  }

                val tickerChannel = ticker(delayMillis = 3_000, initialDelayMillis = 0)

                repeat(15) {
                    tickerChannel.receive()
                    checkAuthStatus()
                }


            } catch (e: Exception) {

            }


        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.logout().collect {

            }
            checkAuthStatus()
        }
    }

    @ExperimentalCoroutinesApi
    fun checkAuthStatus() {
        viewModelScope.launch(Dispatchers.Main) {

            repository.authChanged().collect {

                if (it == "verified") {
                    _authFlowState.emit(AuthState.EmailVerified())

                } else if (it == "unverified") {
                    _authFlowState.emit(AuthState.LoggedIn())
                } else {
                    _authFlowState.emit(AuthState.LoggedOut())
                }


            }


        }

    }

    fun createInputDataForUri(imageUri: Uri): Data {
        val builder = Data.Builder()
        imageUri?.let {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())
        }
        return builder.build()
    }


    fun uploadImage (imageUri: Uri){
        val uploadRequest = OneTimeWorkRequestBuilder<UploadImageWorker>()
            .setInputData(createInputDataForUri(imageUri))
            .build()

        workManager.enqueue(uploadRequest)
    }



sealed class AuthState {
    class LoggedIn : AuthState()
    class EmailVerified : AuthState()
    class LoggedOut : AuthState()
    class OpenRegistration: AuthState()

}


}