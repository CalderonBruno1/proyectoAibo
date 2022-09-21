package com.upc.aibo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.upc.aibo.entidad.Usuario
import com.upc.aibo.modelo.UsuarioDAO

class RegistrarActivity : AppCompatActivity() {


    private lateinit var txtPrimerNombre:EditText
    private lateinit var txtSegundoNombre:EditText
    private lateinit var txtPrimerApellido:EditText
    private lateinit var txtSegundoApellido:EditText
    private lateinit var txtDni_CE:EditText
    private lateinit var txtFechaNacimiento:EditText
    private lateinit var txtEmail:EditText
    private lateinit var txtContrasena:EditText
    private lateinit var txtContrasenaConfirmada:EditText
    private lateinit var btnRegistrarse:Button


    var usuarioDAO: UsuarioDAO = UsuarioDAO(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)
        asignarReferencias();
    }

    private fun asignarReferencias(){
        txtPrimerNombre = findViewById(R.id.txt_PrimerNombre)
        txtSegundoNombre = findViewById(R.id.txt_SegundoNombre)
        txtPrimerApellido = findViewById(R.id.txt_PrimerApellido)
        txtSegundoApellido = findViewById(R.id.txt_SegundoApellido)
        txtDni_CE = findViewById(R.id.txt_DNI_CE)
        txtFechaNacimiento = findViewById(R.id.txt_FechaNac)
        txtEmail = findViewById(R.id.txt_correo)
        txtContrasena = findViewById(R.id.txt_Clave)
        txtContrasenaConfirmada = findViewById(R.id.txt_confirmar_clave)
        btnRegistrarse = findViewById(R.id.btnRegistrarse)

        btnRegistrarse.setOnClickListener{
            registrarUsuario()
        }
    }

    private fun registrarUsuario(){
        //Toast.makeText(this,"Hiciste click", Toast.LENGTH_LONG).show()
        val primerNombre = txtPrimerNombre.text.toString()
        val segundoNombre = txtSegundoNombre.text.toString()
        val primerApellido = txtPrimerApellido.text.toString()
        val segundoApellido = txtSegundoApellido.text.toString()
        val dni_ce = txtDni_CE.text.toString()
        val fecha_nac = txtFechaNacimiento.text.toString()
        val correo = txtEmail.text.toString()
        val contraseña = txtContrasena.text.toString()
        val contraseñaconfirmada = txtContrasenaConfirmada.text.toString()

        if( primerNombre.isEmpty() ||
            segundoNombre.isEmpty() ||
            primerApellido.isEmpty() ||
            segundoApellido.isEmpty() ||
            dni_ce.isEmpty() ||
            fecha_nac.isEmpty() ||
            correo.isEmpty() ||
            contraseña.isEmpty() ||
            contraseñaconfirmada.isEmpty()){
            Toast.makeText(this, "Completar todos los campos", Toast.LENGTH_LONG).show()
        }else{
            if(contraseña.equals(contraseñaconfirmada)) {
                val obj = Usuario()
                obj.primerNombre = primerNombre
                obj.segundoNombre = segundoNombre
                obj.primerApellido = primerApellido
                obj.segundoApellido = segundoApellido
                obj.dni_ce = dni_ce
                obj.fechaNac = fecha_nac
                obj.email = correo
                obj.contraseña = contraseña

                val mensaje = usuarioDAO.registrarUsuario(obj)
                mostrarMensaje(mensaje)
                limpiarCajas()
                val intent = Intent(baseContext, LoginActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Ingrese contraseñas iguales", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun mostrarMensaje(mensaje:String){
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Mensaje informativo")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", null)
        ventana.create().show()
    }

    private fun limpiarCajas(){
        txtPrimerNombre.setText("")
        txtSegundoNombre.setText("")
        txtPrimerApellido.setText("")
        txtSegundoApellido.setText("")
        txtDni_CE.setText("")
        txtFechaNacimiento.setText("")
        txtEmail.setText("")
        txtContrasena.setText("")
        txtContrasenaConfirmada.setText("")
    }
}