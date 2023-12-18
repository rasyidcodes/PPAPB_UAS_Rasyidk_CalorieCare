package com.example.mobile_uas.getstarted

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mobile_uas.R
import com.example.mobile_uas.databinding.ActivityAuthBinding
import com.example.mobile_uas.databinding.ActivityGs1InputNameBinding

class GS1_inputNameActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityGs1InputNameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGs1InputNameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val receivedIntent = intent
        val val_uid = receivedIntent.getStringExtra("val_uid")
        val val_email = receivedIntent.getStringExtra("val_email")
        val intent = Intent(this, GS2_SayHiActivity::class.java)

        with(binding){
            binding.gs1BtLanjut.setOnClickListener {
                val name = binding.gs1EtName.text.toString()
                if (name == ""){
                    Toast.makeText(applicationContext, "Nama Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show()
                }else{
                    intent.putExtra("val_name", name)
                    intent.putExtra("val_uid", val_uid)
                    intent.putExtra("val_email", val_email);
                    startActivity(intent)
                }
            }
        }


    }
}