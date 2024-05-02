package com.developer.android.dev.freakycode.androidapp.innertalk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.android.dev.freakycode.androidapp.innertalk.adapter.ChatAdapter
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.FragmentChatBinding
import com.developer.android.dev.freakycode.androidapp.innertalk.model.Chat
import com.developer.android.dev.freakycode.androidapp.innertalk.viewmodel.ChatViewmodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding

    private var db = FirebaseDatabase.getInstance("https://innertalk-therapy-default-rtdb.asia-southeast1.firebasedatabase.app/")


//    private val chatViewmodel by viewModels<ChatViewmodel>()
    private lateinit var chatAdapter: ChatAdapter
    private var senderId: String? = ""
    private var receiverId: String? = ""
    private var receiverName: String? = ""

    private lateinit var chatList: ArrayList<Chat>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        senderId = FirebaseAuth.getInstance().currentUser!!.uid
        receiverId = requireActivity().intent.getStringExtra("id")
        receiverName = requireActivity().intent.getStringExtra("name")


//        chatList = ArrayList()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        bindSetObserver()
//        bindObserver()
        chatList = ArrayList()



        binding.btnSend.setOnClickListener {
            if (binding.edtMessage.text.toString().isNotBlank()) {
//                sendMessage()
                saveMessage()
            }
        }

        showMessage()
        chatAdapter =ChatAdapter(chatList)
//        setMessage()
        binding.particularUserName.text = receiverName.toString()
    }

    private fun showMessage() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.messageRecyclerView.layoutManager = layoutManager

        db.reference.child("Chats")
            .child(senderId!!)
            .child(receiverId!!)
            .child("message")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatList.clear()
                    if (snapshot.exists()) {
                        for (snap in snapshot.children) {
                            val data = snap.getValue(Chat::class.java)
                            chatList.add(data!!)
                        }
                    }
                    binding.messageRecyclerView.adapter = ChatAdapter(chatList)

                    layoutManager.scrollToPositionWithOffset(chatList.size - 1, 0)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    private fun saveMessage() {
        val message =Chat(senderId,receiverId,message = binding.edtMessage.text.toString())
        val randomKey = db.reference.push().key

        db.reference.child("Chats")
            .child(senderId!!)
            .child(receiverId!!)
            .child("message")
            .child(randomKey!!)
            .setValue(message)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.edtMessage.setText("")
                    db.reference.child("Chats")
                        .child(receiverId!!)
                        .child(senderId!!)
                        .child("message")
                        .child(randomKey)
                        .setValue(message)
                        .addOnSuccessListener {
                        }
                }
            }
    }




//    private fun bindSetObserver() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            chatViewmodel.getChatData.collect {
//                if (it.isLoading) {
//
//                }
//                if (it.error!!.isNotBlank()) {
//                    Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
//                }
//
//                it.data?.let { msg ->
//                    chatList.clear()
//                    chatList.addAll(msg)
//                    chatAdapter.notifyDataSetChanged()
//                }
//            }
//        }
//    }
//
//    private fun bindObserver() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            chatViewmodel.saveChatData.collect {
//                if (it.isLoading) {
//
//                }
//                if (it.error!!.isNotBlank()) {
//
//                }
//
//                it.data?.let {
//                    binding.edtMessage.setText("")
//                    chatList.addAll(listOf(it))
//                    chatAdapter.notifyDataSetChanged()
//                }
//            }
//        }
//    }
//
//    private fun setMessage() {
//        senderId?.let {
//            receiverId?.let { it1 ->
//                chatViewmodel.getMessage(it, it1)
//                chatViewmodel.getMessage(it1, it)
//            }
//        }
//        val layout = LinearLayoutManager(requireContext())
//        chatAdapter = ChatAdapter(chatList)
//        binding.messageRecyclerView.apply {
//            this.layoutManager = layout
//            adapter = chatAdapter
//        }
//    }
//
//    private fun sendMessage() {
//        val chat = Chat(
//            senderId = senderId,
//            receiverId = receiverId,
//            message = binding.edtMessage.text.toString()
//        )
//
//        chatViewmodel.sendMessage(senderId!!, receiverId!!, chat)
//    }

}