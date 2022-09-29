package com.upc.aiboapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.contentValuesOf
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.upc.aiboapp.entidad.Prestamos
import com.upc.aiboapp.entidad.Usuario


class FirebaseStorageManager {

    private val db= Firebase.database

    fun uploadImage(mContext: Context,imageURI: Uri, folder: String,dni:String){
        val progressDialog = ProgressDialog(mContext)
        progressDialog.setMessage("Uploading File")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val Folder: StorageReference =FirebaseStorage.getInstance().getReference().child(dni).child(folder) //crea folder en firebase storage
        val filename: StorageReference =Folder.child("imagen"+imageURI!!.lastPathSegment)//crea el child del folder en firebase storage

        filename.putFile(imageURI).addOnSuccessListener {//sube la foto del url dado dentro del cel, en la ubicacion dada en filename en firebase
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(mContext,"Foto subida correctamente", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(mContext,"Foto fallo al subirse", Toast.LENGTH_SHORT).show()
        }
    }

    fun loginUsuario(dni:String,contra:String,correo:String,mContext: Context) {
        val progressDialog = ProgressDialog(mContext)
        esperaLogin(progressDialog)

        db.getReference("usuario").orderByChild("dni_ce").equalTo(dni)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()){
                        for (dataSnap in dataSnapshot.children) {
                            val usuario: Usuario? = dataSnap?.getValue(Usuario::class.java)

                            if (contra == usuario?.contrasena && correo == usuario?.email){
                                if (progressDialog.isShowing) progressDialog.dismiss()

                                if (usuario.validacion=="espera"){
                                    val intent = Intent(mContext, EsperaActivity::class.java)
                                    startActivity(mContext,intent,null)
                                }else if(usuario.validacion=="rechazado"){
                                    val intent = Intent(mContext, RechazadoActivity::class.java)
                                    startActivity(mContext,intent,null)
                                }else if(usuario.validacion=="aceptado"){
                                    val intent = Intent(mContext, AceptadoActivity::class.java)
                                    intent.putExtra("dni",dni)
                                    startActivity(mContext,intent,null)
                                }else if(usuario.validacion=="nuevo"){
                                    val intent = Intent(mContext, ValidarRegistroActivity::class.java)
                                    intent.putExtra("dni",dni)
                                    startActivity(mContext,intent,null)
                                }else if(usuario.validacion=="confirmado"){
                                    val intent = Intent(mContext, SimulacionActivity::class.java)
                                    intent.putExtra("dni",dni)
                                    startActivity(mContext,intent,null)
                                }

                            }else{
                                if (progressDialog.isShowing) progressDialog.dismiss()
                                Toast.makeText(mContext, "Correo o Clave incorrectos", Toast.LENGTH_LONG).show()
                            }
                        }
                    }else{
                        if (progressDialog.isShowing) progressDialog.dismiss()
                        Toast.makeText(mContext, "Dni no existe", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }
    fun guardarUsuario(primerNombre:String,segundoNombre:String,primerApellido:String,
                        segundoApellido:String,dni_CE:String,fechaNacimiento:String,email:String,contrasena:String,context: Context){
        val referencia=db.getReference("usuario")

        val usuario=Usuario(primerNombre,segundoNombre,primerApellido,segundoApellido,dni_CE,fechaNacimiento,email,contrasena)
        referencia.child(referencia.push().key.toString()).setValue(usuario).addOnCompleteListener{
            mostrarMensajeRegistro(context)

        }.addOnFailureListener{
                err->Toast.makeText(context, "Error ${err.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun ingresarSolicitudPrestamo(sueldo:String, monto:String, cuotas:String,
                                  interes:String, estado:String, context: Context){
        val referencia=db.getReference("prestamos")

        val prestamo = Prestamos(sueldo, monto, cuotas, interes, estado)
        referencia.child(referencia.push().key.toString()).setValue(prestamo).addOnCompleteListener {
            mostrarMensajeSolicitud(context)
            val intent = Intent(context, LocalizacionActivity::class.java)
            startActivity(context,intent,null)
        }.addOnFailureListener{
            err->Toast.makeText(context, "Error ${err.message}", Toast.LENGTH_LONG).show()
        }
    }


    private fun mostrarMensajeSolicitud(context: Context){
        val ventana = AlertDialog.Builder(context)
        ventana.setTitle("Mensaje informativo")
        ventana.setMessage("Se registro correctamente")
        ventana.setPositiveButton("Aceptar"){ dialog, which ->
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(context,intent,null)
        }
        ventana.create().show()
    }

    private fun mostrarMensajeRegistro(context: Context){
        val ventana = AlertDialog.Builder(context)
        ventana.setTitle("Mensaje informativo")
        ventana.setMessage("Se registro correctamente")
        ventana.setPositiveButton("Aceptar"){ dialog, which ->
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(context,intent,null)
        }
        ventana.create().show()
    }
    private fun esperaLogin(progressDialog:ProgressDialog){
        progressDialog.setMessage("Ingresando...")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    fun setValidacionUsser(dni:String,context: Context, validacion:String){
        db.getReference("usuario").orderByChild("dni_ce").equalTo(dni)
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    userSnapshot.ref.child("validacion").setValue(validacion)
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(context, intent, null)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}