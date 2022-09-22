package com.upc.aibo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var btnStart:ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startApp()
    }

    private fun startApp(){
        btnStart = findViewById(R.id.btn_Aibo)
        btnStart.setOnClickListener{
            val intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}