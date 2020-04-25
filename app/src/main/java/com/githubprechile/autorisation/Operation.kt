package com.githubprechile.autorisation

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
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
        val okbtn = findViewById<Button>(R.id.OK)

        okbtn.setOnClickListener{
            val tent = Intent(this,MainActivity::class.java)
            startActivity(tent)
            finish()
        }


//            // convert from byte array to bitmap
            fun getImage(image: ByteArray): Bitmap {
                return BitmapFactory.decodeByteArray(image, 0, image.size)
            }

        //init db
        dbHandler = DatabaseHandler(this)
        qrc.setImageBitmap(getImage(dbHandler!!.getAllImage()!!))

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
