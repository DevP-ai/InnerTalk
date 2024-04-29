package com.developer.android.dev.freakycode.androidapp.innertalk.utils

import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.developer.android.dev.freakycode.androidapp.innertalk.model.User
import com.developer.android.dev.freakycode.androidapp.innertalk.viewmodel.AuthViewmodel
import kotlinx.coroutines.launch

object AuthUtils {

    fun registerUser(
        authViewmodel: AuthViewmodel,
        email:String,
        password:String,
        user: User
    ){
        authViewmodel.registerUser(email,password,user)
    }

    fun bindObserver(
        authViewmodel: AuthViewmodel,
        viewLifecycleOwner: LifecycleOwner,
        progressBar: ProgressBar,
        onSuccess: () ->Unit
    ){
        viewLifecycleOwner.lifecycleScope.launch {
            authViewmodel.authUser.collect{
                if (it.isLoading) {
                    progressBar.isVisible = true
                }
                if (it.error.isNotBlank()) {
                    progressBar.isVisible = false
                }
                it.data?.let { data ->
                    progressBar.isVisible = false
                    onSuccess.invoke()
                }
            }
        }
    }
}