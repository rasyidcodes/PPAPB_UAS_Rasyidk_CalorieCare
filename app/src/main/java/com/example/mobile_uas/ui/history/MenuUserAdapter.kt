package com.example.mobile_uas.ui.history

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_uas.R
import com.example.mobile_uas.data.model.room.MenuUser
import com.example.myapplication.data.database.MenuUserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuUserAdapter(var menuUserList: List<MenuUser>, private val menuUserDao: MenuUserDAO) :
    RecyclerView.Adapter<MenuUserAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewFoodName: TextView = itemView.findViewById(R.id.item_History_FoodName)
        val textViewCalorie: TextView = itemView.findViewById(R.id.item_History_Calorie)
        val textViewDate : TextView = itemView.findViewById(R.id.item_History_Date)
        val textViewType : TextView = itemView.findViewById(R.id.item_History_Type)
        val textServing : TextView = itemView.findViewById(R.id.item_History_Serving)
        val btDelete : Button = itemView.findViewById(R.id.item_History_Delete)
        val btEdit : Button = itemView.findViewById(R.id.item_History_bt_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = menuUserList[position]

        holder.textViewType.text = currentItem.type
        holder.textViewFoodName.text = currentItem.foodName
        holder.textViewCalorie.text = "Calorie: ${currentItem.foodCalorie}"
        holder.textViewDate.text = currentItem.date
        holder.textServing.text = currentItem.serving.toString() + "g"


        holder.btEdit.setOnClickListener {
            val editDialogBuilder = AlertDialog.Builder(holder.itemView.context)
            editDialogBuilder.setTitle("Edit Makanan")

            val editDialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.edit_dialog, null)

            val editFoodName = editDialogView.findViewById<EditText>(R.id.editFoodName)
            val editFoodCalorie = editDialogView.findViewById<EditText>(R.id.editFoodCalorie)

            // Set the current values to the EditTexts
            editFoodName.setText(currentItem.foodName)
            editFoodCalorie.setText(currentItem.foodCalorie.toString())

            editDialogBuilder.setView(editDialogView)

            editDialogBuilder.setPositiveButton("Yes") { dialog, _ ->
                // User clicked Yes, validate and update the food information
                val newFoodName = editFoodName.text.toString().trim()
                val newFoodCalorieStr = editFoodCalorie.text.toString().trim()

                if (newFoodName.isNotEmpty() && newFoodCalorieStr.isNotEmpty()) {
                    // Validation passed, proceed with updating
                    val newFoodCalorie = newFoodCalorieStr.toInt()

                    CoroutineScope(Dispatchers.IO).launch {
                        // Update the MenuUser object with new values
                        val updatedMenuUser = currentItem.copy(foodName = newFoodName, foodCalorie = newFoodCalorie)

                        // Call the update method from the DAO
                        menuUserDao.update(updatedMenuUser)

                        withContext(Dispatchers.Main) {
                            // Optionally, show a toast message to indicate successful update
                            Toast.makeText(
                                holder.itemView.context,
                                "Item updated",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    dialog.dismiss()
                } else {
                    // Validation failed, show an error message or handle it as needed
                    Toast.makeText(
                        holder.itemView.context,
                        "Please enter valid values for food name and calorie",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            editDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                // User clicked Cancel, do nothing
                dialog.dismiss()
            }

            val editAlertDialog: AlertDialog = editDialogBuilder.create()
            editAlertDialog.show()
        }

        holder.btDelete.setOnClickListener{

            val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
            alertDialogBuilder.setTitle("Confirmation")
            alertDialogBuilder.setMessage("Are you sure you want to delete this item?")

            alertDialogBuilder.setPositiveButton("Yes") { dialog, _ ->
                // User clicked Yes, proceed with deletion
                CoroutineScope(Dispatchers.IO).launch {
                    // Call the delete method from the DAO
                    menuUserDao.delete(currentItem)


                    withContext(Dispatchers.Main) {


                        // Optionally, show a toast message to indicate successful deletion
                        Toast.makeText(
                            holder.itemView.context,
                            "Item deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                dialog.dismiss()
            }

            alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
                // User clicked No, do nothing
                dialog.dismiss()
            }

            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    fun updateData(newMenuUserList: List<MenuUser>) {
        menuUserList = newMenuUserList
        notifyDataSetChanged()
    }

    override fun getItemCount() = menuUserList.size
}
