package com.githubprechile.autorisation

import android.Manifest.permission.READ_PHONE_STATE
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.provider.Settings.Secure
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.githubprechile.autorisation.Database.DatabaseHandler
import com.githubprechile.autorisation.Metier.Metier
import com.githubprechile.autorisation.Retrofit.URL
import com.githubprechile.autorisation.model.ApiAutorisation
import android.provider.Settings.System;
import com.githubprechile.autorisation.Retrofit.Interface
import com.githubprechile.autorisation.Retrofit.UnsafeOK_HttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Autorisation : AppCompatActivity() {

    var dbHandler: DatabaseHandler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autorisation)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val nom = findViewById<EditText>(R.id.nom)
        val prenom = findViewById<EditText>(R.id.prenom)
        val adresse = findViewById<EditText>(R.id.adresse)
        val adresse2 = findViewById<EditText>(R.id.adresse2)
        val imm = findViewById<EditText>(R.id.immatriculation)
        val motif = findViewById<Spinner>(R.id.motif)
        val lieu = findViewById<EditText>(R.id.nomLieu)
        val engin = findViewById<Spinner>(R.id.engin)
        val btn = findViewById<Button>(R.id.btnValider)
        val pb = findViewById<ProgressBar>(R.id.pgbar)
        dbHandler = DatabaseHandler(this)
        pb.visibility=View.GONE


        val display1 = dbHandler!!.nom()
        val display2 = dbHandler!!.prenom()
        val display3 = dbHandler!!.adresse()
        val display4 = dbHandler!!.lieu()
        val display5 = dbHandler!!.engin()
        val display7 = dbHandler!!.immat()
        val display8 = dbHandler!!.motif()
        val display9 = dbHandler!!.nomDest()


            nom.setText(display1)
            prenom.setText(display2)
            adresse.setText(display3)
            adresse2.setText(display4)
            // engin.selectedItem(display5)
            // motif.setText(display8)
            lieu.setText(display9)


        val ListEngin =arrayOf("Véhicule", "Moto", "piéton")
        val adapterOrgane = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ListEngin)
        engin.adapter = adapterOrgane

        val ListMotif =arrayOf("Hôpital", "pharmacie", "clinique")
        val adapterMotif = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ListMotif)
        motif.adapter = adapterMotif
        val metier = Metier()
        var etat:String?
        val androidID: String =
            System.getString(this.contentResolver, Secure.ANDROID_ID)


        btn.setOnClickListener(){
            pb.visibility=View.VISIBLE
            if(TextUtils.isEmpty(nom.text.toString()) && TextUtils.isEmpty(prenom.text.toString())||
                TextUtils.isEmpty(adresse.text.toString())||TextUtils.isEmpty(imm.text.toString())
                ||TextUtils.isEmpty(lieu.text.toString())){

                nom.error = "Entrer votre nom";
                prenom.error = "Entrer votre prénom"
                adresse.error = "saisir votre adresse"
                imm.error = "saisir une immatriculation"
                imm.error = "saisir le nom du lieu"
                Toast.makeText(applicationContext,
                    "veuillez renseigner tous mes champs" , Toast.LENGTH_LONG).show()

            }else{
                pb.visibility = View.VISIBLE
                //connexion.isClickable=false

                val person=ApiAutorisation(androidID,nom.text.toString(),lieu.text.toString(),prenom.text.toString(),motif.selectedItem.toString(),
                    adresse.text.toString(),adresse2.text.toString(),engin.selectedItem.toString(),imm.text.toString())

                val retrofit = Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(UnsafeOK_HttpClient.getUnsafeOkHttpClient().build())
                    .build()

                val service: Interface = retrofit.create(Interface::class.java)
                val call: Call<String> =service.envoiePersonne(person)

                call.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        pb.visibility = View.GONE
                        response.body()

                        if (response.code()==200) {

                            val code = response.body()
                            dbHandler!!.addCode(code)
                            dbHandler!!.insertUser(person)

                            val builder = AlertDialog.Builder(this@Autorisation)
                            val inflater = layoutInflater
                            val dialogLayout = inflater.inflate(R.layout.alert_dialog_with_imageview, null)
                            builder.setPositiveButton(
                                "OK",
                                DialogInterface.OnClickListener { dialog, id ->
                                    pb.visibility=View.GONE
                                    val home = Intent(applicationContext, Code::class.java)
                                    startActivity(home)
                                    finish()
                                })
                            builder.setMessage("Demande accepté, appuyé sur OK pour avoir votre code")
                            builder.setView(dialogLayout)
                            builder.setCancelable(false)
                            builder.show()


                        } else {
                            Toast.makeText(applicationContext,
                                "erreur, veuillez réessayer" , Toast.LENGTH_LONG).show()

                            val builder = AlertDialog.Builder(this@Autorisation)
                            val inflater = layoutInflater
                            val dialogLayout1 = inflater.inflate(R.layout.alert_dialog_with_imageviewfalse, null)
                            builder.setPositiveButton(
                                "OK",
                                DialogInterface.OnClickListener { dialog, id ->
                                    pb.visibility=View.GONE

                                })
                            builder.setMessage("Désolé, vous pouvez plus bénéficiez d'un passe pour cette semaine, veuillez réessayer dans 7 jours")
                            builder.setView(dialogLayout1)
                            builder.setCancelable(false)
                            builder.show()
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {

                        pb.visibility = View.GONE
                        val dialogBuilder = AlertDialog.Builder(this@Autorisation)
                            //dialogBuilder.setMessage("")
                        Toast.makeText(applicationContext,
                            "erreur de connexion, veuillez réessayer" , Toast.LENGTH_LONG).show()

                        val builder = AlertDialog.Builder(this@Autorisation)
                        val inflater = layoutInflater
                        val dialogLayout1 = inflater.inflate(R.layout.alert_dialog_with_imageviewfalse, null)
                        builder.setPositiveButton(
                            "OK",
                            DialogInterface.OnClickListener { dialog, id ->
                                pb.visibility=View.GONE

                            })
                        builder.setMessage("erreur de connexion, veuillez réessayer")
                        builder.setView(dialogLayout1)
                        builder.setCancelable(false)
                        builder.show()

                    }
                });
            }

            }
        }
            }





