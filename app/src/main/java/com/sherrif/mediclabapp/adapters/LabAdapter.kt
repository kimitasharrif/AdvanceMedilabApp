package com.sherrif.mediclabapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sherrif.mediclabapp.LabTestsActivity
import com.sherrif.mediclabapp.MainActivity
import com.sherrif.mediclabapp.R
import com.sherrif.mediclabapp.models.Lab
import com.sherrif.mediclabapp.models.LabTests
import com.google.android.material.textview.MaterialTextView

// we provide context in below class to make it be an activity
class  LabAdapter (var context: Context):
    RecyclerView.Adapter<LabAdapter.ViewHolder>(){
        // name of our class is LabAdapter
        //Recycler view.adapter provided by android to work with recyclerview
        // Lab adapter.viewHolder:: it means adapter will work with view holder class named view holder
        // create a list and connect it with our model
        var itemList: List<Lab> = listOf()// its empty
        //var item lists means the  value can be changed
    // list of means there are no labs in the list initially
     //create a class which will hold our views in single_lab.xml
      //
       inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    //access single_lab.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //access/inflate the single_item.xml
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_lab ,parent, false)
        //pass the single lab to view holder
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // bind the data to viewss from the single_lab.xml
        //fid your three textviews in single_item
        val lab_name = holder.itemView.findViewById<MaterialTextView>(R.id.lab_name)
        val permit_id = holder.itemView.findViewById<MaterialTextView>(R.id.permit_id)
        val email =holder.itemView.findViewById<MaterialTextView>(R.id.email)
        // assume one lab, and bind data, it will loop all other labs
        val lab = itemList[position]
        lab_name.text = lab.lab_name
        permit_id.text = "Permit ID"+ lab.permit_id
        email.text =lab.email
        //when one lab is clicked move to lab test activity

        holder.itemView.setOnClickListener{
            // to navigate to each lab test on each lab click
            // we carry lab_id clicked with bundles(putextra)
            var id = lab.lab_id
            //pass this id along with intent
            val intent = Intent(context, LabTestsActivity::class.java)
            // we use key 1 to save it
            intent.putExtra("key1",id)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
              context.startActivity(intent)


        }


    }
//count the number of items
    override fun getItemCount(): Int {
        return itemList.size//count how many items in each list
    }
   //function filter data
    fun filterList(filterlist:List<Lab>){
        itemList = filterlist
       notifyDataSetChanged()
    }

//    Earlier we mention item list is empty
//we will get data from our api, the bring it to the below function
// the data you bring must follow the lab model
    fun setListItems(data: List<Lab>){
        itemList = data // link our data with the item list
         notifyDataSetChanged()
       // tell us the adapter class that our item list is loaded with data
    }
 }


