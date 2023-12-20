package com.example.mobile_uas.data.model.room

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity(tableName = "menuadmin_table")
data class MenuAdmin(

    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,
    // get from firebase auth
    var userId: String = "",
    // Add new fields
    val action: String = "", // "makan", "workout"
    val foodName: String = "",
    val foodCalorie: Int = 0,
    val date: String = "",

) : Serializable