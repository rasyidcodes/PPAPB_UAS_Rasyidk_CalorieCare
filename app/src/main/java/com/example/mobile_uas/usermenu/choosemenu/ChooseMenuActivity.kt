package com.example.mobile_uas.usermenu.choosemenu

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobile_uas.BottomNavigationActivity
import com.example.mobile_uas.R
import com.example.mobile_uas.admin.AdminAddFoodActivity
import com.example.mobile_uas.admin.AdminDataAdapter
import com.example.mobile_uas.auth.AuthActivity
import com.example.mobile_uas.data.model.firestore.MenuAdminFS
import com.example.mobile_uas.databinding.ActivityAdminBinding
import com.example.mobile_uas.databinding.ActivityChooseMenuBinding
import com.example.mobile_uas.usermenu.addmenu.AddFoodActivity
import com.example.mobile_uas.util.SharedPreferencesHelper
import com.google.firebase.firestore.FirebaseFirestore

class ChooseMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseMenuBinding
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var admindataAdapter: ChooseMenuAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(this@ChooseMenuActivity)

        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        admindataAdapter = ChooseMenuAdapter()
        recyclerView.adapter = admindataAdapter
        fetchDataAndObserve()
        //To custom

        binding.chooseToCustom.setOnClickListener{
            val toMainActivity = Intent(this@ChooseMenuActivity, AddFoodActivity::class.java)
            startActivity(toMainActivity)
        }

        // Set up search functionality
        val searchEditText = binding.sgnPassword
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Call fetchDataAndObserve with the search query
                fetchDataAndObserve(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }
        })
    }

    private fun fetchDataAndObserve(searchQuery: String = "") {
        try {
            val bukuCollection = firestore.collection("makanan")

            // Add a condition to filter data based on the search query
            val query = if (searchQuery.isNotEmpty()) {

                bukuCollection.whereGreaterThanOrEqualTo("foodName", searchQuery)
                    .whereLessThanOrEqualTo("foodName", searchQuery + "\uf8ff")
            } else {
                bukuCollection
            }

            // Observe Firestore changes
            query.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    showToast(this@ChooseMenuActivity, "Error fetching data from Firestore")
                    return@addSnapshotListener
                }

                snapshot?.let { documents ->
                    val bukus = mutableListOf<MenuAdminFS>()
                    for (document in documents) {
                        val bukuId = document.id
                        val buku = document.toObject(MenuAdminFS::class.java).copy(id = bukuId)
                        bukus.add(buku)
                    }

                    // Update the UI with the Firestore data
                    admindataAdapter.setMakanan(bukus, searchQuery)
                }
            }
        } catch (e: Exception) {
            showToast(this@ChooseMenuActivity, e.toString())
            Log.d("ERRORKU", e.toString())
        }
    }

    private fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }
}
