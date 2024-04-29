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
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.FragmentClientRegistrationBinding
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.FragmentTherapistRegistrationBinding
import com.developer.android.dev.freakycode.androidapp.innertalk.model.User
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.AuthUtils
import com.developer.android.dev.freakycode.androidapp.innertalk.viewmodel.AuthViewmodel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClientRegistrationFragment : Fragment() {
    private lateinit var binding: FragmentClientRegistrationBinding

    private val authViewmodel by viewModels<AuthViewmodel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClientRegistrationBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObserver()

        binding.btnSignUp.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {

        val user = User(
            name = binding.txtUsername.text.toString(),
            email = binding.txtEmail.text.toString(),
            userType = "Client"

        )
        AuthUtils.registerUser(
            authViewmodel,
            binding.txtEmail.text.toString(),
            binding.txtPassword.text.toString(),
            user
        )

    }


    private fun bindObserver() {
        AuthUtils.bindObserver(
            authViewmodel,
            viewLifecycleOwner,
            binding.progressBar
        ){
            startActivity(Intent(requireContext(),HostActivity::class.java))
            requireActivity().finish()
        }
    }
}