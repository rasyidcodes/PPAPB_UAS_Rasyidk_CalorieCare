package com.example.mobile_uas.addmenu

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.mobile_uas.BottomNavigationActivity
import com.example.mobile_uas.R
import com.example.mobile_uas.data.database.MenuRoomDatabase
import com.example.mobile_uas.data.model.room.MenuUser
import com.example.mobile_uas.databinding.ActivityAddFoodBinding
import com.example.mobile_uas.util.SharedPreferencesHelper
import com.example.myapplication.data.database.MenuUserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class AddFoodActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityAddFoodBinding
    private lateinit var mMenuUserDao: MenuUserDAO
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFoodBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //InitDB
        val db = MenuRoomDatabase.getDatabase(this@AddFoodActivity)
        mMenuUserDao = db?.MenuUserDAO() ?: throw Exception("Database not initialized")
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(this@AddFoodActivity)

        //Spinner
        val spinner1 = binding.makanSpinnerMakan
        val data = listOf("lunch", "dinner", "breakfast")
        var selectTypeMakan = ""

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = adapter

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectTypeMakan = data[position] // Get the selected item from the data list
                // Perform actions based on the selected item
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected (optional)
            }
        }

        //Time Picker
        binding.makanTime.setOnClickListener{
            val timepicker = TimePicker()
            timepicker.show(supportFragmentManager, "TP")
        }


        binding.makanBtSubmit.setOnClickListener {
            var nameMakan = binding.makanEtName.text.toString()
            var waktuMakan = binding.makanTime.text.toString()
            var jumlahKaloriNew = binding.makanEtKalori.text.toString()
            var jumlahServing = binding.makanEtServing.text.toString()

            try {
                // Attempt to convert jumlahKaloriNew and jumlahServing to integers
                val kaloriInt = jumlahKaloriNew.toInt()
                val servingInt = jumlahServing.toInt()

                if (nameMakan.isBlank() || waktuMakan.isBlank() || kaloriInt <= 0 || servingInt <= 0 || selectTypeMakan.isBlank()) {
                    Toast.makeText(this, "Data tidak valid!", Toast.LENGTH_SHORT).show()
                } else {
                    // Use coroutines to perform the database insertion in the background
                    CoroutineScope(Dispatchers.IO).launch {
                        val menuUser = MenuUser(
                            userId = sharedPreferencesHelper.getUserId().toString(),
                            type = selectTypeMakan,
                            action = "makan",
                            foodName = nameMakan,
                            foodCalorie = kaloriInt * servingInt,
                            serving = servingInt,
                            date = getCurrentDateInGMTPlus7()
                        )

                        mMenuUserDao.insert(menuUser)
                        val toMainActivity = Intent(this@AddFoodActivity, BottomNavigationActivity::class.java)
                        startActivity(toMainActivity)
                    }
                }
            } catch (e: NumberFormatException) {
                // Handle the case where the conversion to Int fails
                Toast.makeText(this, "Invalid numeric input!", Toast.LENGTH_SHORT).show()
            }
        }



    }

    override fun onTimeSet(view: android.widget.TimePicker?, hourOfDay: Int, minute: Int) {
        val selectedTime =  String.format("%02d:%02d", hourOfDay, minute)
        binding.makanTime.setText(selectedTime)
    }

    fun getCurrentDateInGMTPlus7(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormat.timeZone = TimeZone.getTimeZone("GMT+7")
        return dateFormat.format(Date())
    }
}