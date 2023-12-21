package com.example.myapplication.data.database

// MenuUserDAO.kt

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mobile_uas.data.model.room.MenuUser

@Dao
interface MenuUserDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(menu: MenuUser)

    @Update
    fun update(menu: MenuUser)

    @Delete
    fun delete(menu: MenuUser)

    @Query("SELECT * FROM menuuser_table WHERE userId = :userId ORDER BY id DESC")
    fun allMenusByUserId(userId: String): LiveData<List<MenuUser>>

    @Query("SELECT * FROM menuuser_table WHERE userId = :userId AND date = :date ORDER BY id DESC")
    fun allMenusByUserId(userId: String, date: String): LiveData<List<MenuUser>>

    @Query("SELECT * FROM menuuser_table WHERE userId = :userId AND type = :category AND date = :date ORDER BY id DESC")
    fun allMenusByCategory(userId: String, category: String, date: String): LiveData<List<MenuUser>>

    @Query("SELECT SUM(foodCalorie) FROM menuuser_table WHERE userId = :userId AND date = :date")
    fun getTotalCaloriesByUserIdAndDate(userId: String,date: String): LiveData<Int>

    @Query("SELECT SUM(foodCalorie) FROM menuuser_table WHERE userId = :userId AND date = :date AND type = :type")
    fun getCaloriesByType(userId: String,date: String, type:String): LiveData<Int>
}
