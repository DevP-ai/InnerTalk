package com.developer.android.dev.freakycode.androidapp.innertalk.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.android.dev.freakycode.androidapp.innertalk.model.User
import com.developer.android.dev.freakycode.androidapp.innertalk.repository.AuthRepository
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.AllUserState
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.AuthState
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.NetworkResult
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewmodel @Inject constructor(private val authRepository: AuthRepository) : ViewModel(){
    private val _authUser = MutableStateFlow(AuthState())
    val authUser: StateFlow<AuthState> = _authUser

    private val _userData = MutableStateFlow(UserState())
    val userData: StateFlow<UserState> = _userData

    private val _expertData = MutableStateFlow(AllUserState())
    val expertData: StateFlow<AllUserState> = _expertData


    fun registerUser(email:String,password:String,user: User){
        authRepository.registerUser(email,password,user).onEach {
            when(it){
                is NetworkResult.Loading ->{
                    _authUser.value = AuthState(isLoading = true)
                }
                is NetworkResult.Error ->{
                    _authUser.value = AuthState(error =it.message?:"")
                }
                is NetworkResult.Success ->{
                    _authUser.value = AuthState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun loginUser(email: String,password: String){
        authRepository.logInUser(email,password).onEach {
            when(it){
                is NetworkResult.Loading ->{
                    _authUser.value = AuthState(isLoading = true)
                }
                is NetworkResult.Error ->{
                    _authUser.value = AuthState(error = it.message?:"")
                }
                is NetworkResult.Success ->{
                    _authUser.value = AuthState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getUserData(){
        authRepository.readUserData().onEach {
            when(it){
                is NetworkResult.Loading ->{
                    _userData.value = UserState(isLoading = true)
                }
                is NetworkResult.Error ->{
                    _userData.value = UserState(error = it.message?:"")
                }
                is NetworkResult.Success ->{
                    _userData.value = UserState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getAllExperts(){
        authRepository.getAllExperts().onEach {
            when(it){
                is NetworkResult.Loading->{
                    _expertData.value = AllUserState(isLoading = true)
                }
                is NetworkResult.Error ->{
                    _expertData.value = AllUserState(error = it.message?:"")
                }
                is NetworkResult.Success ->{
                    _expertData.value = AllUserState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }
    fun logOutUser(){
        viewModelScope.launch {
            authRepository.logOutUser()
        }
    }
}