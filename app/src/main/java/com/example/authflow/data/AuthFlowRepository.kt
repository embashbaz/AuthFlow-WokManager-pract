package com.example.authflow.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthFlowRepository @Inject constructor(private val mFirebaseAuth: FirebaseAuth) {

    var TAG = "Repository"


    suspend fun createUser(email: String, password: String): Flow<Boolean> = flow {
        try {
            mFirebaseAuth.createUserWithEmailAndPassword(email, password).await().user

            emit(true)
        } catch (e: Exception) {
            emit(false)
        }

    }

    suspend fun verifyEmail(): Flow<Boolean> = flow {

        try {
            mFirebaseAuth.currentUser!!.sendEmailVerification().await()
            //mFirebaseAuth.signOut()
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }

    }


    suspend fun login(email: String, password: String): Flow<Boolean> = flow {
        try {
            mFirebaseAuth.signInWithEmailAndPassword(email, password).await().user!!
            emit(true)
        }catch (e: Exception){
            emit(false)
        }


    }

    suspend fun logout(): Flow<Boolean> = flow {
        try {
           // Firebase.auth.signOut()
               mFirebaseAuth.signOut()
            Log.d("TOKEN", mFirebaseAuth.currentUser?.uid ?: "User is null")
            emit(true)
        } catch (e: Exception) {

            Log.d("Error: ", e.toString())
            emit(false)
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun authChanged(): Flow<String> = callbackFlow {
        //val listener =

       mFirebaseAuth.addAuthStateListener {

           if (it.currentUser != null){
               it.currentUser!!.reload()
                if (it.currentUser!!.isEmailVerified){
                    trySend("verified")
                }else{
                    trySend("unverified")
                }
               Log.d("REPO: USER: ", it.currentUser!!.uid)
           }else{
               trySend("logged out")
           }

       }
        awaitClose {
            cancel()
        }



    }




}