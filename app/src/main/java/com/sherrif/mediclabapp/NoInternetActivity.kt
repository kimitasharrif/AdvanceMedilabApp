package com.sherrif.mediclabapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textview.MaterialTextView
import com.sherrif.mediclabapp.ui.home.HomeFragment


class NoInternetActivity : AppCompatActivity() {
 

    private lateinit var btnOpenSettings: Button
    private lateinit var refreshnetwork: Button
    private lateinit var cancel: MaterialTextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_no_internet)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

         cancel = findViewById(R.id.btncancel)
        btnOpenSettings = findViewById(R.id.btnopenwifiset)
        refreshnetwork =findViewById(R.id.refreshnetwork)

        //set cancel onclick listener
        cancel.setOnClickListener{
            startActivity(Intent(applicationContext,MainActivity::class.java))
        }


        // Open Wi-Fi settings when the user taps on the button
        btnOpenSettings.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }

    }


}