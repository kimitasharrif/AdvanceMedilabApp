package com.sherrif.mediclabapp.ui.dependants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sherrif.mediclabapp.databinding.FragmentDependantsBinding
import android.app.DatePickerDialog
import android.content.Intent
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.sherrif.mediclabapp.R
import com.sherrif.mediclabapp.ViewDependants
import com.sherrif.mediclabapp.constants.Constants
import com.sherrif.mediclabapp.helpers.ApiHelper
import com.sherrif.mediclabapp.helpers.PrefsHelper
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class   DependantsFragment : Fragment() {


    private var _binding: FragmentDependantsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


//        val notificationsViewModel =
//            ViewModelProvider(this).get(DependantsFragment::class.java)
                // Inflate the layout for this fragment
        _binding = FragmentDependantsBinding.inflate(inflater, container, false)
        val root = inflater.inflate(R.layout.fragment_dependants, container, false)

        val surname = root.findViewById<TextInputEditText>(R.id.surname)
        val others = root.findViewById<TextInputEditText>(R.id.others)
        val male = root.findViewById<RadioButton>(R.id.radiomale)
        val female = root.findViewById<RadioButton>(R.id.radiofemale)
        val dob = root.findViewById<MaterialButton>(R.id.dob)
        val editText = root.findViewById<EditText>(R.id.dateedittext)
        val dependant = root.findViewById<MaterialButton>(R.id.add_dependant)
        val viewdependants = root.findViewById<MaterialButton>(R.id.mydependants)
        val viewbookings = root.findViewById<MaterialButton>(R.id.mybookings)
        viewdependants.setOnClickListener {
//            intent to view dependant
            startActivity(Intent(requireContext(), ViewDependants::class.java))
        }
        viewbookings.setOnClickListener{
            //intent to my booking
//            startActivity(Intent(requireContext(), viewbookings::class.java))
        }
                // Check selected gender
        var gender = "N/A"
        if (female.isChecked) {
            gender = "Female"
        }
        if (male.isChecked) {
            gender = "Male"
        }

        // Set onClickListener for date of birth button
        dob.setOnClickListener {
            showDatePickerDialog(editText)
        }

        // Example API call using ApiHelper
        val helper = ApiHelper(requireContext())
        val api = Constants.BASE_URL + "/adddependant"

        // Example of creating a JSON object for API call
        dependant.setOnClickListener {
            val body = JSONObject()
            body.put("surname", surname.text.toString())
            body.put("others", others.text.toString())
            body.put("dob", editText.text.toString())

            val member_id = PrefsHelper.getPrefs(requireContext(), "member_id")
            body.put("member_id", member_id)



            // Make API call
            helper.post(api, body, object : ApiHelper.CallBack {
                override fun onSuccess(result: JSONArray?) {
                    // Handle success
                }

                override fun onSuccess(result: JSONObject?) {
                    // Handle success
                    Toast.makeText(requireContext(), result.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(result: String?) {
                    // Handle failure
                    Toast.makeText(requireContext(), result.toString(), Toast.LENGTH_SHORT).show()
                }
            })

        }





        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
        private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = formatDate(year, month, dayOfMonth)
                editText.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Set max date (e.g., 18 years ago from current date)
        calendar.add(Calendar.YEAR, -18)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    private fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }


}




