package com.upc.aibo.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UsuarioBD(context:Context):SQLiteOpenHelper(context, DATABASENAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASENAME = "aibo.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        var sql = "CREATE TABLE IF NOT EXISTS usuarios "+
                  "(id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                  "primerNombre TEXT NOT NULL, "+
                  "segundoNombre TEXT NOT NULL, "+
                  "primerApellido TEXT NOT NULL, "+
                  "segundoApellido TEXT NOT NULL, "+
                  "dni_ce TEXT NOT NULL, "+
                  "fechaNac TEXT NOT NULL, "+
                  "email TEXT NOT NULL, "+
                  "contraseña TEXT NOT NULL)"
        p0?.execSQL(sql)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(p0)
    }

}