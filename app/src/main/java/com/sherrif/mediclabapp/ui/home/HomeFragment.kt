package com.sherrif.mediclabapp.ui.home


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.gson.GsonBuilder
import com.sherrif.mediclabapp.helpers.SQLiteCartHelper
import com.sherrif.mediclabapp.LoginActivity
import com.sherrif.mediclabapp.MainActivity
import com.sherrif.mediclabapp.MyCart
import com.sherrif.mediclabapp.NoInternetActivity
import com.sherrif.mediclabapp.R
import com.sherrif.mediclabapp.adapters.LabAdapter
import com.sherrif.mediclabapp.constants.Constants
import com.sherrif.mediclabapp.databinding.FragmentHomeBinding
import com.sherrif.mediclabapp.helpers.ApiHelper
import com.sherrif.mediclabapp.helpers.NetworkHelper
import com.sherrif.mediclabapp.helpers.PrefsHelper

import com.sherrif.mediclabapp.models.Lab
import com.sherrif.mediclabapp.ui.profile.ProfileFragment
import org.json.JSONArray
import org.json.JSONObject

class HomeFragment : Fragment() {







    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: LabAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var itemList: List<Lab>
    private lateinit var swiper: SwipeRefreshLayout
    private lateinit var handler: Handler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize views
        recyclerview = root.findViewById(R.id.recyclerview)
        progressBar = root.findViewById(R.id.progress)
        swiper = root.findViewById(R.id.swiperefresh)
        handler = Handler(Looper.getMainLooper())

        setupView(root)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (NetworkHelper.checkForInternet(requireContext())) {
            updateViews(view)
            simulateProgress()

            val layoutManager = LinearLayoutManager(requireContext())
            recyclerview.layoutManager = layoutManager
            recyclerview.setHasFixedSize(true)

            adapter = LabAdapter(requireContext())
            recyclerview.adapter = adapter

            fetchData()
            swiper.setOnRefreshListener {
                fetchData()
            }
        } else {
            startActivity(Intent(requireContext(), NoInternetActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun setupView(root: View) {
        root.findViewById<View>(R.id.main)?.let { mainView ->
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }


    }

    private fun updateViews(root: View) {
        val user = root.findViewById<MaterialTextView>(R.id.user)
        val signin = root.findViewById<MaterialButton>(R.id.signin)
        val signout = root.findViewById<MaterialButton>(R.id.signout)
        val profile = root.findViewById<MaterialButton>(R.id.profile)

        signin.visibility = View.GONE
        signout.visibility = View.GONE
        profile.visibility = View.GONE

        val token = PrefsHelper.getPrefs(requireContext(), "access_token")
        if (token.isEmpty()) {
            user.text = "Not Logged In"
            signin.visibility = View.VISIBLE
            signin.setOnClickListener {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        } else {
            profile.visibility = View.VISIBLE
            profile.setOnClickListener {
                startActivity(Intent(requireContext(), ProfileFragment::class.java))
            }
            signout.visibility = View.VISIBLE
            val surname = PrefsHelper.getPrefs(requireContext(), "surname")
            user.text = "Welcome $surname"
            signout.setOnClickListener {
                PrefsHelper.clearPrefs(requireContext())
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finishAffinity()
            }
        }
    }

    private fun fetchData() {
        val api = Constants.BASE_URL + "/laboratories"
        val helper = ApiHelper(requireContext())
        helper.get(api, object : ApiHelper.CallBack {
            override fun onSuccess(result: JSONArray?) {
                val labjson = GsonBuilder().create()
                itemList = labjson.fromJson(result.toString(), Array<Lab>::class.java).toList()
                adapter.setListItems(itemList)
                progressBar.visibility = View.GONE
                swiper.isRefreshing = false

                // Ensure recyclerview is updated with the adapter
                recyclerview.adapter = adapter

                val labsearch = requireView().findViewById<EditText>(R.id.search)
                labsearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        filter(p0.toString())
                    }

                    override fun afterTextChanged(p0: Editable?) {}
                })
            }

            override fun onSuccess(result: JSONObject?) {
                // Handle JSONObject response if needed
            }

            override fun onFailure(result: String?) {
                Toast.makeText(requireContext(), "Error: $result", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
        })
    }


    private fun filter(text: String) {
        val filteredList: ArrayList<Lab> = ArrayList()
        for (item in itemList) {
            if (item.lab_name.lowercase().contains(text.lowercase())) {
                filteredList.add(item)
            }
        }
        adapter.filterList(filteredList)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        val item: MenuItem = menu.findItem(R.id.mycart)
        item.setActionView(R.layout.design)
        val actionView: View? = item.actionView
        val image = actionView?.findViewById<ImageView>(R.id.image)
        val badge = actionView?.findViewById<TextView>(R.id.badge)
        image?.setOnClickListener {
            val intent = Intent(requireContext(), MyCart::class.java)
            startActivity(intent)
        }
        val helper = SQLiteCartHelper(requireContext())
        badge?.text = "" + helper.getNumberOfItems()
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun simulateProgress() {
        var progress = 0
        val maxProgress = 100
        Thread {
            while (progress <= maxProgress) {
                progress += 1
                handler.post {
                    progressBar.progress = progress
                }
                Thread.sleep(50)
            }
        }.start()
    }
}
