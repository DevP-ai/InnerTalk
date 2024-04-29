package com.developer.android.dev.freakycode.androidapp.innertalk.utils

import com.developer.android.dev.freakycode.androidapp.innertalk.model.User

data class UserState(
    val data: User?=null,
    val error:String?="",
    val isLoading:Boolean = false
)
