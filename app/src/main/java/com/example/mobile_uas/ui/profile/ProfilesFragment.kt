package com.example.mobile_uas.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mobile_uas.BottomNavigationActivity
import com.example.mobile_uas.auth.AuthActivity
import com.example.mobile_uas.databinding.FragmentProfilesBinding
import com.example.mobile_uas.util.SharedPreferencesHelper

class ProfilesFragment : Fragment() {



    private lateinit var binding: FragmentProfilesBinding
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilesBinding.inflate(inflater, container, false)
        val view = binding.root


        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(requireContext())

        binding.frProfileTvName.text = sharedPreferencesHelper.getUserName()
        binding.frProfileTvHeight.text = "165"
        binding.frProfileTvWeight.text = sharedPreferencesHelper.getUserWeight().toString()
        binding.frProfileTvTargetweight.text = sharedPreferencesHelper.getUserTargetWeight().toString()

        binding.proRlKeluar.setOnClickListener{
            sharedPreferencesHelper.setLoggedIn(false)
            val toMainActivity = Intent(requireContext(), AuthActivity::class.java)
            startActivity(toMainActivity)

        }

        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}