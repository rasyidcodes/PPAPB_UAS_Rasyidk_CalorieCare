package com.example.mobile_uas.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.mobile_uas.BottomNavigationActivity
import com.example.mobile_uas.R
import com.example.mobile_uas.data.model.firestore.MenuAdminFS
import com.example.mobile_uas.databinding.ActivityAddFoodBinding
import com.example.mobile_uas.databinding.ActivityAdminAddFoodBinding
import com.example.mobile_uas.util.SharedPreferencesHelper
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class AdminAddFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminAddFoodBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(this@AdminAddFoodActivity)


        with(binding){

            adminAddBtSubmit.setOnClickListener{

                var nameMakan = adminAddEtFoodName.text.toString()
                try {

                    val kaloriInt = adminAddEtFoodCalorie.text.toString().toInt()

                    if (nameMakan.isBlank() || kaloriInt <= 0 ){
                        Toast.makeText(this@AdminAddFoodActivity, "Data tidak valid!", Toast.LENGTH_SHORT).show()
                    } else {
                        adminAddProgressBar.setVisibility(View.VISIBLE);
                        insertToFireStore(nameMakan, kaloriInt)

                    }



                }catch (e: NumberFormatException) {
                    // Handle the case where the conversion to Int fails
                    Toast.makeText(this@AdminAddFoodActivity, "Invalid numeric input!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun insertToFireStore(nameMakan: String, kaloriInt: Int) {
        val db = FirebaseFirestore.getInstance()

        // Create an instance of your data class without setting the ID
        val menuAdminFS = MenuAdminFS(
            foodName = nameMakan,
            foodCalorie = kaloriInt,
            date = getCurrentDateInGMTPlus7(),
            userId = sharedPreferencesHelper.getUserId().toString()
            // No need to set the ID here; it will be set after adding to Firestore
        )

        // Add a new document with a generated ID
        db.collection("makanan")
            .add(menuAdminFS)
            .addOnSuccessListener { documentReference ->
                // Get the generated document ID
                val generatedId = documentReference.id

                binding.adminAddProgressBar.setVisibility(View.GONE);
                // Update the document in Firestore with the new ID
                db.collection("makanan")
                    .document(generatedId)
                    .set(menuAdminFS.copy(id = generatedId))
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@AdminAddFoodActivity,
                            "Data added to Firestore with ID: $generatedId",
                            Toast.LENGTH_SHORT
                        ).show()

                        val toMainActivity = Intent(this@AdminAddFoodActivity, AdminActivity::class.java)
                        startActivity(toMainActivity)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this@AdminAddFoodActivity,
                            "Error updating document with ID $generatedId: $e",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@AdminAddFoodActivity,
                    "Error adding data to Firestore: $e",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun getCurrentDateInGMTPlus7(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormat.timeZone = TimeZone.getTimeZone("GMT+7")
        return dateFormat.format(Date())
    }
}