package com.githubprechile.autorisation

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.githubprechile.autorisation.Database.DatabaseHandler
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.io.ByteArrayOutputStream


class Code : AppCompatActivity() {
    var dbHandler: DatabaseHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code)

        supportActionBar!!.title="Resultat de la demande"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val qrcodeImageView = findViewById<ImageView>(R.id.qrcodeImageView)
        val txt = findViewById<TextView>(R.id.codeQR)

        //init db
        dbHandler = DatabaseHandler(this)
        val code = dbHandler!!.getCode()
        txt.text = code

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(code, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }


            // convert from bitmap to byte array
            fun getBytes(bitmap: Bitmap): ByteArray {
                val stream = ByteArrayOutputStream()
                bitmap.compress(CompressFormat.PNG, 0, stream)
                return stream.toByteArray()
            }

//            // convert from byte array to bitmap
//            fun getImage(image: ByteArray): Bitmap {
//                return BitmapFactory.decodeByteArray(image, 0, image.size)
//            }


        qrcodeImageView.setImageBitmap(bitmap)
        dbHandler!!.insertImage(getBytes(bitmap))
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
