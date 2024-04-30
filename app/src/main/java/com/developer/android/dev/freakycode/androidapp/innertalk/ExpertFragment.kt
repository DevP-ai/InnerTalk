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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.developer.android.dev.freakycode.androidapp.innertalk.adapter.ExpertsAdapter
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.FragmentExpertBinding
import com.developer.android.dev.freakycode.androidapp.innertalk.model.User
import com.developer.android.dev.freakycode.androidapp.innertalk.viewmodel.AuthViewmodel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ExpertFragment : Fragment() {
    private lateinit var binding:FragmentExpertBinding
    private val authViewmodel by viewModels<AuthViewmodel>()
    private lateinit var expertsAdapter : ExpertsAdapter
    private var expertsList:ArrayList<User>?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpertBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        expertsList = ArrayList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewmodel.getAllExperts()

        bindObserver()
        bindRecyclerView()
        onClickUser()

    }

    private fun onClickUser() {
        expertsAdapter.onItemClick={
            val intent = Intent(requireContext(),MessageActivity::class.java)
            intent.putExtra("id",it.id)
            intent.putExtra("name",it.name)
            startActivity(intent)
        }
    }

    private fun bindRecyclerView() {
        binding.searchUserRecyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        expertsAdapter = ExpertsAdapter(expertsList!!)
        binding.searchUserRecyclerView.adapter = expertsAdapter
    }

    private fun bindObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            authViewmodel.expertData.collect {
                binding.progressBar.isVisible = false
                if (it.isLoading) {
                    binding.progressBar.isVisible = true
                }

                if (it.error!!.isNotBlank()) {
                    binding.progressBar.isVisible = false
                }

                it.data?.let { data ->
                    (expertsList as ArrayList<User>).addAll(data)
                    expertsAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        viewLifecycleOwner.lifecycleScope.launch {
//
//        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}