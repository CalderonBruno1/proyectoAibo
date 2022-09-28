package com.upc.aiboapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat

class EsperaActivity : AppCompatActivity() {
    private lateinit var esperandoBtnId:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_espera)
        asignarReferencias()
    }

    fun asignarReferencias(){
        esperandoBtnId = findViewById(R.id.esperandoBtnId)

        esperandoBtnId.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            ContextCompat.startActivity(this, intent, null)
        }
    }
}