package com.sherrif.mediclabapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.GsonBuilder
import com.sherrif.mediclabapp.adapters.DependantAdapter
import com.sherrif.mediclabapp.constants.Constants
import com.sherrif.mediclabapp.helpers.ApiHelper
import com.sherrif.mediclabapp.helpers.PrefsHelper
import com.sherrif.mediclabapp.models.Dependant
import org.json.JSONArray
import org.json.JSONObject

//declare globals

class ViewDependants : AppCompatActivity() {
    //declare globals
    lateinit var recyclerview: RecyclerView
    lateinit var adapter: DependantAdapter
    lateinit var progress: ProgressBar
    lateinit var itemList: List<Dependant>
    lateinit var swiperrefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_dependants)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }//end of insets
        //        fetch recycler view and progressbar and swiperrefresh
        recyclerview = findViewById(R.id.recyclerview)
        progress = findViewById(R.id.progress)
        swiperrefresh = findViewById(R.id.swiperefresh)
        //link our adapter to our layout manager
        var layoutmanager = LinearLayoutManager(applicationContext)
        recyclerview.layoutManager = layoutmanager
        recyclerview.setHasFixedSize(true)
        adapter = DependantAdapter(applicationContext)
        recyclerview.adapter = adapter


        //function to get dependants
        get_dependants()
        swiperrefresh.setOnRefreshListener {
            get_dependants()
        }

    }

    private fun get_dependants() {
        val api = Constants.BASE_URL + "/viewdependants"
        //    helper
        val helper = ApiHelper(applicationContext)
//    Create a json object that will hold input values
        val body = JSONObject()
        //    provide the ID to the api //get memberid from prefshelper
        val member_id = PrefsHelper.getPrefs(applicationContext,"member_id")
        body.put("member_id",member_id)
        helper.post(api,body,object :ApiHelper.CallBack{
            override fun onSuccess(result: JSONArray?) {
                //convert to list array
                val gson = GsonBuilder().create()
                itemList = gson.fromJson(result.toString(),Array<Dependant>::class.java).toList()
                //                finally our adapter has data
                adapter.setListItems(itemList)
//                for the  sake of loopint,add the adapter to recycler
                recyclerview.adapter = adapter
                progress.visibility = View.GONE
                swiperrefresh.isRefreshing = false
                //                search
                val labsearch = findViewById<EditText>(R.id.search)
                labsearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }// end of before

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        filter(s.toString())
                    }//end of text change

                    override fun afterTextChanged(s: Editable?) {

                    }//end of after text changed
                })



            }

            override fun onSuccess(result: JSONObject?) {
                Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_SHORT).show()
                progress.visibility = View.GONE
            }

            override fun onFailure(result: String?) {
                progress.visibility = View.GONE
                Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_SHORT).show()

            }

        })



    }//  end of fun getdependants
    private fun filter(text: String) {
//        create a new arraylist to filter our data
        val filteredlist: ArrayList<Dependant> = ArrayList()

//    run a for loop to compare elements
        for (item in itemList) {
//        check if entered string matched with any item of our recycler view
            if (item.surname.lowercase().contains(text.lowercase()) || item.others.lowercase().contains(text.lowercase())) {
//            if the item is matched we are
//            adding it to our filtered list
                filteredlist.add(item)
            } //end of if statement


        } //end of for loop
        if (filteredlist.isEmpty()) {
//        if no item is added in filtered list
//        we are display a toast message as no data found
//        Toast.makeText(applicationContext, "No Data Found", Toast.LENGTH_SHORT).show()
            adapter.filterList(filteredlist)

        } else {
//        atleast we are passing filtered data
            adapter.filterList(filteredlist)
        }
    }
}