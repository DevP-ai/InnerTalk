package com.developer.android.dev.freakycode.androidapp.innertalk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.android.dev.freakycode.androidapp.innertalk.model.Chat
import com.developer.android.dev.freakycode.androidapp.innertalk.repository.ChatRepository
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.ChatState
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChatViewmodel @Inject constructor(
    private val chatRepository: ChatRepository
):ViewModel(){

    private val _chatData = MutableStateFlow(ChatState())
    val chatData:StateFlow<ChatState> = _chatData

   fun sendMessage(senderId:String,receiverId:String,chat: Chat){
       chatRepository.sendMessage(senderId=senderId,receiverId=receiverId,chat=chat).onEach {
           when(it){
               is NetworkResult.Loading ->{
                   _chatData.value = ChatState(isLoading = true)
               }
               is NetworkResult.Error ->{
                   _chatData.value = ChatState(error = it.message?:"")
               }
               is NetworkResult.Success ->{
                   _chatData.value = ChatState(data = it.data)
               }
           }
       }.launchIn(viewModelScope)
   }

}