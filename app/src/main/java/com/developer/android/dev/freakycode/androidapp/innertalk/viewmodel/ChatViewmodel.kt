package com.developer.android.dev.freakycode.androidapp.innertalk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.android.dev.freakycode.androidapp.innertalk.model.Chat
import com.developer.android.dev.freakycode.androidapp.innertalk.repository.ChatRepository
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.AllUserState
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.GetMessageState
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.SendMessageState
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

    private val _saveChatData = MutableStateFlow(SendMessageState())
    val saveChatData:StateFlow<SendMessageState> = _saveChatData

    private val _getChatData = MutableStateFlow(GetMessageState())
    val getChatData:StateFlow<GetMessageState> = _getChatData

    private val _allChatUser= MutableStateFlow(AllUserState())
    val allChatUser:StateFlow<AllUserState> = _allChatUser

   fun sendMessage(senderId:String,receiverId:String,chat: Chat){
       chatRepository.sendMessage(senderId=senderId,receiverId=receiverId,chat=chat).onEach {
           when(it){
               is NetworkResult.Loading ->{
                   _saveChatData.value = SendMessageState(isLoading = true)
               }
               is NetworkResult.Error ->{
                   _saveChatData.value = SendMessageState(error = it.message?:"")
               }
               is NetworkResult.Success ->{
                   _saveChatData.value = SendMessageState(data = it.data)
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

    fun getAllChatsUser(){
        chatRepository.getAllChatsUser().onEach {
            when(it){
                is NetworkResult.Loading ->{
                    _allChatUser.value = AllUserState(isLoading = true)
                }
                is NetworkResult.Error ->{
                    _allChatUser.value = AllUserState(error= it.message?:"")
                }

                is NetworkResult.Success ->{
                    _allChatUser.value = AllUserState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }
}