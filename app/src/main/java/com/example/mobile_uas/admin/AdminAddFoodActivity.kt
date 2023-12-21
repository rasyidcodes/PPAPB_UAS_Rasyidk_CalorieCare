package com.example.mobile_uas.admin

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
                            "Data berhasil ditambahkan",
                            Toast.LENGTH_SHORT
                        ).show()

                        createNotification()
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

    private fun createNotification() {
        // Notification channel is required for Android Oreo (API level 26) and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "YOUR_CHANNEL_ID",
                "YOUR_CHANNEL_NAME",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val builder = NotificationCompat.Builder(this, "YOUR_CHANNEL_ID")
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("CalorieCare")
            .setContentText("Admin Baru Saja Menambahkan Makanan")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@AdminAddFoodActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(1, builder.build())
        }
    }

}