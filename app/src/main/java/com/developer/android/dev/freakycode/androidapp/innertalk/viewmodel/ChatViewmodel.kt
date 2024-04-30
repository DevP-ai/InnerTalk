package com.developer.android.dev.freakycode.androidapp.innertalk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.android.dev.freakycode.androidapp.innertalk.model.Chat
import com.developer.android.dev.freakycode.androidapp.innertalk.repository.ChatRepository
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.GetMessageState
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.SetMessageState
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

    private val _setChatData = MutableStateFlow(SetMessageState())
    val setChatData:StateFlow<SetMessageState> = _setChatData

    private val _getChatData = MutableStateFlow(GetMessageState())
    val getChatData:StateFlow<GetMessageState> = _getChatData

   fun sendMessage(senderId:String,receiverId:String,chat: Chat){
       chatRepository.sendMessage(senderId=senderId,receiverId=receiverId,chat=chat).onEach {
           when(it){
               is NetworkResult.Loading ->{
                   _setChatData.value = SetMessageState(isLoading = true)
               }
               is NetworkResult.Error ->{
                   _setChatData.value = SetMessageState(error = it.message?:"")
               }
               is NetworkResult.Success ->{
                   _setChatData.value = SetMessageState(data = it.data)
               }
           }
       }.launchIn(viewModelScope)
   }

    fun getMessage(senderId:String,receiverId:String){
        chatRepository.getMessage(senderId=senderId,receiverId=receiverId).onEach {
            when(it){
                is NetworkResult.Loading ->{
                    _getChatData.value = GetMessageState(isLoading = true)
                }
                is NetworkResult.Error ->{
                    _getChatData.value = GetMessageState(error = it.message?:"")
                }
                is NetworkResult.Success ->{
                    _getChatData.value = GetMessageState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

}