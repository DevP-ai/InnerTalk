package com.developer.android.dev.freakycode.androidapp.innertalk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.FragmentChatBinding
import com.developer.android.dev.freakycode.androidapp.innertalk.model.Chat
import com.google.firebase.auth.FirebaseAuth

class ChatFragment : Fragment() {
    private lateinit var binding:FragmentChatBinding

    private var senderId:String?=""
    private var receiverId:String?=""
    private var receiverName:String?=""

    private lateinit var charList:ArrayList<Chat>

    private lateinit var senderRoom:String
    private lateinit var receiverRoom:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        senderId = FirebaseAuth.getInstance().currentUser!!.uid
        receiverId = requireActivity().intent.getStringExtra("id")
        receiverName = requireActivity().intent.getStringExtra("name")

        senderRoom=senderId+receiverId
        receiverRoom=receiverId+senderId

        charList = ArrayList()

        if(charList.isEmpty()){
            binding.noMessage.isVisible = true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.particularUserName.text = receiverName.toString()
    }
}