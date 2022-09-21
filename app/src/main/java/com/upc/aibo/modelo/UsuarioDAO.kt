package com.upc.aibo.modelo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.upc.aibo.entidad.Usuario
import com.upc.aibo.util.UsuarioBD

class UsuarioDAO(context: Context) {
    private var usuarioBD: UsuarioBD = UsuarioBD(context)

    //----------------------------- REGISTRAR USUARIO -----------------------------
    fun registrarUsuario(usuario: Usuario): String{
        var respuesta = ""
        val db = usuarioBD.writableDatabase
        try {
            val valores = ContentValues()
            valores.put("primerNombre", usuario.primerNombre)
            valores.put("segundoNombre", usuario.segundoNombre)
            valores.put("primerApellido", usuario.primerApellido)
            valores.put("segundoApellido", usuario.segundoApellido)
            valores.put("dni_ce", usuario.dni_ce)
            valores.put("fechaNac", usuario.fechaNac)
            valores.put("email", usuario.email)
            valores.put("contraseña", usuario.contraseña)
            var resultado = db.insert("usuarios", null, valores)
            if(resultado == -1L){
                respuesta = "Error al insertar"
            }else{
                respuesta = "Se registro correctamente"
            }

        }catch (e:Exception){
            respuesta = e.message.toString()

        }finally {
            db.close()

        }
        return respuesta
    }


    //----------------------------- VERIFICAR DNI -----------------------------
    fun verificarDni(dniLogin: String): Boolean{
        var respuesta = false;
        val query = "SELECT * FROM usuarios"
        val db = usuarioBD.readableDatabase
        val cursor:Cursor

        try{
            cursor = db.rawQuery(query,null)
            if(cursor.count > 0){
                cursor.moveToFirst()
                do{
                    val dni:String = cursor.getString(cursor.getColumnIndexOrThrow("dni_ce"))
                    if(dni.equals(dniLogin)){
                        respuesta = true
                        break
                    }
                }while(cursor.moveToNext())
            }
        }catch (e:Exception){
            var mensaje = ""
            mensaje = e.message.toString()
        }finally {
            db.close();
        }



        return respuesta;
    }

    fun ObtenerUsuario(dniLogin: String): Usuario{
        var usuario = Usuario();

        val query = "SELECT * FROM usuarios"
        val db = usuarioBD.readableDatabase
        val cursor:Cursor

        try{
            cursor = db.rawQuery(query,null)
            if(cursor.count > 0){
                cursor.moveToFirst()
                do{
                    val dni:String = cursor.getString(cursor.getColumnIndexOrThrow("dni_ce"))
                    if(dni.equals(dniLogin)){
                        val id:Int = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                        val primerNombre:String = cursor.getString(cursor.getColumnIndexOrThrow("primerNombre"))
                        val segundoNombre:String = cursor.getString(cursor.getColumnIndexOrThrow("segundoNombre"))
                        val primerApellido:String = cursor.getString(cursor.getColumnIndexOrThrow("primerApellido"))
                        val segundoApellido:String = cursor.getString(cursor.getColumnIndexOrThrow("segundoApellido"))
                        val dni_ce:String = cursor.getString(cursor.getColumnIndexOrThrow("dni_ce"))
                        val fechaNac:String = cursor.getString(cursor.getColumnIndexOrThrow("fechaNac"))
                        val email:String = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                        val contraseña:String = cursor.getString(cursor.getColumnIndexOrThrow("contraseña"))

                        usuario.id = id
                        usuario.primerNombre = primerNombre
                        usuario.segundoNombre = segundoNombre
                        usuario.primerApellido = primerApellido
                        usuario.segundoApellido = segundoApellido
                        usuario.dni_ce = dni_ce
                        usuario.fechaNac = fechaNac
                        usuario.email = email
                        usuario.contraseña = contraseña

                        break
                    }
                }while(cursor.moveToNext())
            }
        }catch (e:Exception){
            var mensaje = ""
            mensaje = e.message.toString()
        }finally {
            db.close();
        }

        return usuario;
    }
}