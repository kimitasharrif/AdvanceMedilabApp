package com.sherrif.mediclabapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.sherrif.mediclabapp.constants.Constants
import com.sherrif.mediclabapp.helpers.ApiHelper
import com.sherrif.mediclabapp.helpers.NetworkHelper
import com.sherrif.mediclabapp.helpers.PrefsHelper
import org.json.JSONArray
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
   if (NetworkHelper.checkForInternet(applicationContext)){
       //        link to signup
       val signup = findViewById<MaterialTextView>(R.id.linktosignup)
       signup.setOnClickListener {
           startActivity(Intent(applicationContext, SignupActivity::class.java))

       }//end of signup

//fetch email, password and login
       val email = findViewById<TextInputEditText>(R.id.email)
       val password = findViewById<TextInputEditText>(R.id.password)
       val btnlogin = findViewById<MaterialButton>(R.id.login)

       val emailInputLayout = findViewById<TextInputLayout>(R.id.email_input_layout)
       val passwordInputLayout = findViewById<TextInputLayout>(R.id.ppassword_input_layout)



       btnlogin.setOnClickListener {
           var isValid = true

           if (email.text.isNullOrEmpty()) {
               emailInputLayout.error = "Please insert your Email"
               emailInputLayout.setBoxStrokeColorStateList(
                   resources.getColorStateList(R.color.error_red, theme)
               )
               isValid = false
           } else {
               emailInputLayout.error = null
               emailInputLayout.setBoxStrokeColorStateList(
                   resources.getColorStateList(R.color.default_stroke, theme)
               )
           }

           if (password.text.isNullOrEmpty()) {
               passwordInputLayout.error = "Please insert your password"
               passwordInputLayout.setBoxStrokeColorStateList(
                   resources.getColorStateList(R.color.error_red, theme)
               )
               isValid = false
           } else {
               passwordInputLayout.error = null
               passwordInputLayout.setBoxStrokeColorStateList(
                   resources.getColorStateList(R.color.default_stroke, theme)
               )
           }

           if (isValid) {
               //specify your member sign in endpoint
               val api = Constants.BASE_URL + "/membersignin"
               //specify apihelper object
               val helper = ApiHelper(applicationContext)
               //create a json object of email and password
               val body = JSONObject()
               body.put("email", email.text.toString())
               body.put("password", password.text.toString())
               //post
               helper.post(api, body,object : ApiHelper.CallBack{
                   override fun onSuccess(result: JSONArray?) {

                   }

                   override fun onSuccess(result: JSONObject?) {
                       //Check if access_token exist in response
                       if (result!!.has("access_token")){
                           //Access token found means login success
                           //access the   token and the member from the JSON returned
                           val access_token = result.getString("access_token")
                           val member = result.getString("message")
                           //toast a success message
                           Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()
                           //save the access token to shared preference
                           PrefsHelper.savePrefs(applicationContext, "access_token", access_token)
                           //convert member to an object
                           val memberObject = JSONObject(member)
                           val member_id = memberObject.getString("member_id")
                           val memberemail = memberObject.getString("email")
                           val surname = memberObject.getString("surname")
                           // save member_id to shared prefs
                           PrefsHelper.savePrefs(applicationContext,"member_id",member_id)
                           //save member email to shared prefs
                           PrefsHelper.savePrefs(applicationContext,"email",memberemail)
                           //save surname to shared prefs
                           PrefsHelper.savePrefs(applicationContext,"surname",surname)
                           //save member to shared prefs
                           PrefsHelper.savePrefs(applicationContext,"userObject",member)
//                            Toast.makeText(applicationContext, "$access_token", Toast.LENGTH_SHORT).show()
                           // redirect to main activity upon successful login
                           startActivity(Intent(applicationContext,MainActivity::class.java))
                           finishAffinity()
                       }else{
                           //No access toke found, Login Failed
                           Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_SHORT).show()
                       }
                   }

                   override fun onFailure(result: String?) {
                   }

               })

           }

   }

   }else{
       startActivity(Intent(applicationContext,NoInternetActivity::class.java))
       finish()
   }

    }
}