package com.developer.android.dev.freakycode.androidapp.innertalk.di

import com.developer.android.dev.freakycode.androidapp.innertalk.repository.AuthRepository
import com.developer.android.dev.freakycode.androidapp.innertalk.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule{
    @Provides
    @Singleton
    fun provideAuthRepository()= AuthRepository()
    @Provides
    @Singleton
    fun provideChatRepository()= ChatRepository()
}