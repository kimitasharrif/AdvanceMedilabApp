package com.sherrif.mediclabapp.helpers

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.sherrif.mediclabapp.MyCart
import com.sherrif.mediclabapp.models.LabTests

//our class name is called SQLiteCartHelper and its going to accept 1 parameter called context
class SQLiteCartHelper(context : Context) : SQLiteOpenHelper(context, "cart1.db",null,1) {
    //    make context visible to other functions
    val context = context
    override fun onCreate(p0: SQLiteDatabase?) {
        // create a table if it does not exist
        val createtable = """
    CREATE TABLE IF NOT EXISTS cart(
        test_id INTEGER PRIMARY KEY AUTOINCREMENT,
        test_name VARCHAR,
        test_description TEXT,
        test_cost INTEGER,
        test_discount INTEGER,
        lab_id INTEGER
    )
""".trimIndent()

        p0?.execSQL(createtable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS cart")
        onCreate(p0)
    }//end of on upgrade

    //    insert/ Save to cart table
    fun insertData(
        test_id: String,
        test_name: String,
        test_description: String,
        test_cost: String,
        test_discount: String,
        lab_id: String
    ) {

        //ask permission to write our db
        val p0 = this.writableDatabase
//    select before insert to see if ID already exists
        val values = ContentValues()
        values.put("test_id", test_id)
        values.put("test_name", test_name)
        values.put("test_description", test_description)
        values.put("test_cost", test_cost)
        values.put("test_discount", test_discount)
        values.put("lab_id", lab_id)

//    save/ inset to cart table
        val result: Long = p0.insert("cart", null, values)
        if (result < 1) {
            println("Failed to Add")
            Toast.makeText(context, "Item Already in Cart", Toast.LENGTH_SHORT).show()
        } else {
            println("Item Added to Cart")
            Toast.makeText(context, "Item Added to Cart", Toast.LENGTH_SHORT).show()
        }//end of else statement
    }//end of insert data

    //check the number of item saved in our table
    fun getNumberOfItems(): Int {
//        get permission to read the db
        val p0 = this.readableDatabase
        val result: Cursor = p0.rawQuery("select* from cart", null)
        //return result count
        return result.count

    }//end of get all number of items
    //function to clear cart
    fun clearcart(){
        // get permission to write the database
        val p0 =this.writableDatabase
        p0.delete("cart",null,null)
        println("cart cleared")
        Toast.makeText(context, "cart cleared", Toast.LENGTH_SHORT).show()
        //reload
        val intent =Intent(context,MyCart::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }// end of clear cart

    // function to retrieve all record in cart
    //loop them in arraylist
    fun getAllItems():ArrayList<LabTests>{
        // get permission to write database
        val p0 =this.writableDatabase
        val items = ArrayList<LabTests>()
        //create a cursor for our results
        val result:Cursor =p0.rawQuery("select* from cart",null)
        // lets add all the rows into the items in our arraylist
        while (result.moveToNext()){
//            access the lab test model
            val model = LabTests()
            model.test_id = result.getString(0)//assume one row
            model.test_name = result.getString(1)
            model.test_description = result.getString(2)
            model.test_cost = result.getString(3)
            model.test_discount = result.getString(4)
            model.lab_id = result.getString(5)


            //add our model to arraylist
            items.add(model)
        }///end of while loop
        return items




    }// end of get all items

    // delete records by id..... this deletes one record at a time
//function to remove one item
    fun clearCartById(test_id: String){
        //write permission
        val p0 = this.writableDatabase

        //provide your testing id when deleting
        p0.delete("cart","test_id=?", arrayOf(test_id))
        println("item Id $test_id Removed")
        Toast.makeText(context, "item Id $test_id Removed", Toast.LENGTH_SHORT).show()
        // reload
    var intent = Intent(context,MyCart::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)

    }// end of clear cart by id

    //fun to get the total cost of cart items
    fun totalcost():Double{
//        permission to read database
        val p0 =this.readableDatabase
        val result:Cursor = p0.rawQuery("select test_cost from cart",null)

//    create a variable called total and asign it 0.0
        var total:Double = 0.0
        while (result.moveToNext()){
//        the cursor results return a list of test _cost
//        we loop through as we add the totals

            total += result.getDouble(0)
//        total = total + result.getDouble(0)


        }//end of while loop

//        Toast.makeText(context, "The total cost is $total", Toast.LENGTH_SHORT).show()
//    return the updated total

        return total
    }







}


