package com.sherrif.mediclabapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sherrif.mediclabapp.LabTestsActivity
import com.sherrif.mediclabapp.R
import com.sherrif.mediclabapp.SingleLabTest
import com.sherrif.mediclabapp.models.LabTests
import com.google.android.material.textview.MaterialTextView

class LabTestsAdapter (var context: Context):
  RecyclerView.Adapter<LabTestsAdapter.ViewHolder>()  {
      //create a list and connect it with our model
    var itemList: List<LabTests> = listOf() //its empty=(" inteview perspect")  ::::meanns to inherit
// create aa view holder class which holds our views insingle_labtest
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
//access or inflate the labtests.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_labtests, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //find 3 textviews
        val test_name = holder.itemView.findViewById<MaterialTextView>(R.id.test_name)
        val test_description = holder.itemView.findViewById<MaterialTextView>(R.id.test_description)
        val test_cost = holder.itemView.findViewById<MaterialTextView>(R.id.test_cost)
// asssume one labtests
        val item = itemList[position]
        test_name.text = item.test_name
        test_description.text = item.test_description
        test_cost.text = item.test_cost+ "KES"



        holder.itemView.setOnClickListener{
            // navigate to the single lab activity
//            var id = item.test_name
            //pass this id along with intent
            val intent = Intent(context, SingleLabTest::class.java)
            // we use key 1 to save it
            intent.putExtra("lab_id",item.lab_id)
            intent.putExtra("test_id",item.test_id)
            intent.putExtra("test_name",item.test_name)
            intent.putExtra("test_description",item.test_description)
            intent.putExtra("test_discount",item.test_discount)
            intent.putExtra("test_cost",item.test_cost)
            intent.putExtra("reg_date",item.reg_date)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }//end
    }

    override fun getItemCount(): Int {
        //count the number of labtest items
        return itemList.size
    }
 //you need to filter data
    fun filterList(filterList:List<LabTests>){
        itemList = filterList
     notifyDataSetChanged()
    }
    fun setListItems(data: List<LabTests>) {
        itemList = data // link our data with the item list
        notifyDataSetChanged()
        // tell this adapter class that now our itemlist is now loaded with data

    }

}