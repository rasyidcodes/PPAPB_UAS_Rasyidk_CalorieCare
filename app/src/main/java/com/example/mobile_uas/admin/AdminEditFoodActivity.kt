package com.example.mobile_uas.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import com.example.mobile_uas.BottomNavigationActivity
import com.example.mobile_uas.R
import com.example.mobile_uas.databinding.ActivityAdminEditFoodBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminEditFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminEditFoodBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val makananCollection = firestore.collection("makanan")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminEditFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val id = intent.getStringExtra("id")
        val foodName = intent.getStringExtra("foodName")
        val foodCalorie = intent.getIntExtra("foodCalorie",0)

        binding.adminEditEtFoodName.text = Editable.Factory.getInstance().newEditable(foodName.toString())
        binding.adminEditFoodCalorie.text = Editable.Factory.getInstance().newEditable(foodCalorie.toString())


        with(binding){


            adminEditBtSubmit.setOnClickListener{
                adminEditProgressBar.setVisibility(View.VISIBLE)
                var nameMakan = adminEditEtFoodName.text.toString()
                try {

                    val kaloriInt = adminEditFoodCalorie.text.toString().toInt()

                    if (nameMakan.isBlank() || kaloriInt <= 0 ){
                        Toast.makeText(this@AdminEditFoodActivity, "Data tidak valid!", Toast.LENGTH_SHORT).show()
                    } else {


                        id?.let {
                            // Update data in Firestore
                            makananCollection.document(it)
                                .update(
                                    mapOf(
                                        "foodName" to nameMakan,
                                        "foodCalorie" to kaloriInt
                                    )
                                )

                                .addOnSuccessListener {
                                    adminEditProgressBar.setVisibility(View.GONE)
                                    showToast("Data Updated successfully")
                                    val intent = Intent(this@AdminEditFoodActivity, AdminActivity::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener { e ->
                                    adminEditProgressBar.setVisibility(View.GONE)
                                    showToast("Failed to update data: $e")
                                }
                        }
                    }



                }catch (e: NumberFormatException) {
                    // Handle the case where the conversion to Int fails
                    Toast.makeText(this@AdminEditFoodActivity, "Invalid numeric input!", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}