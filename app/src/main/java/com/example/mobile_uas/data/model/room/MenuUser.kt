package com.example.mobile_uas.data.model.room

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity(tableName = "menuuser_table")
data class MenuUser(

    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,

    // get from firebase auth
    var userId: String = "",

    // Add new fields
    val type: String = "", // "lunch", "dinner", "breakfast"
    val action: String = "", // "makan", "workout"
    val foodName: String = "",
    val foodCalorie: Int = 0,
    val serving: Int = 0,
    val date: String = "",
    val time: String = "",


) : Serializable