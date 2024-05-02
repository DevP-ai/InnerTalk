package com.developer.android.dev.freakycode.androidapp.innertalk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.developer.android.dev.freakycode.androidapp.innertalk.R
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.ReceiverItemLayoutBinding
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.SenderItemLayoutBinding
import com.developer.android.dev.freakycode.androidapp.innertalk.model.Chat
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class ChatAdapter(private var list:ArrayList<Chat>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var ITEM_SENT=0
    var ITEM_REVEIVED=1

    inner class SendViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding:SenderItemLayoutBinding=SenderItemLayoutBinding.bind(view)
    }

    inner class ReceivedViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding:ReceiverItemLayoutBinding=ReceiverItemLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  if(viewType==ITEM_SENT){
            SendViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.sender_item_layout,parent,false)
            )
        }else{
            ReceivedViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.receiver_item_layout,parent,false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(FirebaseAuth.getInstance().currentUser!!.uid==list[position].senderId) ITEM_SENT else ITEM_REVEIVED
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message=list[position]
        if(holder.itemViewType==ITEM_SENT){
            var viewHolder=holder as SendViewHolder
            viewHolder.binding.sentMessage.text=message.message
        }else{
            var viewHolder=holder as ReceivedViewHolder
            viewHolder.binding.receiveMessage.text=message.message
        }
    }


}