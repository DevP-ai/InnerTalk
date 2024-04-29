package com.developer.android.dev.freakycode.androidapp.innertalk

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.FragmentProfileBinding
import com.developer.android.dev.freakycode.androidapp.innertalk.viewmodel.AuthViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private val authViewmodel by viewModels<AuthViewmodel>()
//    private var userDataJob: Job? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewmodel.getUserData()

        bindObserver()

        binding.btnLogout.setOnClickListener {
            showDialog()
        }

    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_logout_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)
        val btnLogout = dialog.findViewById<Button>(R.id.btn_logout_confirm)

        btnLogout.setOnClickListener {
            authViewmodel.logOutUser()
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun bindObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            authViewmodel.userData.collect {
                binding.progressBar.isVisible = false
                if (it.isLoading) {
                    binding.progressBar.isVisible = true
                }

                if (it.error!!.isNotBlank()) {
                    binding.progressBar.isVisible = false
                    binding.txtError.text = it.error.toString()
                }

                it.data?.let { data ->
                    binding.profileName.text = data.name
                    binding.profileEmail.text = data.email
                    binding.profileType.text = data.userType
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
//       viewLifecycleOwner.lifecycleScope.launch {
//
//        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}