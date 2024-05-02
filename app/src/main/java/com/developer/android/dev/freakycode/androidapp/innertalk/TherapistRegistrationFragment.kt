package com.developer.android.dev.freakycode.androidapp.innertalk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.FragmentTherapistRegistrationBinding
import com.developer.android.dev.freakycode.androidapp.innertalk.model.User
import com.developer.android.dev.freakycode.androidapp.innertalk.utils.AuthUtils
import com.developer.android.dev.freakycode.androidapp.innertalk.viewmodel.AuthViewmodel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TherapistRegistrationFragment : Fragment() {
    private var _binding: FragmentTherapistRegistrationBinding?=null
    private val binding get() = _binding!!

    private val authViewmodel by viewModels<AuthViewmodel>()
    private val auth=FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTherapistRegistrationBinding.inflate(layoutInflater)
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
            userType = binding.expertise.text.toString()

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
            startActivity(Intent(requireActivity(),NavHostActivity::class.java))
            requireActivity().finish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}