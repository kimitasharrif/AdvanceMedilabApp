package com.sherrif.mediclabapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.GsonBuilder
import com.sherrif.mediclabapp.adapters.LabTestsAdapter
import com.sherrif.mediclabapp.constants.Constants
import com.sherrif.mediclabapp.helpers.ApiHelper
import com.sherrif.mediclabapp.helpers.NetworkHelper
import com.sherrif.mediclabapp.models.LabTests
import org.json.JSONArray
import org.json.JSONObject

class LabTestsActivity : AppCompatActivity() {
    //declare globals

    lateinit var recyclerview: RecyclerView
    lateinit var adapter: LabTestsAdapter
    lateinit var progress: ProgressBar
    lateinit var itemList: List<LabTests>
    lateinit var swiperrefresh: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lab_tests)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }//end

     if(NetworkHelper.checkForInternet(applicationContext)){
         //        fetch recycler view and progressbar and swiperrefresh
         recyclerview = findViewById(R.id.recyclerview)
         progress = findViewById(R.id.progress)
         swiperrefresh = findViewById(R.id.swiperefresh)

         var layoutmanager = LinearLayoutManager(applicationContext)
         recyclerview.layoutManager = layoutmanager
         recyclerview.setHasFixedSize(true)
         adapter = LabTestsAdapter(applicationContext)
         recyclerview.adapter = adapter

         getlabTest()
         //        find the swiper

         swiperrefresh.setOnRefreshListener {
             getlabTest() // fetch data again
         }

     }else{
         startActivity(Intent(applicationContext,NoInternetActivity::class.java))
         finish()
     }
    }//end of oncreate

    //    function to get lab tests but we use POST METHOD
    fun getlabTest() {
        val api = Constants.BASE_URL + "/labtests"
//    helper
        val helper = ApiHelper(applicationContext)
//    Create a json object that will hold input values
        val body = JSONObject()
//    Retrieve the id from Intent Extras- We will add this ID to bundle extras later
        val id = intent.extras?.getString("key1")
//    provide the ID to the api
        body.put("lab_id", id)
        helper.post(api, body, object : ApiHelper.CallBack {
            override fun onSuccess(result: JSONArray?) {
//            covert above result from JSONArray to List<LabTest>
                val labtestgson = GsonBuilder().create()
                itemList =
                    labtestgson.fromJson(result.toString(), Array<LabTests>::class.java).toList()

//            finally our adapter has data
                adapter.setListItems(itemList)
//            for the  sake of loopint,add the adapter to recycler
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
                progress.visibility = View.GONE

            }

            override fun onFailure(result: String?) {
                progress.visibility = View.GONE
                Log.d("failureerrors", result.toString())

            }

        })
    }//end of fetch data

    //    function to filter data
//    private function
    private fun filter(text: String) {
//        create a new arraylist to filter our data
        val filteredlist: ArrayList<LabTests> = ArrayList()

//    run a for loop to compare elements
        for (item in itemList) {
//        check if entered string matched with any item of our recycler view
            if (item.test_name.lowercase().contains(text.lowercase())) {
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