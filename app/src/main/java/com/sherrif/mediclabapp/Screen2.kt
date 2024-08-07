package com.sherrif.mediclabapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.sherrif.mediclabapp.ui.home.HomeFragment

class Screen2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_screen2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val skip =findViewById<MaterialTextView>(R.id.skip1)

        skip.setOnClickListener {
            val intent = Intent(applicationContext, HomeFragment::class.java)
            startActivity(intent)
            finish()
        }
//        // fetch button
        val fb =findViewById<FloatingActionButton>(R.id.fab)

        fb.setOnClickListener {
            val intent = Intent(applicationContext, Screen3::class.java)
            startActivity(intent)
        }




    }
}