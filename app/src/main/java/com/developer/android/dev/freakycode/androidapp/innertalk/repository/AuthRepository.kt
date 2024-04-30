package com.developer.android.dev.freakycode.androidapp.innertalk.repository

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.developer.android.dev.freakycode.androidapp.innertalk.model.User
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.NetworkResult
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException

class AuthRepository {
    private var auth: FirebaseAuth = Firebase.auth
    private var logOutLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val fireStoreDatabase = FirebaseFirestore.getInstance()

    init {
        if (auth.currentUser != null) {
            logOutLiveData.postValue(false)
        }
    }

    fun registerUser(
        email: String,
        password: String,
        user: User
    ): Flow<NetworkResult<FirebaseUser>> = flow {
        emit(NetworkResult.Loading())

        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            user.id = auth.currentUser!!.uid
            fireStoreDatabase.collection("Users")
                .document(auth.currentUser!!.uid)
                .set(user)
            emit(result.user?.let {
                NetworkResult.Success(it)
            }!!)

        } catch (e: Exception) {
            emit(NetworkResult.Error(e.localizedMessage ?: ""))
        } catch (e: IOException) {
            emit(NetworkResult.Error(e.localizedMessage ?: "Check Your Internet Connection"))
        }
    }


    fun logInUser(email: String, password: String): Flow<NetworkResult<FirebaseUser>> = flow {
        emit(NetworkResult.Loading())

        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            emit(result.user?.let {
                NetworkResult.Success(it)
            }!!)

        } catch (e: Exception) {
            emit(NetworkResult.Error(e.localizedMessage ?: ""))
        } catch (e: IOException) {
            emit(NetworkResult.Error(e.localizedMessage ?: "Check Your Internet Connection"))
        }
    }

    fun readUserData(): Flow<NetworkResult<User>> = flow {
        emit(NetworkResult.Loading())

        if (auth.currentUser != null) {
            try {
                val snapshot = fireStoreDatabase.collection("Users")
                    .document(auth.currentUser!!.uid).get().await()

                if (snapshot.exists()) {
                    val user: User? = snapshot.toObject(User::class.java)
                    emit(NetworkResult.Success(data = user!!))
                }
            } catch (e: IOException) {
                emit(
                    NetworkResult.Error(
                        message = e.localizedMessage ?: "Check Your Internet Connection"
                    )
                )
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.localizedMessage ?: ""))
            }
        }
    }

    fun getAllExperts():Flow<NetworkResult<List<User>>> = flow {
        emit(NetworkResult.Loading())
//            .whereNotEqualTo("userType","Client")
        try {
            val snapshot = fireStoreDatabase.collection("Users")
                .whereNotEqualTo("id",auth.currentUser!!.uid)
                .get()
                .await()

            val userList = mutableListOf<User>()

            for (document in snapshot.documents){
                val user = document.toObject(User::class.java)
                user?.let {
                    userList.add(it)
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

    fun logOutUser() {
        auth.signOut()
        logOutLiveData.postValue(true)
    }
}