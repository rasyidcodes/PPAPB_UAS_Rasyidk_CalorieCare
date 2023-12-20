package com.example.mobile_uas.ui.history

// HistoryFragment.kt

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_uas.BottomNavigationActivity
import com.example.mobile_uas.addmenu.AddFoodActivity
import com.example.mobile_uas.data.database.MenuRoomDatabase
import com.example.mobile_uas.data.model.room.MenuUser
import com.example.mobile_uas.databinding.FragmentHistoryBinding
import com.example.mobile_uas.util.SharedPreferencesHelper
import com.example.myapplication.data.database.MenuUserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

            val toMainActivity = Intent(requireContext(), AddFoodActivity::class.java)
            startActivity(toMainActivity)
        }

        updateRecyclerView()

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

    private fun updateRecyclerView() {
        mMenuUserDao.allMenus.observe(viewLifecycleOwner, { menuUserList ->
            menuUserAdapter.updateData(menuUserList)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

