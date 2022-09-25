package com.upc.aiboapp

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


class ValidarRegistroActivity : AppCompatActivity() {

    private lateinit var imgDniCara: ImageView
    private lateinit var imgDniSello: ImageView
    private lateinit var imgBoleta1: ImageView
    private lateinit var imgBoleta2: ImageView
    private lateinit var imgBoleta3: ImageView
    private lateinit var textDniCara: TextView
    private lateinit var textDniSello: TextView
    private lateinit var textBoleta1: TextView
    private lateinit var textBoleta2: TextView
    private lateinit var textBoleta3: TextView
    private lateinit var enviarBtn: Button

    lateinit var imgDniCaraUri: Uri
    lateinit var imgDniSelloUri: Uri
    lateinit var imgBol1Uri: Uri
    lateinit var imgBol2Uri: Uri
    lateinit var imgBol3Uri: Uri

    lateinit var img: String

    var storageManager:  FirebaseStorageManager= FirebaseStorageManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validar_registro)

        asignarReferencias()

    }

    fun asignarReferencias(){
        imgDniCara = findViewById(R.id.caraDniId)
        imgDniSello = findViewById(R.id.selloDniId)
        imgBoleta1 = findViewById(R.id.boleta1Id)
        imgBoleta2 = findViewById(R.id.boleta2Id)
        imgBoleta3 = findViewById(R.id.boleta3Id)
        textDniCara = findViewById(R.id.textDniCaraId)
        textDniSello = findViewById(R.id.textDniSelloId)
        textBoleta1 = findViewById(R.id.textBoleta1Id)
        textBoleta2 = findViewById(R.id.textBoleta2Id)
        textBoleta3 = findViewById(R.id.textBoleta3Id)
        enviarBtn = findViewById(R.id.btnEnviarImagenesId)

        imgDniCara.setOnClickListener {
            img="cara"
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }

        imgDniSello.setOnClickListener {
            img="sello"
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }

        imgBoleta1.setOnClickListener {
            img="boleta1"
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }

        imgBoleta2.setOnClickListener {
            img="boleta2"
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }

        imgBoleta3.setOnClickListener {
            img="boleta3"
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
        enviarBtn.setOnClickListener{
            uploadImage()
        }
    }

    private fun uploadImage(){
        storageManager.uploadImage(this,imgDniCaraUri,"Dni_Cara")
        storageManager.uploadImage(this,imgDniSelloUri,"Dni_Sello")
        storageManager.uploadImage(this,imgBol1Uri,"Boleta_1")
        storageManager.uploadImage(this,imgBol2Uri,"Boleta_2")
        storageManager.uploadImage(this,imgBol3Uri,"Boleta_3")

        //eliminamos las ftoso subidas al storage del celular
        File(getFilePath(imgDniCaraUri)).delete()
        File(getFilePath(imgDniSelloUri)).delete()
        File(getFilePath(imgBol1Uri)).delete()
        File(getFilePath(imgBol2Uri)).delete()
        File(getFilePath(imgBol3Uri)).delete()
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK){
            val intent=result.data
            val imageBitmap=intent?.extras?.get("data") as Bitmap

            val bytes = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

            val path = MediaStore.Images.Media.insertImage( //guarda la imagen en el celular despues de tomar foto
                this.getContentResolver(),
                imageBitmap,
                "ola",
                null
            )
            setPhotoUriImageView(path)
            setPhototxtColorImageView(imageBitmap)

        }

    }
    fun setPhototxtColorImageView(imageBitmap: Bitmap){
        when (img) {
            "cara" -> {imgDniCara.setImageBitmap(imageBitmap)
                textDniCara.setTextColor(Color.BLUE)
            }
            "sello" -> {imgDniSello.setImageBitmap(imageBitmap)
                textDniSello.setTextColor(Color.BLUE)
            }
            "boleta1" -> {imgBoleta1.setImageBitmap(imageBitmap)
                textBoleta1.setTextColor(Color.BLUE)
            }
            "boleta2" -> {imgBoleta2.setImageBitmap(imageBitmap)
                textBoleta2.setTextColor(Color.BLUE)
            }
            "boleta3" -> {imgBoleta3.setImageBitmap(imageBitmap)
                textBoleta3.setTextColor(Color.BLUE)
            }
            else -> { // Note the block
                print("x no es 1 o 2")
            }
        }
    }
    fun setPhotoUriImageView(path:String){
        when (img) {
            "cara" -> imgDniCaraUri=Uri.parse(path)
            "sello" -> imgDniSelloUri=Uri.parse(path)
            "boleta1" -> imgBol1Uri=Uri.parse(path)
            "boleta2" -> imgBol2Uri=Uri.parse(path)
            "boleta3" -> imgBol3Uri=Uri.parse(path)
            else -> "error"
        }
    }
    private fun getFilePath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA) //da la ruta  de la galaria uri es ruta de imagen
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex: Int = cursor.getColumnIndex(projection[0])//columname devuelve el indice para el nombre de la columna
            val picturePath: String = cursor.getString(columnIndex) // returns null
            cursor.close()
            return picturePath
        }
        return null
    }
}
