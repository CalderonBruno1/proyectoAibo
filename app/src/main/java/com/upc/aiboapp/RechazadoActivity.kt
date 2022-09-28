package com.upc.aiboapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat

class RechazadoActivity : AppCompatActivity() {
    private lateinit var rechazadoBtnId: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rechazado)
        asignarReferencias()
    }

    fun asignarReferencias(){
        rechazadoBtnId = findViewById(R.id.rechazadoBtnId)

        rechazadoBtnId.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            ContextCompat.startActivity(this, intent, null)
        }
    }
}