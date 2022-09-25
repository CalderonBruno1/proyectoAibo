package com.upc.aiboapp

import android.R
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class FirebaseStorageManager {

    private val mStorageRef = FirebaseStorage.getInstance().reference

    fun uploadImage(mContext: Context,imageURI: Uri, folder: String){
        val progressDialog = ProgressDialog(mContext)
        progressDialog.setMessage("Uploading File")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val Folder: StorageReference =FirebaseStorage.getInstance().getReference().child(folder) //crea folder en firebase storage
        val filename: StorageReference =Folder.child("imagen"+imageURI!!.lastPathSegment)//crea el child del folder en firebase storage

        filename.putFile(imageURI).addOnSuccessListener {//sube la foto del url dado dentro del cel, en la ubicacion dada en filename en firebase
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(mContext,"Foto subida correctamente", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(mContext,"Foto fallo al subirse", Toast.LENGTH_SHORT).show()
        }
    }

}