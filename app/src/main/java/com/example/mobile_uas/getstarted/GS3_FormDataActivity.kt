package com.example.mobile_uas.getstarted

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import com.example.mobile_uas.MainActivity
import com.example.mobile_uas.R
import com.example.mobile_uas.databinding.ActivityGs2SayHiBinding
import com.example.mobile_uas.databinding.ActivityGs3FormDataBinding
import com.google.firebase.firestore.FirebaseFirestore

class GS3_FormDataActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private  lateinit var binding: ActivityGs3FormDataBinding
    var selectedSatuan1 = ""
    var selectedSatuan2 = ""
    val data = listOf("Kg", "Lb")
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGs3FormDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firestore
        firestore = FirebaseFirestore.getInstance()

        //global
        var targetTanggal = ""


        //retrieve intent
        val intent = intent
        var val_name = intent.getStringExtra("val_name")
        var val_uid = intent.getStringExtra("val_uid")
        var val_email = intent.getStringExtra("val_email")

        spinner1()
        spinner2()


        //Tujuan diet
        var selectedTitle=""
        var selectedPosition: Int? = null
        binding.editTextTujuanDiet.setOnClickListener { view ->
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.menuInflater.inflate(R.menu.list_tujuan, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                val newPosition = item.itemId
                if (newPosition != selectedPosition) {
                    Log.d("PositionChange", "Selected Position changed to: $newPosition")
                    selectedPosition = newPosition
                }
                selectedTitle = item.title.toString()
                binding.editTextTujuanDiet.setText(selectedTitle)
                true
            }

            // Set up a default value if no item is selected
            popupMenu.setOnDismissListener {
                if (selectedPosition == null) {
                    Log.d("PositionChange", "No item selected, using default value")
                    // Set a default value or handle the unselected state here
                    binding.editTextTujuanDiet.setText("Default Value")
                    selectedPosition = 0  // Set to the default position (or -1 if you prefer)
                }
            }

            popupMenu.show()
        }

        //getDatepicker
        binding.gs3datePickerTextInputEditText.setOnClickListener{
            val datePicker = DatePicker()
            datePicker.show(supportFragmentManager, "DP")
        }

        binding.gs3btsend.setOnClickListener {

            Log.d("title", selectedTitle)
            Log.d("position", selectedPosition.toString())


            val beratsaatini = binding.gs3beratsaatini.text.toString()
            val beratyangdiinginkan = binding.gs3beratyangdiinginkan.text.toString()
            val jumlahkalori    = binding.gs3jumlahkalori.text.toString()
            targetTanggal = binding.gs3datePickerTextInputEditText.text.toString()


            if (beratsaatini.isFloat() && beratyangdiinginkan.isFloat() && jumlahkalori.isFloat()) {
                if (selectedTitle == "" || targetTanggal == ""){
                    Toast.makeText(this, "Data tidak cocok!", Toast.LENGTH_SHORT).show()
                }else{


                    Log.d("data_name", val_name.toString())
                    Log.d("data_uid", val_uid.toString())
                    Log.d("data_beratsaatini",beratsaatini)
                    Log.d("data_satuanberatsaatini",selectedSatuan1)
                    Log.d("data_beratyangdiinginkan",beratyangdiinginkan)
                    Log.d("data_satuanberatyangdiinginkan",selectedSatuan2)
                    Log.d("data_targetTanggal",targetTanggal)
                    Log.d("data_jumlahkalori",jumlahkalori)
                    addUserDataToFirestore(val_uid, val_name.toString(), beratsaatini.toInt(), selectedSatuan1, beratyangdiinginkan.toInt(), selectedSatuan2, targetTanggal, jumlahkalori.toInt(), val_email.toString(),1)


                }


            } else {
                Toast.makeText(this, "Data tidak cocok!", Toast.LENGTH_SHORT).show()
            }

        }
    }


    private fun addUserDataToFirestore(userId: String?, val_name: String, weight: Int, weight_unit: String, target_weight: Int, target_weight_unit: String, target_date: String, calorie: Int, email: String, tipe: Int) {
        // Add user data to Firestore
        val user = hashMapOf(
            "userId" to userId,
            "name" to val_name,
            "weight" to weight,
            "weight_unit" to weight_unit,
            "target_weight" to target_weight,
            "target_weight_unit" to target_weight_unit,
            "target_date" to target_date,
            "calorie" to calorie,
            "email" to email,
            "tipe" to tipe
            // Add any additional data you want to store
        )

        if (userId != null) {
            try {
                firestore.collection("users")
                    .document(userId)
                    .set(user)
                    .addOnSuccessListener {
                        // Log.d(TAG, "DocumentSnapshot successfully written!")
                        showToast("FIRESTORE SUKSES")
                        // TODO: Handle success
                    }
                    .addOnFailureListener { e ->
                        // Log.w(TAG, "Error writing document", e)
                        // TODO: Handle failure
                        showToast("FIRESTORE GAGAL")
                    }
            } catch (e: Exception) {
                // Handle other exceptions
                showToast("Terjadi kesalahan: ${e.message}")
                Log.d("KESALAHAN", e.message.toString())
            }
        }
    }


    private fun spinner2() {

        val spinner2 = binding.gs3spinnersatuan2
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = adapter2

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSatuan2 = data[position] // Get the selected item from the data list
                // Perform actions based on the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected (optional)
            }
        }
    }

    private fun spinner1() {
        val spinner1 = binding.gs3spinnersatuan1

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = adapter

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSatuan1 = data[position] // Get the selected item from the data list
                // Perform actions based on the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected (optional)
            }
        }
    }

    fun String.isFloat(): Boolean {
        return try {
            this.toFloat()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this@GS3_FormDataActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDateSet(
        view: android.widget.DatePicker?,
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        val selectDate = "$dayOfMonth/${month + 1} / $year"
        binding.gs3datePickerTextInputEditText.setText(selectDate)
    }

}