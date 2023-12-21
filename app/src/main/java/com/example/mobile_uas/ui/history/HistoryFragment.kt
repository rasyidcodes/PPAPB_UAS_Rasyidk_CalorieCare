package com.example.mobile_uas.ui.history

// HistoryFragment.kt

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_uas.data.database.MenuRoomDatabase
import com.example.mobile_uas.data.model.room.MenuUser
import com.example.mobile_uas.databinding.FragmentHistoryBinding
import com.example.mobile_uas.usermenu.choosemenu.ChooseMenuActivity
import com.example.mobile_uas.util.SharedPreferencesHelper
import com.example.myapplication.data.database.MenuUserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMenuUserDao: MenuUserDAO
    private lateinit var menuUserAdapter: MenuUserAdapter
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val db = MenuRoomDatabase.getDatabase(requireContext())
        mMenuUserDao = db?.MenuUserDAO() ?: throw Exception("Database not initialized")
        sharedPreferencesHelper = SharedPreferencesHelper.getInstance(requireContext())

        // Set up RecyclerView
        val recyclerView: RecyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)
        menuUserAdapter = MenuUserAdapter(emptyList(), mMenuUserDao)
        recyclerView.adapter = menuUserAdapter

        binding.fabAdd.setOnClickListener {
//            val menuUser = MenuUser(
//                userId = sharedPreferencesHelper.getUserId().toString(),
//                type = "lunch",
//                action = "makan",
//                foodName = "foodName",
//                foodCalorie = 2000,
//                serving = 1,
//                date = "2023-01-01"
//            )
//            insertMenu(menuUser)

            val toMainActivity = Intent(requireContext(), ChooseMenuActivity::class.java)
            startActivity(toMainActivity)
        }

        val userId = sharedPreferencesHelper.getUserId()
        updateRecyclerView(userId.toString())

        return root
    }

    private fun insertMenu(menuUser: MenuUser) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                mMenuUserDao.insert(menuUser)
            }
            showToast("Menu added successfully")
        } catch (e: Exception) {
            showToast("Error adding menu")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun updateRecyclerView(userId: String) {
        // Use the allMenusByUserId query to retrieve menus for the specific userId
        mMenuUserDao.allMenusByUserId(userId).observe(viewLifecycleOwner, { menuUserList ->
            menuUserAdapter.updateData(menuUserList)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

