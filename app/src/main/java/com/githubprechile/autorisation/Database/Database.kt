package com.githubprechile.autorisation.Database


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.githubprechile.autorisation.model.ApiAutorisation
import java.io.ByteArrayOutputStream


/**
 * Created by Eyehunt Team on 07/06/18.
 */
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSIOM) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME " +
                "($ID Integer PRIMARY KEY, $CODE TEXT)"
        db?.execSQL(CREATE_TABLE)

        val CREATE_TABLE_BIT = "CREATE TABLE $TABLE_NAME1 " +
                "($Id Integer PRIMARY KEY, $BIT BLOB)"
        db?.execSQL(CREATE_TABLE_BIT)

        val CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME2 " +
                "($IdUser TEXT DEFAULT 1,$emei TEXT,$nom TEXT, $prenom TEXT," +
                " $adresse TEXT, $destiation TEXT, $typeE TEXT, $immat TEXT," +
                "$motif TEXT, $nomDes TEXT)"
        db?.execSQL(CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Called when the database needs to be upgraded
    }

    //Inserting (Creating) data
    fun addCode(codes: String?): Boolean {
        //Create and/or open a database that will be used for reading and writing.
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(CODE, codes)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.v("InsertedCode", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }


    //get all users
    fun getCode(): String {
        var allUser: String = "";
        val db = readableDatabase
        val selectALLQuery = "SELECT $CODE FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var username = cursor.getString(cursor.getColumnIndex(CODE))
                    allUser = "$username"
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return allUser
    }

    fun getBitmapAsByteArray(bitmap: Bitmap): ByteArray? {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }

    fun insertImage( image: ByteArray?){
        val database = this.writableDatabase;
        val cv =   ContentValues();
        cv.put(BIT,   image);
        database.insert( TABLE_NAME1, null, cv );
    }


    fun getAllImage(): ByteArray? {
        var Bit:ByteArray?=null
        val db = readableDatabase
        val qu = "SELECT $BIT FROM $TABLE_NAME1 "
        val cursor = db.rawQuery(qu, null)
        if (cursor != null) {
        if (cursor.moveToFirst()) {
            do {
                var imgByte = cursor.getBlob(0)
               // return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.size)
                Bit = imgByte
            } while (cursor.moveToNext())
        }
    }
    cursor.close()
    db.close()
    return Bit

    }


    //update setting
    fun insertUser(params:ApiAutorisation):Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(emei, params.emei)
        values.put(nom, params.nomD)
        values.put(prenom, params.prenomD)
        values.put(adresse, params.adresse)
        values.put(destiation, params.adresse2)
        values.put(typeE, params.engin)
        values.put(immat, params.immatriculation)
        values.put(motif, params.motif)
        values.put(nomDes, params.lieu)
        val _success = db.insert(TABLE_NAME2, null, values)
        db.close()
        Log.v("InsertedNewUser", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }


    fun nom(): String {
        var Nom=""
        val db = readableDatabase
        val selectALLQuery = "SELECT $nom FROM $TABLE_NAME2"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var nom = cursor.getString(cursor.getColumnIndex(nom))
                    Nom = "$nom"
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return Nom
    }

    fun prenom(): String {
        var Prenom=""
        val db = readableDatabase
        val selectALLQuery = "SELECT $prenom FROM $TABLE_NAME2"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var prenom = cursor.getString(cursor.getColumnIndex(prenom))
                    Prenom = "$prenom"
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return Prenom
    }

    fun adresse(): String {
        var Adresse=""
        val db = readableDatabase
        val selectALLQuery = "SELECT $adresse FROM $TABLE_NAME2"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var adresse = cursor.getString(cursor.getColumnIndex(adresse))
                    Adresse = "$adresse"
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return Adresse
    }


    fun lieu(): String {
        var Lieu=""
        val db = readableDatabase
        val selectALLQuery = "SELECT $destiation FROM $TABLE_NAME2"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var lieu = cursor.getString(cursor.getColumnIndex(destiation))
                    Lieu = "$lieu"
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return Lieu
    }

    fun engin(): String {
        var Engin=""
        val db = readableDatabase
        val selectALLQuery = "SELECT $typeE FROM $TABLE_NAME2"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var engin = cursor.getString(cursor.getColumnIndex(typeE))
                    Engin = "$engin"
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return Engin
    }

    fun immat(): String {
        var Immat=""
        val db = readableDatabase
        val selectALLQuery = "SELECT $immat FROM $TABLE_NAME2"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var immat = cursor.getString(cursor.getColumnIndex(immat))
                    Immat = "$immat"
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return Immat
    }

    fun motif(): String {
        var Motif=""
        val db = readableDatabase
        val selectALLQuery = "SELECT $motif FROM $TABLE_NAME2"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var motif = cursor.getString(cursor.getColumnIndex(motif))
                    Motif = "$motif"
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return Motif
    }

    fun nomDest(): String {
        var NomD=""
        val db = readableDatabase
        val selectALLQuery = "SELECT $nomDes FROM $TABLE_NAME2"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var nomD = cursor.getString(cursor.getColumnIndex(nomDes))
                    NomD = "$nomD"
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return NomD
    }



    companion object {
        private val DB_NAME = "UsersDB"
        private val DB_VERSIOM = 1;
        private val TABLE_NAME = "CodeQR"
        private val ID = "id"
        private val CODE = "code"

        private val TABLE_NAME1 = "Bitmap"
        private val Id = "id"
        private val BIT = "imageB"

        private val TABLE_NAME2 = "user"
        private val IdUser = "id"
        private val emei = "emei"
        private val nom = "nom"
        private val prenom = "prenom"
        private val adresse = "adresse"
        private val destiation = "destination"
        private val typeE = "typeEngins"
        private val immat = "immatriculation"
        private val motif = "motif"
        private val nomDes = "nomDestination"




    }


}