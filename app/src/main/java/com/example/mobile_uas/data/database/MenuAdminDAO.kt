package com.example.myapplication.data.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobile_uas.data.model.room.MenuAdmin

@Dao
interface MenuAdminDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert (menu : MenuAdmin)

    @Query("SELECT * from menuadmin_table")
    fun getUnsyncedData() : LiveData<List<MenuAdmin>>


    @Query("DELETE FROM menuadmin_table") // Query untuk menghapus semua data dari tabel menu_admin
    fun deleteAllMenuAdmin()


    @Delete
    fun delete(menu: MenuAdmin)






}