package com.sherrif.mediclabapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.sherrif.mediclabapp.helpers.NetworkHelper

class Screen1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_screen1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //check fo rinternet connection
        if (NetworkHelper.checkForInternet(applicationContext)){
            //you have internet access
            ////        fetch skip 1 text view
            val skip =findViewById<MaterialTextView>(R.id.skip1)

            skip.setOnClickListener {
                val intent = Intent(applicationContext, SignupActivity::class.java)
                startActivity(intent)
                finish()
            }
//        // fetch button
            val fb =findViewById<FloatingActionButton>(R.id.fab)

            fb.setOnClickListener {
                val intent = Intent(applicationContext, Screen2::class.java)
                startActivity(intent)
            }
        }else{
            //you have no internet access
            val intent = Intent(applicationContext, NoInternetActivity::class.java)
            startActivity(intent)
            finish()
//
//
        }


    }
}