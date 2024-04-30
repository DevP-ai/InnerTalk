package com.developer.android.dev.freakycode.androidapp.innertalk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.developer.android.dev.freakycode.androidapp.innertalk.adapter.ChatAdapter
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.FragmentChatBinding
import com.developer.android.dev.freakycode.androidapp.innertalk.model.Chat
import com.developer.android.dev.freakycode.androidapp.innertalk.viewmodel.ChatViewmodel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private lateinit var binding:FragmentChatBinding

    private val chatViewmodel by viewModels<ChatViewmodel>()
    private lateinit var chatAdapter: ChatAdapter
    private var senderId:String?=""
    private var receiverId:String?=""
    private var receiverName:String?=""

    private lateinit var chatList:ArrayList<Chat>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        senderId = FirebaseAuth.getInstance().currentUser!!.uid
        receiverId = requireActivity().intent.getStringExtra("id")
        receiverName = requireActivity().intent.getStringExtra("name")


        chatList = ArrayList()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatViewmodel.getMessage(senderId!!,receiverId!!)

        bindObserver()
        bindSetObserver()

        binding.btnSend.setOnClickListener {
            if(binding.edtMessage.text.toString().isNotBlank()){
                sendMessage()
            }
        }

        setMessage()
        binding.particularUserName.text = receiverName.toString()
    }

    private fun bindSetObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            chatViewmodel.getChatData.collect{
                chatViewmodel.saveChatData.collect{
                    if(it.isLoading){

                    }
                    if(it.error!!.isNotBlank()){

                    }

                    it.data?.let {msg->
                        chatList.add(msg)
                         chatAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun bindObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            chatViewmodel.saveChatData.collect{
                if(it.isLoading){

                }
                if(it.error!!.isNotBlank()){

                }

                it.data?.let {
                    binding.edtMessage.setText("")
                }
            }
        }
    }

    private fun setMessage() {
        val layout = LinearLayoutManager(requireContext())
        binding.messageRecyclerView.layoutManager = layout
        if(chatList.isEmpty()){
            binding.noMessage.isVisible = true
        }
        chatAdapter = ChatAdapter(chatList)
        binding.messageRecyclerView.adapter = chatAdapter
    }

    private fun sendMessage() {
        val chat =Chat(
            senderId = senderId,
            receiverId = receiverId,
            message = binding.edtMessage.text.toString()
        )

        chatViewmodel.sendMessage(senderId!!,receiverId!!,chat)
    }
}