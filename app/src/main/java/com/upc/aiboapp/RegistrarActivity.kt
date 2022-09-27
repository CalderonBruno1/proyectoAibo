package com.upc.aiboapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class RegistrarActivity : AppCompatActivity() {

    private lateinit var txtPrimerNombre: EditText
    private lateinit var txtSegundoNombre: EditText
    private lateinit var txtPrimerApellido: EditText
    private lateinit var txtSegundoApellido: EditText
    private lateinit var txtDni_CE: EditText
    private lateinit var txtFechaNacimiento: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtContrasena: EditText
    private lateinit var txtContrasenaConfirmada: EditText
    private lateinit var btnRegistrarse: Button

    private val db= Firebase.database
    var storageManager:  FirebaseStorageManager= FirebaseStorageManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)
        setBirthdayEditText()
        asignarReferencias()
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
        val primerNombre = txtPrimerNombre.text.toString()
        val segundoNombre = txtSegundoNombre.text.toString()
        val primerApellido = txtPrimerApellido.text.toString()
        val segundoApellido = txtSegundoApellido.text.toString()
        val dni_ce = txtDni_CE.text.toString()
        val fecha_nac = txtFechaNacimiento.text.toString()
        val correo = txtEmail.text.toString()
        val contrasena = txtContrasena.text.toString()
        val contrasenaconfirmada = txtContrasenaConfirmada.text.toString()

        if( primerNombre.isEmpty() ||
            segundoNombre.isEmpty() ||
            primerApellido.isEmpty() ||
            segundoApellido.isEmpty() ||
            dni_ce.isEmpty() ||
            fecha_nac.isEmpty() ||
            correo.isEmpty() ||
            contrasena.isEmpty() ||
            contrasenaconfirmada.isEmpty()){
            Toast.makeText(this, "Completar todos los campos", Toast.LENGTH_LONG).show()
        }else{
            if(contrasena.equals(contrasenaconfirmada)) {
                storageManager.guardarUsuario(primerNombre,segundoNombre,primerApellido,segundoApellido,dni_ce,fecha_nac,
                    correo,contrasena,this)
                limpiarCajas()
            }else{
                Toast.makeText(this, "Ingrese contraseñas iguales", Toast.LENGTH_LONG).show()
            }
        }
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



    fun setBirthdayEditText() {
        txtFechaNacimiento=findViewById(R.id.txt_FechaNac)
        txtFechaNacimiento.addTextChangedListener(object : TextWatcher {

            private var current = ""
            private val ddmmyyyy = "DDMMYYYY"
            private val cal = Calendar.getInstance()

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString() != current) {
                    var clean = p0.toString().replace("[^\\d.]|\\.".toRegex(), "")
                    val cleanC = current.replace("[^\\d.]|\\.", "")

                    val cl = clean.length
                    var sel = cl
                    var i = 2
                    while (i <= cl && i < 6) {
                        sel++
                        i += 2
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean == cleanC) sel--

                    if (clean.length < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length)
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        var day = Integer.parseInt(clean.substring(0, 2))
                        var mon = Integer.parseInt(clean.substring(2, 4))
                        var year = Integer.parseInt(clean.substring(4, 8))

                        mon = if (mon < 1) 1 else if (mon > 12) 12 else mon
                        cal.set(Calendar.MONTH, mon - 1)
                        year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                        cal.set(Calendar.YEAR, year)
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(
                            Calendar.DATE) else day
                        clean = String.format("%02d%02d%02d", day, mon, year)
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8))

                    sel = if (sel < 0) 0 else sel
                    current = clean
                    txtFechaNacimiento.setText(current)
                    txtFechaNacimiento.setSelection(if (sel < current.count()) sel else current.count())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable) {

            }
        })
    }
}