package com.example.mobile_uas.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mobile_uas.data.model.room.MenuAdmin
import com.example.mobile_uas.data.model.room.MenuUser
import com.example.myapplication.data.database.MenuAdminDAO
import com.example.myapplication.data.database.MenuUserDAO

@Database(entities = [MenuUser::class, MenuAdmin::class],
    version = 2, // Ubah versi ke versi yang lebih tinggi
    exportSchema = false)

abstract class MenuRoomDatabase : RoomDatabase() {

    abstract fun MenuUserDAO(): MenuUserDAO?
    abstract fun MenuAdminDAO() : MenuAdminDAO?
//    dao menu, milik admin

    companion object {
        @Volatile
        private var INSTANCE : MenuRoomDatabase ? = null
        fun getDatabase(context: Context) : MenuRoomDatabase? {
            if(INSTANCE == null){
                synchronized(MenuRoomDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MenuRoomDatabase::class.java, "menu_db"
                    )
                        .fallbackToDestructiveMigration() // Tambahkan fallbackToDestructiveMigration jika perlu
                        .build()
                }
            }
            return INSTANCE
        }
    }
}
