package com.developer.android.dev.freakycode.androidapp.innertalk.repository

import com.developer.android.dev.freakycode.androidapp.innertalk.model.Chat
import com.developer.android.dev.freakycode.androidapp.innertalk.model.User
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.NetworkResult
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import java.io.IOException


class ChatRepository {
    private var db =
        FirebaseDatabase.getInstance("https://innertalk-therapy-default-rtdb.asia-southeast1.firebasedatabase.app/")

    private val fireStoreDatabase = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth = Firebase.auth
    fun sendMessage(
        senderId: String,
        receiverId: String,
        chat: Chat
    ): Flow<NetworkResult<Chat>> = flow {
        emit(NetworkResult.Loading())

        try {
            val randomKey =
                db.reference.push().key ?: throw Exception("Failed to generate message ID.")

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

        } catch (e: Exception) {
            emit(NetworkResult.Error(e.localizedMessage ?: ""))
        } catch (e: IOException) {
            emit(NetworkResult.Error(e.localizedMessage ?: "Check Your Internet Connection"))
        }

    }

    fun getAllChatsUser():Flow<NetworkResult<List<User>>> = flow {
        emit(NetworkResult.Loading())

        try {
            val refUserId = db.reference.child("Chats").get().await()

            val userList = mutableListOf<User>()

            if(refUserId.exists()){
                for (snap in refUserId.children){
                    if(snap.hasChild(auth.currentUser!!.uid)){
                        val otherUserId=snap.key
                        if(otherUserId!=null){
                            val snapshot = fireStoreDatabase.collection("Users")
                                .document(otherUserId).get().await()

                            if(snapshot.exists()){
                                val otherUser = snapshot.toObject(User::class.java)
                                otherUser?.let {
                                    userList.add(it)
                                }
                            }

                        }
                    }
                }
            }
            emit(NetworkResult.Success(data = userList.toList()))

        }catch (e: IOException) {
            emit(
                NetworkResult.Error(
                    message = e.localizedMessage ?: "Check Your Internet Connection"
                )
            )
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.localizedMessage ?: ""))
        }
    }

    fun getMessage(
        senderId: String,
        receiverId: String
    ): Flow<NetworkResult<List<Chat>>> = flow {
        emit(NetworkResult.Loading())
        try {
            val chatList = mutableListOf<Chat>()
            val snapShot=db.reference.child("Chats")
                .child(senderId)
                .child(receiverId)
                .child("message")
                .get().await()

            for (snap in snapShot.children) {
                val chat = snap.getValue(Chat::class.java)
                chatList.add(chat!!)
            }

            emit(NetworkResult.Success(chatList))

        } catch (e: Exception) {
            emit(NetworkResult.Error(e.localizedMessage ?: ""))
        } catch (e: IOException) {
            emit(NetworkResult.Error(e.localizedMessage ?: "Check Your Internet Connection"))
        }
    }
}