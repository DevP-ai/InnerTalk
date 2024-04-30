package com.developer.android.dev.freakycode.androidapp.innertalk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.UserDesignLayoutBinding
import com.developer.android.dev.freakycode.androidapp.innertalk.model.User

class ExpertsAdapter(
    private val expertList: ArrayList<User>
) : RecyclerView.Adapter<ExpertsAdapter.ExpertsViewHolder>() {

    var onItemClick :((User)->Unit?)?=null
    inner class ExpertsViewHolder(val binding: UserDesignLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpertsViewHolder {
        return ExpertsViewHolder(
            UserDesignLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int {
        return expertList.size
    }

    override fun onBindViewHolder(holder: ExpertsViewHolder, position: Int) {
        val data = expertList[position]
        holder.binding.userProfileName.text = data.name
        holder.binding.userProfileType.text = data.userType

        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(expertList[position])
        }
    }
}