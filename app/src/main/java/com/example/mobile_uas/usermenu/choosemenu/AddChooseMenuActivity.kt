package com.example.mobile_uas.usermenu.choosemenu

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.mobile_uas.BottomNavigationActivity
import com.example.mobile_uas.R
import com.example.mobile_uas.data.database.MenuRoomDatabase
import com.example.mobile_uas.data.model.room.MenuUser
import com.example.mobile_uas.databinding.ActivityAddChooseMenuBinding
import com.example.mobile_uas.databinding.ActivityAdminEditFoodBinding
import com.example.mobile_uas.usermenu.addmenu.TimePicker
import com.example.mobile_uas.util.SharedPreferencesHelper
import com.example.myapplication.data.database.MenuUserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class AddChooseMenuActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityAddChooseMenuBinding
    private lateinit var mMenuUserDao: MenuUserDAO
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddChooseMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val foodName = intent.getStringExtra("foodName")
        val foodCalorie = intent.getIntExtra("foodCalorie",0)


        binding.addChoosemenuName.text = Editable.Factory.getInstance().newEditable(foodName.toString())
        binding.addChoosemenuEtKalori.text = Editable.Factory.getInstance().newEditable(foodCalorie.toString())



        //InitDB
        val db = MenuRoomDatabase.getDatabase(this@AddChooseMenuActivity)
        mMenuUserDao = db?.MenuUserDAO() ?: throw Exception("Database not initialized")
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(this@AddChooseMenuActivity)

        //Spinner
        val spinner1 = binding.addChoosemenuSpinnerMakan
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
        binding.addChoosemenuTime.setOnClickListener{
            val timepicker = TimePicker()
            timepicker.show(supportFragmentManager, "TP")
        }


        binding.addChoosemenuBtSubmit.setOnClickListener {
            var nameMakan = binding.addChoosemenuName.text.toString()
            var waktuMakan = binding.addChoosemenuTime.text.toString()
            var jumlahKaloriNew = binding.addChoosemenuEtKalori.text.toString()
            var jumlahServing = binding.addChoosemenuEtServing.text.toString()

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
                            foodCalorie = kaloriInt,
                            serving = servingInt,
                            date = getCurrentDateInGMTPlus7()
                        )

                        mMenuUserDao.insert(menuUser)
                        val toMainActivity = Intent(this@AddChooseMenuActivity, BottomNavigationActivity::class.java)
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
        binding.addChoosemenuTime.setText(selectedTime)
    }

    fun getCurrentDateInGMTPlus7(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormat.timeZone = TimeZone.getTimeZone("GMT+7")
        return dateFormat.format(Date())
    }
}