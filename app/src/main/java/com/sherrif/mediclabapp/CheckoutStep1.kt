package com.sherrif.mediclabapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.radiobutton.MaterialRadioButton
import com.sherrif.mediclabapp.helpers.PrefsHelper
import java.util.Calendar
import java.util.Locale

class CheckoutStep1 : AppCompatActivity() {
    private lateinit var buttonDatePicker: MaterialButton
    private lateinit var editTextDate: EditText
    private lateinit var buttonTimePicker: MaterialButton
    private lateinit var editTextTime: EditText
    private lateinit var proceed2 :MaterialButton

//function to show time picker

    fun showTimePicker(){
        val calender = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            this,
            timeSetListener,
            calender.get(Calendar.HOUR_OF_DAY),
            calender.get(Calendar.MINUTE),
            false)
        timePickerDialog.show()
    }//end
    //This code Listens for time selections and place the time in editTextTime
    private val timeSetListener = TimePickerDialog.OnTimeSetListener{
            _, hourOfDay, minute ->


        val calendar = Calendar.getInstance() //***********
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val sdf = SimpleDateFormat("hh:mm", Locale.getDefault())
        val selectedTime = sdf.format(calendar.time)
        editTextTime.setText(selectedTime)
    }//end
//function to show date picker dialog
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
        val dateformat = java.text.SimpleDateFormat("YYYY-MM-dd", Locale.getDefault())
        return dateformat.format(calendar.time)
    }


override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checkout_step1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }//end of insets

    //click a button to trigger show ime picker

    buttonTimePicker = findViewById(R.id.buttonTimePicker)
    editTextTime = findViewById(R.id.editTextTime)
    buttonTimePicker.setOnClickListener {
        showTimePicker()
    }//end of time picker


    //function to show date picker
    buttonDatePicker = findViewById(R.id.buttonDatePicker)
    editTextDate = findViewById(R.id.editTextDate)
    buttonDatePicker.setOnClickListener {
        showDatePickerDialog()
    }//end of date picker dialog

//    fetch proceed button

    proceed2 = findViewById(R.id.proceedtostep2)
    proceed2.setOnClickListener {
        val time = editTextTime.text.toString()
        val date = editTextDate.text.toString()
        // check radio button home/hospital
        val home = findViewById<MaterialRadioButton>(R.id.radiohome)
        val hospital = findViewById<MaterialRadioButton>(R.id.radioaway)


        var where_taken = ""

        if (home.isChecked) {
            where_taken = "Home"
        }
        if (hospital.isChecked) {
            where_taken = "Hospital"
        }//end of if
//radio button sfor self or other
        val self = findViewById<MaterialRadioButton>(R.id.radioself)
        val other = findViewById<MaterialRadioButton>(R.id.radiother)

        var booked_for = ""

        if (self.isChecked) {
            booked_for = "Self"
        }
        if (other.isChecked) {
            booked_for = "Other"
        }//end of if
//        Toast.makeText(applicationContext, "$booked_for", Toast.LENGTH_SHORT).show()
// check for emptyList()
        if (date.isEmpty() || time.isEmpty() || where_taken.isEmpty() || booked_for.isEmpty()){
            Toast.makeText(applicationContext, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }else{
            //save values collected in checkoutt stepi to shared prefernces
            PrefsHelper.savePrefs(applicationContext,"date",date)
            PrefsHelper.savePrefs(applicationContext,"time",time)
            PrefsHelper.savePrefs(applicationContext,"where_taken",where_taken)
            PrefsHelper.savePrefs(applicationContext, "booked_for",booked_for)
            //check if GPS/lOCATION is enabled
            if (isLocationEnabled()){
//                return true meaninig the location is enabled
                //check if user checked self or other
                if (booked_for== "Self"){
//                for members ,we save the dependant id as empty in shared prefs since no dependant is needed
                    startActivity(Intent(applicationContext,CheckoutStep2::class.java))
                    PrefsHelper.savePrefs(applicationContext,"dependant_id","")
                }else{
//                for other direct a user to pick a dependant in Viewdependants activity
                    val intent = Intent(applicationContext,ViewDependants::class.java)
                    startActivity(intent)
                }

            }else{
//                return false meaning the gps location is not enabled
                Toast.makeText(applicationContext, "GPS IS OFF", Toast.LENGTH_SHORT).show()
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

            }



        }



//
    }


    }
    //end of oncreate
    //functionn to check if gps is on or off in your phone
    private fun isLocationEnabled() :Boolean{
//        get system service - which retrieves the locationmanager
        val locationmanager:LocationManager = getSystemService(Context.LOCATION_SERVICE)as LocationManager
        return locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)  ||
                locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}