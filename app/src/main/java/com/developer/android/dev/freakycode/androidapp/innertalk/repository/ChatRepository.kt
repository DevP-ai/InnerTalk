package com.developer.android.dev.freakycode.androidapp.innertalk.repository

import com.developer.android.dev.freakycode.androidapp.innertalk.model.Chat
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.NetworkResult
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException


class ChatRepository {
    private var db= FirebaseDatabase.getInstance("https://innertalk-therapy-default-rtdb.asia-southeast1.firebasedatabase.app/")
   fun sendMessage(
       senderId:String,
       receiverId:String,
       chat: Chat
   ): Flow<NetworkResult<Chat>> =flow{
       emit(NetworkResult.Loading())

       try {
           val randomKey= db.reference.push().key?:throw Exception("Failed to generate message ID.")

           db.reference.child("Chats")
               .child(senderId)
               .child(receiverId)
               .child("message")
               .child(randomKey)
               .setValue(chat)
               .addOnCompleteListener {
                   db.reference.child("Chats")
                       .child(receiverId)
                       .child(senderId)
                       .child("message")
                       .setValue(chat)
               }

           emit(NetworkResult.Success(chat))

       }catch (e: Exception) {
           emit(NetworkResult.Error(e.localizedMessage ?: ""))
       } catch (e: IOException) {
           emit(NetworkResult.Error(e.localizedMessage ?: "Check Your Internet Connection"))
       }
   }
}