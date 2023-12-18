package com.example.mobile_uas.getstarted

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobile_uas.R
import com.example.mobile_uas.databinding.ActivityGs1InputNameBinding
import com.example.mobile_uas.databinding.ActivityGs2SayHiBinding

class GS2_SayHiActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityGs2SayHiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGs2SayHiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentToFormData = Intent(this, GS3_FormDataActivity::class.java)

        val intent = intent
        var val_uid = intent.getStringExtra("val_uid")
        var val_name = intent.getStringExtra("val_name")
        var val_email = intent.getStringExtra("val_email")

        with(binding){
            val textHi = binding.gs2TvName
            textHi.text = "Hi, " + val_name

            binding.gs2BtLanjut.setOnClickListener {
                intentToFormData.putExtra("val_name", val_name)
                intentToFormData.putExtra("val_uid", val_uid)
                intentToFormData.putExtra("val_email", val_email)
                startActivity(intentToFormData)
            }
        }
    }
}