package com.githubprechile.autorisation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn1 = findViewById<Button>(R.id.btn1)
        val btn2 = findViewById<Button>(R.id.btn2)

        btn1.setOnClickListener{
            val intent = Intent(this,Autorisation::class.java)
            startActivity(intent)
        }

        btn2.setOnClickListener{
            val intent = Intent(this,Operation::class.java)
            startActivity(intent)
        }


    }
}

