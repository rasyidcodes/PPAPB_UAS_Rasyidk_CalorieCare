package com.example.mobile_uas.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_uas.data.database.MenuRoomDatabase
import com.example.mobile_uas.data.model.room.MenuUser
import com.example.myapplication.data.database.MenuUserDAO
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application){


    private val menuUserDAO: MenuUserDAO

    init {
        val database = MenuRoomDatabase.getDatabase(application.applicationContext)
        menuUserDAO = database?.MenuUserDAO() ?: throw Exception("Database not initialized")
    }



}