package com.example.mobile_uas.auth
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

import com.example.mobile_uas.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Set up your UI components using View Binding
        binding.loginBtLogin.setOnClickListener {
            val email = binding.loginEtEmail.text.toString().trim()
            val password = binding.loginEtPassword.text.toString().trim()

            // Validate email and password (add your own validation logic)
            if (email.isEmpty() || password.isEmpty()) {
                showToast("Please fill in all fields.")
                return@setOnClickListener
            }

            // Sign in user with email and password
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    try {
                        if (task.isSuccessful) {
                            // Login success
                            val user = auth.currentUser
                            // Store the user ID (you can use ViewModel, SharedPreferences, etc.)
                            val userId = user?.uid
                            showToast("Login successful!")
                            // TODO: Navigate to the home fragment or activity
                        } else {
                            // If login fails, display a message to the user.
                            throw task.exception ?: Exception("Unknown error occurred.")
                        }
                    } catch (e: Exception) {
                        showToast("Login failed: ${e.message}")
                    }
                }
        }

        binding.loginBtToSignup.setOnClickListener {
            // TODO: Navigate to the signup fragment or activity
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
