package com.upc.aiboapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    private lateinit var txtDni_CE: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtContrasena: EditText

    private lateinit var btnRegistrar: Button
    private lateinit var btnAcceder: Button

    var storageManager:  FirebaseStorageManager= FirebaseStorageManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        asignarReferencias()
    }

    private fun asignarReferencias(){

        txtDni_CE = findViewById(R.id.txt_dni_ce_login)
        txtEmail = findViewById(R.id.txt_correo_login)
        txtContrasena = findViewById(R.id.txt_contraseña_login)
        // ---- REGISTRAR -----
        btnRegistrar = findViewById(R.id.btn_registrarte)
        btnRegistrar.setOnClickListener{
            RegistrarUsuario()
        }
        // ---- ACCEDER -----
        btnAcceder = findViewById(R.id.btn_acceder)
        btnAcceder.setOnClickListener{
            VerificarDni()
        }
    }
    private fun RegistrarUsuario(){
        val intent = Intent(baseContext, RegistrarActivity::class.java)
        startActivity(intent)
    }
    private fun VerificarDni(){
        val correo = txtEmail.text.toString()
        val contraseña = txtContrasena.text.toString()
        val dniLogin = txtDni_CE.text.toString()

        if(correo.isEmpty() || contraseña.isEmpty() || dniLogin.isEmpty()){
            Toast.makeText(this, "Completar todos los campos", Toast.LENGTH_LONG).show()
        }else {
            storageManager.loginUsuario(dniLogin,contraseña,correo,this)
        }
    }
}