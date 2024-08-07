package com.sherrif.mediclabapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.google.gson.GsonBuilder
import com.sherrif.mediclabapp.constants.Constants
import com.sherrif.mediclabapp.helpers.ApiHelper
import com.sherrif.mediclabapp.helpers.NetworkHelper
import com.sherrif.mediclabapp.models.Locations
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class SignupActivity : AppCompatActivity() {
    /// declare global
    private lateinit var buttonDatePicker: MaterialButton
    private lateinit var editTextDate: EditText
    private lateinit var spinner: Spinner
    private lateinit var selectedLocation: TextView
    private lateinit var locations: List<Locations>
    private lateinit var login: MaterialTextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
       if (NetworkHelper.checkForInternet(applicationContext)){
           // fetch the date
           editTextDate = findViewById(R.id.dateedittext)

           //FETCH THE BUTTON FOR DATE PICKER
           buttonDatePicker = findViewById(R.id.dob)
           //set on click listener for the button
           buttonDatePicker.setOnClickListener {
               //create a function to show date picker dialog
               showDatePickerDialog()
           }// end of date picker
           // fetch the spinner and textview
           spinner = findViewById(R.id.spinner)
           selectedLocation = findViewById(R.id.selecteditem)
           login = findViewById(R.id.linktologin)

           login.setOnClickListener {
               val intent = Intent(applicationContext, LoginActivity::class.java)
               startActivity(intent)
           }
           val surname = findViewById<TextInputEditText>(R.id.surname)
           val others = findViewById<TextInputEditText>(R.id.others)
           val email = findViewById<TextInputEditText>(R.id.email)
           val phone = findViewById<TextInputEditText>(R.id.phone)
           val password = findViewById<TextInputEditText>(R.id.password)
           val confirm = findViewById<TextInputEditText>(R.id.confirm)
           //outline red find layout id
           val surnameInputLayout = findViewById<TextInputLayout>(R.id.surname_input_layout)
           val othersInputLayout = findViewById<TextInputLayout>(R.id.others_input_layout)
           val emailInputLayout = findViewById<TextInputLayout>(R.id.email_input_layout)
           val phoneInputLayout = findViewById<TextInputLayout>(R.id.phone_input_layout)
           val passwordInputLayout = findViewById<TextInputLayout>(R.id.password_input_layout)
           val confirmInputLayout = findViewById<TextInputLayout>(R.id.confirm_input_layout)
           val choosegender = findViewById<RadioGroup>(R.id.gendergroup)
           val choosedob = findViewById<EditText>(R.id.dateedittext)
//        val yourlocation = findViewById<MaterialTextView>(R.id.chooselocation)


           //fetch locations and bring them here
           // create an instance of api helper
           val helper = ApiHelper(applicationContext)
           val api = Constants.BASE_URL + "/locations"
           val body = JSONObject()
           helper.post(api, body, object : ApiHelper.CallBack {
               override fun onSuccess(result: JSONArray?) {
//                 covert above result from JSONArray to List<Locations>
                   val locationgson = GsonBuilder().create()
                   locations =
                       locationgson.fromJson(result.toString(), Array<Locations>::class.java).toList()

                   //create a new list and map each loction object in the location list

                   //create a new list and map each location object in the location list

                   val locationsnames = locations.map {
                       it.location
                   }
                   //create an array adapter with application context, spinner layout item and list of location names
                   val adapter = ArrayAdapter(
                       applicationContext,
                       android.R.layout.simple_spinner_item,
                       locationsnames
                   )
                   //specify the layout to use when list of choices i.e,locations appear
                   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                   //set the adapter to the spinner
                   spinner.adapter = adapter

               }// end of on success

               override fun onSuccess(result: JSONObject?) {
               }

               override fun onFailure(result: String?) {
               }

           })//end of post locations
           //check the selected items and location
           var location_id = ""
           spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
               override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                   val selectedlocation = locations[p2]
                   location_id = selectedlocation.location_id
                   Toast.makeText(
                       applicationContext,
                       "${selectedlocation.location}",
                       Toast.LENGTH_SHORT
                   ).show()
               }

               override fun onNothingSelected(p0: AdapterView<*>?) {
                   Toast.makeText(applicationContext, "Please Select a location", Toast.LENGTH_SHORT)
                       .show()

               }

           }// end  of spinner

           //signup member
           val btncreate = findViewById<MaterialButton>(R.id.create)
           btncreate.setOnClickListener {
               var isValid = true

               if (surname.text.isNullOrEmpty()) {
                   surnameInputLayout.error = "Please insert your surname"
                   surnameInputLayout.setBoxStrokeColorStateList(
                       resources.getColorStateList(R.color.error_red, theme)
                   )
                   isValid = false
               } else {
                   surnameInputLayout.error = null
                   surnameInputLayout.setBoxStrokeColorStateList(
                       resources.getColorStateList(R.color.default_stroke, theme)
                   )
               }
               if (others.text.isNullOrEmpty()) {
                   othersInputLayout.error = "Please enter your other name"
                   othersInputLayout.setBoxStrokeColorStateList(
                       resources.getColorStateList(R.color.error_red, theme)
                   )
                   isValid = false
               } else {
                   othersInputLayout.error = null
                   othersInputLayout.setBoxStrokeColorStateList(
                       resources.getColorStateList(R.color.default_stroke, theme)
                   )
               }

               if (email.text.isNullOrEmpty()) {
                   emailInputLayout.error = "Please insert your email"
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
               if (phone.text.isNullOrEmpty()) {
                   phoneInputLayout.error = "Please insert your phone number"
                   phoneInputLayout.setBoxStrokeColorStateList(
                       resources.getColorStateList(R.color.error_red, theme)
                   )
                   isValid = false
               } else {
                   phoneInputLayout.error = null
                   phoneInputLayout.setBoxStrokeColorStateList(
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
               if (confirm.text.isNullOrEmpty()) {
                   confirmInputLayout.error = "Please insert your confirm password"
                   confirmInputLayout.setBoxStrokeColorStateList(
                       resources.getColorStateList(R.color.error_red, theme)
                   )
                   isValid = false
               } else {
                   confirmInputLayout.error = null
                   confirmInputLayout.setBoxStrokeColorStateList(
                       resources.getColorStateList(R.color.default_stroke, theme)
                   )
               }
               if (choosegender.checkedRadioButtonId == -1) {
                   choosegender.setBackgroundResource(R.drawable.error_border)
                   isValid = false
               } else {
                   choosegender.setBackgroundResource(R.drawable.default_border)
               }
               if (choosedob.text.isNullOrEmpty()) {
                   choosedob.backgroundTintList = resources.getColorStateList(R.color.error_red, theme)
                   isValid = false
               } else {
                   choosedob.backgroundTintList = resources.getColorStateList(R.color.default_stroke, theme)
               }
//            if (location_id.isNullOrEmpty()) {
//                yourlocation.setTextColor(resources.getColor(R.color.error_red, theme))
//                isValid = false
//            } else {
//                yourlocation.setTextColor(resources.getColor(R.color.default_text, theme))
//            }


               if (isValid) {
                   val surname = findViewById<TextInputEditText>(R.id.surname)
                   val others = findViewById<TextInputEditText>(R.id.others)
                   val email = findViewById<TextInputEditText>(R.id.email)
                   val phone = findViewById<TextInputEditText>(R.id.phone)
                   val password = findViewById<TextInputEditText>(R.id.password)
                   val confirm = findViewById<TextInputEditText>(R.id.confirm)
                   val female = findViewById<MaterialRadioButton>(R.id.radiofemale)
                   val male = findViewById<MaterialRadioButton>(R.id.radiomale)


//            check selected gender
                   var gender = "N/A"
                   if (female.isChecked) {
                       gender = "Female"
                   } else {
                       gender = "Male"
                   }
                   val uppercase = Regex(".*[A-Z].*")
                   val lowercase = Regex("[a-z]")
                   val digits = Regex(".*\\d.*")
                   val specialcharacter = Regex(".*[!@#\$%^&*(),.?\":{}|<>].*")


                   // check if password match
                   if (password.text.toString() != confirm.text.toString()) {
                       // password don't match
                       Toast.makeText(applicationContext, "Password don`t match", Toast.LENGTH_SHORT)
                           .show()
                   } else if (password.length() < 8) {
                       // password do
                       Toast.makeText(
                           applicationContext,
                           "Password must be atleast eight characters",
                           Toast.LENGTH_SHORT
                       ).show()
                   } else if (!uppercase.containsMatchIn(password.text.toString())) {
                       Toast.makeText(
                           applicationContext,
                           "Password must contain capital letter",
                           Toast.LENGTH_SHORT
                       ).show()
                   } else if (!lowercase.containsMatchIn(password.text.toString())) {
                       Toast.makeText(
                           applicationContext,
                           "Password must contain a small letter",
                           Toast.LENGTH_SHORT
                       ).show()

                   } else if (!digits.containsMatchIn(password.text.toString())) {
                       Toast.makeText(
                           applicationContext,
                           "Password must contain a digit/number",
                           Toast.LENGTH_SHORT
                       ).show()
                   } else if (!specialcharacter.containsMatchIn(password.text.toString())) {
                       Toast.makeText(
                           applicationContext,
                           "Password must contain a special character/symbol",
                           Toast.LENGTH_SHORT
                       ).show()

                   } else {
                       val helper = ApiHelper(applicationContext)
                       val api = Constants.BASE_URL + "/membersignup"
//                create a json object
                       val body = JSONObject()
                       body.put("surname", surname.text.toString())
                       body.put("others", others.text.toString())
                       body.put("email", email.text.toString())
                       body.put("phone", phone.text.toString())
                       body.put("dob", editTextDate.text.toString())
                       body.put("password", password.text.toString())
                       body.put("gender", gender)
                       body.put("status", "TRUE")
                       body.put("location_id", location_id)


                       helper.post(api, body, object : ApiHelper.CallBack {
                           override fun onSuccess(result: JSONArray?) {

                           }

                           override fun onSuccess(result: JSONObject?) {
                               // posted successifully
                               Toast.makeText(
                                   applicationContext,
                                   result.toString(),
                                   Toast.LENGTH_SHORT
                               ).show()
                           }

                           override fun onFailure(result: String?) {
                               // failed to post
                               Toast.makeText(
                                   applicationContext,
                                   result.toString(),
                                   Toast.LENGTH_SHORT
                               ).show()
                           }

                       })// end of on success


                   }


               }
           }

       }else{
           startActivity(Intent(applicationContext,NoInternetActivity::class.java))
           finish()
       }
        }
        private fun showDatePickerDialog() {
            // create an object of calendar
            val calendar = Calendar.getInstance()
            // create a date picker dialog and set the current date as the default selection
            val datePickerDialog = DatePickerDialog(
                this,
                { _: DatePicker, year: Int, month: Int, day: Int ->
                    val selectedDate = formatDate(year, month, day)
//                Toast.makeText(applicationContext, "$selectedDate", Toast.LENGTH_SHORT).show()
                    editTextDate.setText(selectedDate)

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            //PREVENT under i8 from registering
//        show date picker dialog, -2007
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 567648000000
            datePickerDialog.show()
        }

        private fun formatDate(year: Int, month: Int, day: Int): String {
            //date conversion
            //get an instance/object of a calendar
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
//        format date
            val dateformat = SimpleDateFormat("YYYY-MM-dd", Locale.getDefault())
            return dateformat.format(calendar.time)
        }
    }
