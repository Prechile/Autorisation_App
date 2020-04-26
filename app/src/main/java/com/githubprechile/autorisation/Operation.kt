package com.githubprechile.autorisation

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.githubprechile.autorisation.Database.DatabaseHandler

class Operation : AppCompatActivity() {
    var dbHandler: DatabaseHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operation)

        supportActionBar!!.title="Resultat de la demande"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val qrc = findViewById<ImageView>(R.id.qrcodeImageView1)
        val txt = findViewById<TextView>(R.id.codeQR)


//            // convert from byte array to bitmap
            fun getImage(image: ByteArray): Bitmap {
                return BitmapFactory.decodeByteArray(image, 0, image.size)
            }

        //init db
        dbHandler = DatabaseHandler(this)
        try {
            qrc.setImageBitmap(getImage(dbHandler!!.getAllImage()!!))
            txt.setTextColor(Color.parseColor("#196F3D"))
        }catch (e:Exception){
        txt.text="Désolé, vous n'avez aucun passe valide, merci de bien en demandé un "
        txt.setTextColor(Color.parseColor("#9C0E16"))
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
