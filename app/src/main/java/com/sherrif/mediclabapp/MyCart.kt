package com.sherrif.mediclabapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.sherrif.mediclabapp.adapters.LabTestsCartAdapter
import com.sherrif.mediclabapp.helpers.PrefsHelper
import com.sherrif.mediclabapp.helpers.SQLiteCartHelper

class MyCart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_cart)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //fetch recycler view and check out button

        val recyclerview = findViewById<RecyclerView>(R.id.recycler)
        val total = findViewById<MaterialTextView>(R.id.total)
        val btncheckout = findViewById<MaterialButton>(R.id.checkout)


        btncheckout.setOnClickListener {
            //usings prefs check if token exists
            val token = PrefsHelper.getPrefs(applicationContext, "access_token")
//            Toast.makeText(applicationContext, "$token", Toast.LENGTH_SHORT).show()
            if (token.isEmpty()) {
//                token does not exists , not logged in
                Toast.makeText(applicationContext, "Not Logged In", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, SignupActivity::class.java))
                finish()

            } else {
//              token exist you are logged in and can proceed to checkout step 1
                Toast.makeText(applicationContext, "Logged In", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, CheckoutStep1::class.java)
                startActivity(intent)
                finish()
            }
        }

        //put total cost in a textview

        val helper = SQLiteCartHelper(applicationContext)


        //        clear all cart items
//        helper.clearcart()
//        get total from the helper
        total.text = "Total Cost" + helper.totalcost()

//        if total cost is zero and your cart is empty,hide the check out button
        if (helper.totalcost() == 0.0) {

            btncheckout.visibility = View.GONE


        }// end if
        // set the layout
        val layoutmanager = LinearLayoutManager(applicationContext)
        recyclerview.layoutManager = layoutmanager

        recyclerview.setHasFixedSize(true)
        //fi total items is zero show cart is empty

        if (helper.getNumberOfItems() == 0) {
            Toast.makeText(applicationContext, "Your cart is empty", Toast.LENGTH_LONG).show()
        } else {
            // access adapter and provide it with data using get all items
            val adapter = LabTestsCartAdapter(applicationContext)
            adapter.setListIteems(helper.getAllItems())// now we passed our data
            // link your adapetr to recycler
            recyclerview.adapter = adapter
        }
    }//end of on create

    // we want to make sure when the user clicks back button they are redirected to main activity
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        //this clears therunning/opens activities  and go back to main activity
        finishAffinity()
    }//end of onbackpressed

    //function that loads the option menu in the toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //load the cart xml
        menuInflater.inflate(R.menu.cart, menu)



        return super.onCreateOptionsMenu(menu)
    }

    //    function to make options menu clickable
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //clear cart items
        if (item.itemId == R.id.clearcart) {
            val helper = SQLiteCartHelper(applicationContext)
            helper.clearcart()
            Toast.makeText(applicationContext, "Clear Cart Item", Toast.LENGTH_SHORT).show()

        }//end of if
        //go back tolabs
        if (item.itemId == R.id.backtolabs) {
            startActivity(Intent(applicationContext, MainActivity::class.java))

        }

        return super.onOptionsItemSelected(item)
    }
}