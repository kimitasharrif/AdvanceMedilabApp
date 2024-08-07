package com.sherrif.mediclabapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.sherrif.mediclabapp.helpers.NetworkHelper
import com.sherrif.mediclabapp.helpers.SQLiteCartHelper

class SingleLabTest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_single_labtest)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (NetworkHelper.checkForInternet(applicationContext)){
            val testname = findViewById<MaterialTextView>(R.id.test_name)
            val testdescription = findViewById<MaterialTextView>(R.id.test_description)
            val testdiscount = findViewById<MaterialTextView>(R.id.test_discount)
            val testcost = findViewById<MaterialTextView>(R.id.test_cost)

            val cartbtn = findViewById<MaterialButton>(R.id.addtocart)

            // Retrieve data from intent extras
            val lab_id = intent.extras?.getString("lab_id")
            val test_id = intent.extras?.getString("test_id")
            val test_name = intent.extras?.getString("test_name")
            val test_description = intent.extras?.getString("test_description")
            val test_discount = intent.extras?.getString("test_discount")
            val test_cost = intent.extras?.getString("test_cost")


            testname.text = test_name
            testdescription.text = test_description
            testdiscount.text = test_discount + "% OFF"
            testcost.text = "$test_cost KES"


            cartbtn.setOnClickListener {
                //call our class called SQLCart helper
                val helper = SQLiteCartHelper(applicationContext)
                try {
                    helper.insertData(
                        test_id!!,
                        test_name!!,
                        test_description!!,
                        test_cost!!,
                        test_discount!!,
                        lab_id!!
                    )
//
                }   // end of try
                catch (e: Exception) {
                    Toast.makeText(applicationContext, "An error Occurred", Toast.LENGTH_SHORT).show()

                }   //end of catch
            }//end of on click listener
            // test our item count inside our cart
            val helper = SQLiteCartHelper(applicationContext)
            val count = helper.getNumberOfItems()
            Toast.makeText(applicationContext, "Item count is $count", Toast.LENGTH_SHORT).show()


//get all the items
            val items = helper.getAllItems()
            for (item in items) {
//                Toast.makeText(applicationContext, "${item.test_id}", Toast.LENGTH_SHORT).show()
//
//        }//end of for loop
//        helper.clearCartById("2")
                helper.totalcost()


                //e are forced


            }

        }else{
            startActivity(Intent(applicationContext,NoInternetActivity::class.java))
            finish()
        }




    }
}