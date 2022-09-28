package com.upc.aiboapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat

class AceptadoActivity : AppCompatActivity() {
    private lateinit var aceptadoBtnId: Button
    var storageManager:  FirebaseStorageManager= FirebaseStorageManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aceptado)
        asignarReferencias()
    }

    fun asignarReferencias(){
        aceptadoBtnId = findViewById(R.id.aceptadoBtnId)

        aceptadoBtnId.setOnClickListener(){
            val dniUser: String? =getIntent().getStringExtra("dni")
            if (dniUser != null) {
                storageManager.setValidacionUsser(dniUser,this,"confirmado")
            }
        }
    }
}