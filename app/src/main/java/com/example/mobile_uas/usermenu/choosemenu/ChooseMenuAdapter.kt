package com.example.mobile_uas.usermenu.choosemenu

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_uas.BottomNavigationActivity
import com.example.mobile_uas.R
import com.example.mobile_uas.admin.AdminEditFoodActivity
import com.example.mobile_uas.data.model.firestore.MenuAdminFS
import com.google.firebase.firestore.FirebaseFirestore

class ChooseMenuAdapter : RecyclerView.Adapter<ChooseMenuAdapter.ChooseMenuViewHolder>() {

    private var makanan: List<MenuAdminFS> = listOf()
    private val firestore = FirebaseFirestore.getInstance()
    private val makananCollection = firestore.collection("makanan")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseMenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_choosemenu, parent, false)
        return ChooseMenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChooseMenuViewHolder, position: Int) {
        val currentMakanan = makanan[position]

        holder.textViewBuku.text = currentMakanan.foodName
        holder.textViewCalorie.text = currentMakanan.foodCalorie.toString() + " Cal"

        holder.btChoose.setOnClickListener{
            try {
                val intent = Intent(holder.itemView.context, AddChooseMenuActivity::class.java)
                intent.putExtra("foodName", currentMakanan.foodName)
                intent.putExtra("foodCalorie", currentMakanan.foodCalorie)
                holder.itemView.context.startActivity(intent)
            }catch (e: Exception){
                showToast(e.toString(),holder)
                Log.d("ERR", e.toString())
            }
        }



    }

    private fun deleteMakanan(id: String, holder: ChooseMenuViewHolder) {
        makananCollection.document(id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(
                    holder.itemView.context,
                    "Makanan berhasil dihapus",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    holder.itemView.context,
                    "Error deleting document: $e",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun getItemCount(): Int {
        return makanan.size
    }

    fun setMakanan(makanan: List<MenuAdminFS>, searchQuery: String = "") {
        this.makanan = if (searchQuery.isNotEmpty()) {
            makanan.filter { it.foodName.contains(searchQuery, ignoreCase = true) }
        } else {
            makanan
        }

        notifyDataSetChanged()
    }

    inner class ChooseMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewBuku: TextView = itemView.findViewById(R.id.item_choose_FoodName)
        val textViewCalorie: TextView = itemView.findViewById(R.id.item_choose_Calorie)
        val  btChoose : Button = itemView.findViewById(R.id.item_choose_btChoose)

    }

    fun showYesNoAlertDialog(
        context: Context,
        message: String,
        onYesClickListener: DialogInterface.OnClickListener
    ) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton("Yes", onYesClickListener)
        alertDialogBuilder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showToast(message: String, holder: ChooseMenuViewHolder) {
        Toast.makeText(holder.itemView.context, message, Toast.LENGTH_SHORT).show()
    }
}
