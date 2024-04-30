package com.developer.android.dev.freakycode.androidapp.innertalk.utils

import com.developer.android.dev.freakycode.androidapp.innertalk.model.Chat

data class GetMessageState(
    val data: List<Chat>? =null,
    val error:String?="",
    val isLoading:Boolean = false
)
