package com.developer.android.dev.freakycode.androidapp.innertalk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.ReceiverItemLayoutBinding
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.SenderItemLayoutBinding
import com.developer.android.dev.freakycode.androidapp.innertalk.model.Chat
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(private var list:ArrayList<Chat>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var ITEM_SENT=0
    var ITEM_REVEIVED=1

    inner class SendViewHolder(val senderBinding:SenderItemLayoutBinding):RecyclerView.ViewHolder(senderBinding.root)
    inner class ReceiverViewHolder(val receiverBinding:ReceiverItemLayoutBinding):RecyclerView.ViewHolder(receiverBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  if(viewType == ITEM_SENT){
            SendViewHolder(SenderItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }else{
            ReceiverViewHolder(ReceiverItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(FirebaseAuth.getInstance().currentUser!!.uid==list[position].senderId) ITEM_SENT else ITEM_REVEIVED
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = list[position]
        if (holder.itemViewType == ITEM_SENT){
            val senderViewHolder = holder as SendViewHolder
            senderViewHolder.senderBinding.sentMessage.text = message.message
        }else{
            val receiverViewHolder = holder as ReceiverViewHolder
            receiverViewHolder.receiverBinding.receiveMessage.text = message.message
        }
    }


}