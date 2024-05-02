package com.developer.android.dev.freakycode.androidapp.innertalk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.UserListBinding
import com.developer.android.dev.freakycode.androidapp.innertalk.model.User

class HomeAdapter(
    private val userList: List<User>
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    lateinit var onItemClick :((User)->Unit?)
    inner class HomeViewHolder(val binding: UserListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            UserListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.binding.profileName.text = userList[position].name

        holder.itemView.setOnClickListener {
            onItemClick.invoke(userList[position])
        }
    }
}