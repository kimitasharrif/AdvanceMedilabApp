package com.sherrif.mediclabapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.sherrif.mediclabapp.constants.Constants
import com.sherrif.mediclabapp.helpers.ApiHelper
import com.sherrif.mediclabapp.helpers.PrefsHelper
import com.sherrif.mediclabapp.helpers.SQLiteCartHelper
import org.json.JSONArray
import org.json.JSONObject

class Payment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val textpay = findViewById<MaterialTextView>(R.id.textpay)
        val phone = findViewById<TextInputEditText>(R.id.phone)
        val btnpay = findViewById<MaterialButton>(R.id.btnpay)
        //access the sqlitecarthelper
        val helper = SQLiteCartHelper(applicationContext)
        textpay.text= "Please Pay KES \n" + helper.totalcost()
        //btnpay onclick listener
        btnpay.setOnClickListener {
            if (phone.text?.isEmpty() == true){
                Toast.makeText(applicationContext, "Please enter phone +254.......", Toast.LENGTH_SHORT).show()

            }else{



                //access api helper
                val apihelper = ApiHelper(applicationContext)
                val api = Constants.BASE_URL +"/make_payment"
//create ajson object
                val body = JSONObject()
                // PUT THE Variables inside the body
                body.put("phone",phone.text.toString())
                body.put("amount",helper.totalcost() )
                //get invoiice no from shared preferences
                val invoice_no = PrefsHelper.getPrefs(applicationContext,"invoice_no")
                body.put("invoice_no",invoice_no)
                apihelper.post(api,body,object :ApiHelper.CallBack{
                    override fun onSuccess(result: JSONArray?) {

                    }

                    override fun onSuccess(result: JSONObject?) {
                        Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(result: String?) {
                        Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_SHORT).show()
                    }

                })//end of post

            }//end of else
        }//end of listener


    }
}