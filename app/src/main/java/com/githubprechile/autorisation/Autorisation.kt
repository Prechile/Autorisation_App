package com.githubprechile.autorisation

import android.Manifest
import android.Manifest.permission.READ_PHONE_STATE
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
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
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_autorisation.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Autorisation : AppCompatActivity() {

    var dbHandler: DatabaseHandler? = null
    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autorisation)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        supportActionBar!!.title = "Autorisation couvre feu"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        val nom = findViewById<EditText>(R.id.nom)
        val prenom = findViewById<EditText>(R.id.prenom)
        val adresse = findViewById<EditText>(R.id.adresse)
        val adresse2 = findViewById<EditText>(R.id.adresse2)
        val imm = findViewById<EditText>(R.id.immatriculation)
        val motif = findViewById<Spinner>(R.id.motif)
        val lieu = findViewById<EditText>(R.id.nomLieu)
        val engin = findViewById<Spinner>(R.id.engin)
        val sexe = findViewById<Spinner>(R.id.sexe)
        val numpiece = findViewById<EditText>(R.id.numpiece)
        val btn = findViewById<Button>(R.id.btnValider)
        val pb = findViewById<ProgressBar>(R.id.pgbar)
        dbHandler = DatabaseHandler(this)
        pb.visibility = View.GONE


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


        val ListEngin = arrayOf("Véhicule", "Moto", "piéton")
        val adapterEngin =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ListEngin)
        engin.adapter = adapterEngin

        val ListSexe = arrayOf("Homme", "Femme", "Autre")
        val adapterSexe =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ListSexe)
        sexe.adapter = adapterSexe

        val ListMotif = arrayOf("Hôpital", "pharmacie", "clinique")
        val adapterMotif =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ListMotif)
        motif.adapter = adapterMotif

        val metier = Metier()
        var etat: String?
        val androidID: String = System.getString(this.contentResolver, Secure.ANDROID_ID)

        btn.setOnClickListener() {

            if (TextUtils.isEmpty(nom.text.toString()) && TextUtils.isEmpty(prenom.text.toString()) ||
                TextUtils.isEmpty(adresse.text.toString()) || TextUtils.isEmpty(imm.text.toString())
                || TextUtils.isEmpty(lieu.text.toString()) || TextUtils.isEmpty(numpiece.text.toString())
            ) {

                nom.error = "Entrer votre nom";
                prenom.error = "Entrer votre prénom"
                adresse.error = "saisir votre adresse"
                imm.error = "saisir une immatriculation"
                imm.error = "saisir le nom du lieu"
                numpiece.error = "saisir le numero de pièce d'identité"
                Toast.makeText(
                    applicationContext,
                    "veuillez renseigner tous mes champs", Toast.LENGTH_LONG
                ).show()

            } else {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        val latitude: Double = location.latitude
                        val longitude: Double = location.longitude


                        pb.visibility = View.VISIBLE
                        btn.isClickable = false

                        val personne = ApiAutorisation(
                            androidID, nom.text.toString(), lieu.text.toString(),
                            prenom.text.toString(), motif.selectedItem.toString(),
                            adresse.text.toString(), adresse2.text.toString(),
                            engin.selectedItem.toString(), imm.text.toString(), longitude, latitude,
                            numpiece.text.toString()
                        )

                        val retrofit = Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(UnsafeOK_HttpClient.getUnsafeOkHttpClient().build())
                            .build()

                        val service: Interface = retrofit.create(Interface::class.java)
                        val call: Call<String> = service.envoiePersonne(personne)

                        call.enqueue(object : Callback<String> {
                            override fun onResponse(
                                call: Call<String>,
                                response: Response<String?>
                            ) {
                                pb.visibility = View.GONE
                                response.body()

                                if (response.body() != null) {
                                    val code = response.body()
                                    dbHandler!!.addCode(code)
                                    dbHandler!!.insertUser(personne)

                                    val builder = AlertDialog.Builder(this@Autorisation)
                                    val inflater = layoutInflater
                                    val dialogLayout =
                                        inflater.inflate(R.layout.alert_dialog_with_imageview, null)
                                    builder.setPositiveButton(
                                        "OK",
                                        DialogInterface.OnClickListener { dialog, id ->
                                            pb.visibility = View.GONE
                                            val home = Intent(applicationContext, Code::class.java)
                                            startActivity(home)
                                            finish()
                                        })
                                    builder.setMessage("Demande accepté, appuyé sur OK pour avoir votre code")
                                    builder.setView(dialogLayout)
                                    builder.setCancelable(false)
                                    builder.show()


                                } else {
                                    btn.isClickable = true
                                    Toast.makeText(
                                        applicationContext,
                                        "erreur, veuillez réessayer", Toast.LENGTH_LONG
                                    ).show()

                                    val builder = AlertDialog.Builder(this@Autorisation)
                                    val inflater = layoutInflater
                                    val dialogLayout1 = inflater.inflate(
                                        R.layout.alert_dialog_with_imageviewfalse,
                                        null
                                    )
                                    builder.setPositiveButton(
                                        "OK",
                                        DialogInterface.OnClickListener { dialog, id ->
                                            pb.visibility = View.GONE

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
                                    .setCancelable(false)

                                    .setNegativeButton(
                                        "OK",
                                        DialogInterface.OnClickListener { dialog, id ->
                                            dialog.cancel()
                                            btn.isClickable = true
                                        })


                                val alert = dialogBuilder.create()
                                alert.setTitle("Erreur")
                                alert.setMessage("problème de connexion, veuillez réessayer")
                                alert.show()

                            }


                        });
                    }

                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        var latitude = location.latitude.toString()
                        var longitude = location.longitude.toString()
                    }
                }
            } else {
                Toast.makeText(this, "activer votre location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            var Lastlatitude = mLastLocation.latitude.toString()
            var Lastlongitude = mLastLocation.longitude.toString()
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


            }





