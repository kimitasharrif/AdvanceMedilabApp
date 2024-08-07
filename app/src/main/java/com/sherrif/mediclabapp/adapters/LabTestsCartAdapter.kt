package com.sherrif.mediclabapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.sherrif.mediclabapp.R
import com.sherrif.mediclabapp.models.LabTests

class LabTestsCartAdapter (var context :Context):RecyclerView.Adapter<LabTestsCartAdapter.ViewHolder>(){
    //create a list and connect it with our model
    var itemList:List<LabTests> = listOf()
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // acess the single_labtest_cart
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LabTestsCartAdapter.ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_labtest_cart, parent, false)
        return ViewHolder(view)



    }

    override fun onBindViewHolder(holder: LabTestsCartAdapter.ViewHolder, position: Int) {
        // fetch three textview sand one button
        val test_name = holder.itemView.findViewById<MaterialTextView>(R.id.test_name)
        val test_description = holder.itemView.findViewById<MaterialTextView>(R.id.test_description)
        val test_cost = holder.itemView.findViewById<MaterialTextView>(R.id.test_cost)
        val btnremove = holder.itemView.findViewById<MaterialButton>(R.id.remove)

        // assign one cart item
        val item = itemList[position]
        test_name.text = item.test_name
        test_description.text = item.test_description
        test_cost.text = item.test_cost+ "KES"
        btnremove.setOnClickListener{
            //to do later
//            val helper = SQLiteCartHelper(context)
//            helper.clearCartById()
        }
        Toast.makeText(context, "total cost is ${item.test_cost}", Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int {
        // count how many items we have in the cart
        return itemList.size
    }// end of get item count
    // earliier we mentioned itemlist nis empty we will get data from aoi and then bring it to below functio
    // the data you brings must follow the data inlab test model

    fun setListIteems(data:List<LabTests>){
        itemList = data// link the data to itemlist
        notifyDataSetChanged()
//        loaded
    }
}