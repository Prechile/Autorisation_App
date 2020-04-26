package com.githubprechile.autorisation

import android.app.ProgressDialog
import android.content.DialogInterface
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class webview : AppCompatActivity() {
lateinit var web:WebView
    private val TAG = "Main"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        supportActionBar!!.title="Information corona"

        val web = findViewById<WebView>(R.id.web)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)


        web.clearSslPreferences()
        val webSetting = web.settings
        webSetting.javaScriptEnabled=true
        web.webViewClient= WebViewClient()
        web.settings.domStorageEnabled = true // Add this
        web.settings.javaScriptCanOpenWindowsAutomatically = true

         var progressBar: ProgressDialog?
        val alertDialog: android.app.AlertDialog? = android.app.AlertDialog.Builder(this).create()
        progressBar = ProgressDialog.show(this, "ouverture", "patientez...")

        web.webViewClient = object : WebViewClient() {

            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                // DO NOT CALL SUPER METHOD
                super.onReceivedSslError(view, handler, error)

            }

//            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                Log.i(TAG, "Processing webview url click...")
//                view.loadUrl(url)
//                return true
//            }


            override fun onPageFinished(view: WebView, url: String) {
                Log.i(TAG, "Finished loading URL: $url")
                if (progressBar.isShowing) {
                    progressBar.dismiss()
                }
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Log.e(TAG, "Error: $description")
                Toast.makeText(applicationContext, "Oh no! $description", Toast.LENGTH_SHORT).show()
                alertDialog!!.setTitle("Error")
                alertDialog.setMessage(description)
                alertDialog.setButton("OK",
                    DialogInterface.OnClickListener { dialog, which -> return@OnClickListener })
                alertDialog.show()
            }
        }
        web.loadUrl("https://Covid19.gouv.tg")

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
