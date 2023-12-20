package com.example.mobile_uas.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "authAppPrefs"

        private const val KEY_USER_ID = "userIdFromFirebase"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"

//        private const val KEY_USERNAME = "username"
//        private const val KEY_PHONE = "phone"
//        private const val KEY_ROLE = "role"
//        private const val KEY_EMAIL = "gmail"


        private const val KEY_NAME = "NAME"
        private const val KEY_USERID = "USERID"
        private const val KEY_ROLE = "ROLE"
        private const val KEY_TIPE = "TIPE"
        private const val KEY_HEIGHT = "HEIGHT"
        private const val  KEY_WEIGHT = "WEIGHT"
        private const val KEY_TARGETWEIGHT = "TARGETWEIGHT"
        private const val KEY_CALORIE = "CALORIE"



        @Volatile
        private var instance: SharedPreferencesHelper? = null

        fun getInstance(context: Context): SharedPreferencesHelper {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesHelper(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun saveUserId(userId: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERID, userId)
        editor.apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USERID, null)
    }

    fun saveUserRole(userId: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ROLE, userId)
        editor.apply()
    }


    fun getUserRole(): String? {
        return sharedPreferences.getString(KEY_ROLE, null)
    }

    fun saveUserTipe(tipe: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_TIPE, tipe)
        editor.apply()
    }

    fun getUserTipe(): Int {
        return sharedPreferences.getInt(KEY_TIPE, 0)
    }



    fun saveUserName(name: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_NAME, name)
        editor.apply()
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_NAME, null)
    }

    fun saveUserHeight(height: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_HEIGHT, height)
        editor.apply()
    }

    fun getUserHeight(): Int {
        return sharedPreferences.getInt(KEY_HEIGHT, 0)
    }

    fun saveUserWeight(weight: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_WEIGHT, weight)
        editor.apply()
    }

    fun getUserWeight(): Int {
        return sharedPreferences.getInt(KEY_WEIGHT, 0)
    }

    fun saveUserTargetWeight(targetWeight: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_TARGETWEIGHT, targetWeight)
        editor.apply()
    }

    fun getUserTargetWeight(): Int {
        return sharedPreferences.getInt(KEY_TARGETWEIGHT, 0)
    }

    fun saveUserCalorie(calorie: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_CALORIE, calorie)
        editor.apply()
    }

    fun getUserCalorie(): Int {
        return sharedPreferences.getInt(KEY_CALORIE, 0)
    }


    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}
