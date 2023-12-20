package com.example.mobile_uas.auth

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mobile_uas.util.SharedPreferencesHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val sharedPreferencesHelper =
        SharedPreferencesHelper.getInstance(application.applicationContext)

    private var firestore = FirebaseFirestore.getInstance()
    private val _userRole = MutableLiveData<Int?>()

    val userRole: MutableLiveData<Int?>
        get() = _userRole
    fun fetchUserRoleFromFirestore(onResult: (Int) -> Unit) {
        val userId = auth.currentUser?.uid

        userId?.let { uid ->
            firestore.collection("users")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val tipe = document.getLong("tipe")

                        // Display the retrieved role using a toast message
                        if (tipe != null) {
                            onResult(tipe.toInt())
                        }

                    }
                }
                .addOnFailureListener {
                }
        }
    }

    fun saveDataFromFirestore(){
        val userId = auth.currentUser?.uid

        userId?.let { uid ->
            firestore.collection("users")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val tipe = document.getLong("tipe")
                        sharedPreferencesHelper.saveUserName(document.getString("name").toString())
                        sharedPreferencesHelper.saveUserId(document.getString("userId").toString())
                        document.getLong("tipe")
                            ?.let { sharedPreferencesHelper.saveUserTipe(it.toInt()) }

                        document.getLong("height")
                            ?.let { sharedPreferencesHelper.saveUserHeight(it.toInt()) }

                        document.getLong("weight")
                            ?.let { sharedPreferencesHelper.saveUserWeight(it.toInt()) }

                        document.getLong("target_weight")
                            ?.let { sharedPreferencesHelper.saveUserTargetWeight(it.toInt()) }

                        document.getLong("calorie")
                            ?.let { sharedPreferencesHelper.saveUserCalorie(it.toInt()) }
                        // Display the retrieved role using a toast message


                    }
                }
                .addOnFailureListener {
                }
        }
    }

    fun loginUser(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveDataFromFirestore()
                        onResult(true, "Login berhasil")
                        sharedPreferencesHelper.setLoggedIn(true)
                        sharedPreferencesHelper.saveUserId(getUserId())
                    } else {
                        onResult(false, "Login gagal: ${task.exception?.message ?: "Terjadi kesalahan"}")
                    }
                }
        } catch (e: Exception) {
            onResult(false, "Terjadi kesalahan: ${e.message ?: "Unknown error"}")
        }
    }


    fun getUserId(): String {
        return auth.currentUser?.uid ?: ""
    }

    private fun showToast(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }
}