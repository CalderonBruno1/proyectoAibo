package com.upc.aiboapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storageMetadata

class SimulacionActivity : AppCompatActivity() {

    //val bundle = intent.extras
    //val txtDni_CE = bundle?.getString("dni")

    private lateinit var txtSueldoPlanilla: TextView

    private lateinit var barMontoPrestamo: SeekBar
    private lateinit var txtMontoPrestamo: TextView

    private lateinit var barCuotas: SeekBar
    private lateinit var txtCuotas: TextView

    private lateinit var btnSolicitarYa: Button

    private val db = Firebase.database
    var storageManager:  FirebaseStorageManager= FirebaseStorageManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulacion)

        txtSueldoPlanilla = findViewById(R.id.txt_SueldoPlanilla)

        txtMontoPrestamo = findViewById((R.id.MontoPrestamo))

        barMontoPrestamo = findViewById(R.id.BarMontoPrestamo)
        barMontoPrestamo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                txtMontoPrestamo.setText("S/"+progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        txtCuotas = findViewById(R.id.Cuotas)

        barCuotas = findViewById(R.id.BarCuotas)
        barCuotas.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                txtCuotas.setText(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        btnSolicitarYa = findViewById(R.id.btn_SolicitaYa)
        btnSolicitarYa.setOnClickListener{
            solicitarPrestamo()
        }

    }

    private fun solicitarPrestamo(){
        //val dni = txtDni_CE.toString()
        val sueldo = txtSueldoPlanilla.text.toString()
        val monto = txtMontoPrestamo.text.toString()
        val cuotas = txtCuotas.text.toString()
        val interes = "0.125" //12.5% mensual
        val estado = "Pendiente"

        if(sueldo.isEmpty()){
            Toast.makeText(this,"Completar sueldo", Toast.LENGTH_LONG).show()
        }else{
            storageManager.ingresarSolicitudPrestamo(sueldo, monto, cuotas, interes, estado, this)
        }


    }


}

