package com.developer.android.dev.freakycode.androidapp.innertalk

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.developer.android.dev.freakycode.androidapp.innertalk.adapter.ExpertsAdapter
import com.developer.android.dev.freakycode.androidapp.innertalk.adapter.HomeAdapter
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.FragmentHomeBinding
import com.developer.android.dev.freakycode.androidapp.innertalk.model.User
import com.developer.android.dev.freakycode.androidapp.innertalk.viewmodel.ChatViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding

    private lateinit var homeAdapter : HomeAdapter
    private var messageList:ArrayList<User>?=null
    private val chatViewmodel by viewModels<ChatViewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        messageList = ArrayList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(messageList!!.isEmpty()){
            binding.noMessage.isVisible = true
        }

        chatViewmodel.getAllChatsUser()
        bindObserver()
        bindRecyclerView()
        onClickUser()
    }

    private fun onClickUser() {
        homeAdapter.onItemClick={
            val intent = Intent(requireContext(),MessageActivity::class.java)
            intent.putExtra("id",it.id)
            intent.putExtra("name",it.name)
            startActivity(intent)
        }
    }

    private fun bindRecyclerView() {
        binding.messageHistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        homeAdapter= HomeAdapter(messageList!!)
        binding.messageHistoryRecyclerView.adapter = homeAdapter
    }

    private fun bindObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            chatViewmodel.allChatUser.collect{
                binding.progressBar.isVisible = false
                if (it.isLoading) {
                    binding.progressBar.isVisible = true
                }

                if (it.error!!.isNotBlank()) {
                    binding.progressBar.isVisible = false
                }

                it.data?.let { data ->
                    (messageList as ArrayList<User>).addAll(data)
                    homeAdapter.notifyDataSetChanged()
                }
            }
        }
    }

}