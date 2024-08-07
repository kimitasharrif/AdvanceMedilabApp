package com.sherrif.mediclabapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.sherrif.mediclabapp.CheckoutStep2
import com.sherrif.mediclabapp.R
import com.sherrif.mediclabapp.helpers.PrefsHelper
import com.sherrif.mediclabapp.models.Dependant

class DependantAdapter(var context: Context):RecyclerView.Adapter<DependantAdapter.ViewHolder>() {
    //create a list and connect it with our model
    var itemList :List<Dependant> = listOf()// its empty

    //create a class that will hold our views in single_dependants
    inner class ViewHolder (itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //inflate
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_dependant, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //fetch the textviews
        val dep_name = holder.itemView.findViewById<MaterialTextView>(R.id.dep_surname)
        val dep_other = holder.itemView.findViewById<MaterialTextView>(R.id.dep_others)
        val dep_dob = holder.itemView.findViewById<MaterialTextView>(R.id.dep_dob)

        //assume one dependant
        val dependant = itemList[position]
        dep_name.text = dependant.surname
        dep_other.text = dependant.others
        dep_dob.text = dependant.dob


        holder.itemView.setOnClickListener{
//            Toast.makeText(context, "${dependant.dependant_id}", Toast.LENGTH_SHORT).show()
            PrefsHelper.savePrefs(context,"dependant_id",dependant.dependant_id)
            val intent = Intent(context,CheckoutStep2::class.java)
            intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }


    } //end of bid


    //bring the data to the dependant model
    fun setListItems(data: List<Dependant>){
        //mmap or link data to  itemlist
        itemList = data
        //tell the adapter that now item is loaded with data
        notifyDataSetChanged()
    }//end of fun setList items
    override fun getItemCount(): Int {
        //count how many  items int he list
        return itemList.size

    }//end of getitemcount
    //you need to filter data
    fun filterList(filterList: ArrayList<Dependant>){
        itemList = filterList
        notifyDataSetChanged()
    }
}