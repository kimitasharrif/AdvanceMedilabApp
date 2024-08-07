package com.sherrif.mediclabapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.sherrif.mediclabapp.MyCart
import com.sherrif.mediclabapp.R
import com.sherrif.mediclabapp.ViewDependants
import com.sherrif.mediclabapp.databinding.FragmentProfileBinding
import com.sherrif.mediclabapp.helpers.PrefsHelper
import org.json.JSONObject

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

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

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //        fetch the textviews
        val surname : MaterialTextView = binding.surname
        val others : MaterialTextView = binding.others
        val email : MaterialTextView = binding.email
        val phone : MaterialTextView = binding.phone
        val gender : MaterialTextView = binding.gender
        val dob : MaterialTextView = binding.dob


        //get member from shared preferences using member userObject key
        val member = PrefsHelper.getPrefs(requireContext(),"userObject")
        //convert to json object
        val user = JSONObject(member)
        //get surname
        surname.text = user.getString("surname")
        others.text = user.getString("others")
        email.text = user.getString("email")
        phone.text = user.getString("phone")
        gender.text = user.getString("gender")
        dob.text = user.getString("dob")


        //        my buttons
        val mycart : MaterialButton =binding.mycart
        val viewbookings : MaterialButton = binding.mybookings
        mycart.setOnClickListener {
//            intent to view dependant
            startActivity(Intent(requireContext(), MyCart::class.java))
        }
//        viewbookings.setOnClickListener{
//            //intent to my booking
////            startActivity(Intent(requireContext(), viewbookings::class.java))
//        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}