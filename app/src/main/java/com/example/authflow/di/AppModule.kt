package com.example.authflow.di

import android.app.Application
import androidx.work.WorkManager
import com.example.authflow.data.AuthFlowRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideWorkManager(application: Application) = WorkManager.getInstance(application)

    @Singleton
    @Provides
    fun provideRepository(auth: FirebaseAuth) = AuthFlowRepository(auth)



}