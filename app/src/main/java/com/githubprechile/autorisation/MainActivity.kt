package com.githubprechile.autorisation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //toolbar.inflateMenu(R.menu.main)

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

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

   override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.info_corona -> {

             val mweb = Intent(this,webview::class.java)
                startActivity(mweb)
            }

            R.id.pass_cfeu -> {

                val cfeu = Intent(this,Autorisation::class.java)
                startActivity(cfeu)
            }

            R.id.info -> {

                val info = Intent(this,infoApp::class.java)
                startActivity(info)
            }

            R.id.share -> {

                val share = Intent(Intent.ACTION_SEND)
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT,"Milego");
                val message= "Télécharger l'application en cliquant sur le lien \n\n";
                share.putExtra(Intent.EXTRA_TEXT,message);
                startActivity(Intent.createChooser(share,"Partager"));
            }
        }


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}



