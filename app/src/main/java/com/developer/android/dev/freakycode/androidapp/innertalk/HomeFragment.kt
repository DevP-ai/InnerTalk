package com.developer.android.dev.freakycode.androidapp.innertalk

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.android.dev.freakycode.androidapp.innertalk.adapter.HomeAdapter
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.FragmentHomeBinding
import com.developer.android.dev.freakycode.androidapp.innertalk.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var homeAdapter: HomeAdapter

    private var users = ArrayList<User>()

    private val fireStoreDatabase = FirebaseFirestore.getInstance()

    private var db =
        FirebaseDatabase.getInstance("https://innertalk-therapy-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        users = ArrayList()
        retrieveAllUsers()
        bindRecyclerView()


    }

    private fun bindRecyclerView() {
        binding.messageHistoryRecyclerView.setHasFixedSize(true)
        binding.messageHistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        homeAdapter = HomeAdapter(users)
        binding.messageHistoryRecyclerView.adapter = homeAdapter
    }

    private fun retrieveAllUsers() {
        val refUserId = db.getReference("Chats")
        binding.progressBar.isVisible = true
        refUserId.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.progressBar.isVisible = false

                if (snapshot.exists()) {
                    for (snap in snapshot.children) {
                        if (snap.hasChild(auth.currentUser!!.uid)) {
                            val otherUserId = snap.key

                            otherUserId?.let {
                                fireStoreDatabase.collection("Users")
                                    .document(otherUserId).get()
                                    .addOnSuccessListener { userSnapshot ->
                                        if (userSnapshot.exists()) {
                                            val user = userSnapshot.toObject(User::class.java)
                                            user?.let {
                                                users.clear()
                                                users.add(it)
                                            }
                                            homeAdapter.notifyDataSetChanged()
                                            onClickUser()
                                        }
                                    }

                            }

                        }
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progressBar.isVisible = false
            }

        })
    }


//    private fun retrieveAllUsers() {
//        val refUserId = db.getReference("Chats")
//        binding.progressBar.isVisible = true
//        refUserId.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                binding.progressBar.isVisible = false
//                if (snapshot.exists()) {
//                    for (snap in snapshot.children) {
//                        if (snap.hasChild(auth.currentUser!!.uid)) {
//                            val otherUserId = snap.key
//
//                            otherUserId?.let {
//                                binding.progressBar.isVisible = false
//                                fireStoreDatabase.collection("Users")
//                                    .document(otherUserId).get()
//                                    .addOnSuccessListener { userSnapshot ->
//                                        if (userSnapshot.exists()) {
//                                            val user = userSnapshot.toObject(User::class.java)
//                                            user?.let {
//                                                users.add(it)
//                                            }
//                                        }
//                                        homeAdapter.notifyDataSetChanged()
//                                        onClickUser()
//
//                                    }
//                            }
//
//                        }
//                    }
//
//                }
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                binding.progressBar.isVisible = false
//            }
//
//        })
//    }

    private fun onClickUser() {
        homeAdapter.onItemClick = {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("id", it.id)
            intent.putExtra("name", it.name)
            startActivity(intent)
        }
    }

}