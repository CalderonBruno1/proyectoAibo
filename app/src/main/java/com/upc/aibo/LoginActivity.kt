package com.upc.aibo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.upc.aibo.entidad.Usuario
import com.upc.aibo.modelo.UsuarioDAO

class LoginActivity : AppCompatActivity() {

    private lateinit var txtDni_CE: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtContrasena: EditText

    private lateinit var btnRegistrar: Button
    private lateinit var btnAcceder: Button

    var usuarioDAO: UsuarioDAO = UsuarioDAO(this)

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
        btnRegistrar = findViewById(R.id.btn_registrarte)
        btnRegistrar.setOnClickListener{
            val intent = Intent(baseContext, RegistrarActivity::class.java)
            startActivity(intent)
        }
    }

    private fun VerificarDni(){
        btnAcceder = findViewById(R.id.btn_acceder)
        btnAcceder.setOnClickListener{

            val correo = txtEmail.text.toString()
            val contraseña = txtContrasena.text.toString()

            val dniLogin = txtDni_CE.text.toString()

            if(correo.isEmpty() || contraseña.isEmpty() || dniLogin.isEmpty()){
                Toast.makeText(this, "Completar todos los campos", Toast.LENGTH_LONG).show()
            }else {
                if (usuarioDAO.verificarDni(dniLogin)) {
                    DarAcceso(dniLogin)
                } else {
                    Toast.makeText(this, "DNI no esta registrado", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun DarAcceso(dniLogin: String){
        val usuario = usuarioDAO.ObtenerUsuario(dniLogin)

        val correo = txtEmail.text.toString()
        val contraseña = txtContrasena.text.toString()

        if(usuario.email.equals(correo) and
            usuario.contraseña.equals(contraseña)){
            mostrarMensajeAcceso()
            val intent = Intent(baseContext, ValidarRegistroActivity::class.java)
            startActivity(intent)
        }else{
            Toast.makeText(this, "Correo o Clave incorrectos", Toast.LENGTH_LONG).show()
        }
    }

    private fun mostrarMensajeAcceso(){
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Acceso Correcto")
        ventana.setMessage("Bienvenido a AIBOO bienestar")
        ventana.setPositiveButton("Entrar", null)
        ventana.create().show()
    }
}