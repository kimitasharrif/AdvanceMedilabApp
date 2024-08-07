package com.sherrif.mediclabapp.helpers

import android.content.Context

class PrefsHelper {
    //shared preferences are used in key value approach
    //we will use companion object
    companion object{
        //save to preferences
        fun savePrefs(context: Context, key:String,value:String){

            val preferences = context.getSharedPreferences("storage", Context.MODE_PRIVATE)
            val editor = preferences.edit()

            editor.putString(key, value)
            editor.apply()
        }//end of save function
        //get from preferences
        fun getPrefs(context: Context,key: String): String{
            val preferences = context.getSharedPreferences("storage", Context.MODE_PRIVATE)
            val input_name  = preferences.getString(key, "")
            return input_name.toString()

        }//end of get
        //remove an item from preference
        fun clearPrefsByKey(context: Context,key: String){
            val preferences = context.getSharedPreferences("storage", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.remove(key)
            editor.apply()
        }//end of clear by key
        //clear everything from storage
        //clear all items from preferences
        fun clearPrefs(context: Context){
            val preferences = context.getSharedPreferences("storage", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.clear()
            editor.apply()
        }//end of function clear all



    }
}