package com.developer.android.dev.freakycode.androidapp.innertalk.repository

import com.developer.android.dev.freakycode.androidapp.innertalk.model.Chat
import com.developer.android.dev.freakycode.androidapp.innertalk.model.User
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.NetworkResult
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
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
            val randomKey = db.reference.push().key!!

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
                        .child(randomKey)
                        .setValue(chat)
                }

            emit(NetworkResult.Success(chat))

        } catch (e: Exception) {
            emit(NetworkResult.Error(e.localizedMessage ?: ""))
        } catch (e: IOException) {
            emit(NetworkResult.Error(e.localizedMessage ?: "Check Your Internet Connection"))
        }

    }

    fun getAllUsers(
        callback:(List<User>?)->Unit
    ){
        val currentUserId=auth.currentUser!!.uid
        val userList = mutableListOf<User>()
        val data =db.reference.child("Chats")

        data.child(currentUserId)
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children){
                        val otherUserId = snap.key

                        if(otherUserId!=null){
                            fireStoreDatabase.collection("Users")
                                .document(otherUserId).get()
                                .addOnSuccessListener {userSnap->
                                    val userData = userSnap.toObject(User::class.java)
                                    userData?.let {
                                        userList.add(it)
                                    }
                                    callback(userList)
                                }
                                .addOnFailureListener {
                                    callback(null)
                                }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun getMessage(
        senderId: String,
        receiverId: String
    ): Flow<NetworkResult<List<Chat>>> = flow {
        emit(NetworkResult.Loading())
        try {
            val chatList = mutableListOf<Chat>()
            val snapShot = db.reference.child("Chats")
                .child(senderId)
                .child(receiverId)
                .child("message")
                .get().await()
            if (snapShot.exists()) {
                for (snap in snapShot.children) {
                    val chat = snap.getValue(Chat::class.java)
                    chatList.add(chat!!)
                }
            }


            emit(NetworkResult.Success(chatList))

        } catch (e: Exception) {
            emit(NetworkResult.Error(e.localizedMessage ?: ""))
        } catch (e: IOException) {
            emit(NetworkResult.Error(e.localizedMessage ?: "Check Your Internet Connection"))
        }
    }
}