package com.example.mobile_uas.admin

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
import com.example.mobile_uas.R
import com.example.mobile_uas.data.model.firestore.MenuAdminFS
import com.google.firebase.firestore.FirebaseFirestore

class AdminDataAdapter : RecyclerView.Adapter<AdminDataAdapter.AdminDataViewHolder>() {

    private var makanan: List<MenuAdminFS> = listOf()
    private val firestore = FirebaseFirestore.getInstance()
    private val makananCollection = firestore.collection("makanan")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminDataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin, parent, false)
        return AdminDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminDataViewHolder, position: Int) {
        val currentMakanan = makanan[position]

        holder.textViewBuku.text = currentMakanan.foodName
        holder.textViewCalorie.text =currentMakanan.foodCalorie.toString()  +" Cal"

        holder.btEdit.setOnClickListener{

            try {
                val intent = Intent(holder.itemView.context, AdminEditFoodActivity::class.java)
                intent.putExtra("id", currentMakanan.id)
                intent.putExtra("foodName", currentMakanan.foodName)
                intent.putExtra("foodCalorie", currentMakanan.foodCalorie)
                holder.itemView.context.startActivity(intent)
            }catch (e: Exception){
                showToast(e.toString(),holder)
                Log.d("ERR", e.toString())
            }
        }

        holder.btDelete.setOnClickListener {
            showYesNoAlertDialog(
                holder.itemView.context,
                "Apakah anda yakin akan menghapus ${currentMakanan.foodName}",
                DialogInterface.OnClickListener { _, _ ->
                    deleteMakanan(currentMakanan.id,holder)
                }
            )
        }

    }

    private fun deleteMakanan(id: String, holder: AdminDataViewHolder) {
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

    inner class AdminDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewBuku: TextView = itemView.findViewById(R.id.item_admin_FoodName)
        val textViewCalorie: TextView = itemView.findViewById(R.id.item_admin_Calorie)
        val btDelete : Button = itemView.findViewById(R.id.item_admin_Delete)
        val btEdit : Button = itemView.findViewById(R.id.item_admin_edit)
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

    private fun showToast(message: String, holder: AdminDataViewHolder) {
        Toast.makeText(holder.itemView.context, message, Toast.LENGTH_SHORT).show()
    }
}
