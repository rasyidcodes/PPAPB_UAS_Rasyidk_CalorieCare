package com.example.mobile_uas.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobile_uas.data.database.MenuRoomDatabase
import com.example.mobile_uas.databinding.FragmentHomeBinding
import com.example.mobile_uas.util.SharedPreferencesHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class HomeFragment : Fragment() {

private var _binding: FragmentHomeBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    val root: View = binding.root
      sharedPreferencesHelper = SharedPreferencesHelper.getInstance(requireContext())


      //GET CALORIE DATA
      val userId = sharedPreferencesHelper.getUserId().toString()
      val targetCalorie = sharedPreferencesHelper.getUserCalorie()
      val currentDate = "2023-01-02"
      homeViewModel.getTotalCaloriesForCurrentUserAndDate(userId, getCurrentDateInGMTPlus7()).observe(viewLifecycleOwner) { totalCalories ->


          val calLeft = targetCalorie-totalCalories
          val persenPB: Double = (calLeft.toDouble() / targetCalorie.toDouble()) * 100

          binding.progressCircularIndicator.setProgress(persenPB.toInt(),true)

          val displayedCalories = calLeft?.takeIf { it >= 0 } ?: 0
          binding.frHomeTvCalLeft.text = "$calLeft\nCal\nLeft"
      }

      //GET CAL LUNCH
      homeViewModel.getCaloriesByType(userId, getCurrentDateInGMTPlus7(), "lunch").observe(viewLifecycleOwner){lunchCalories ->
          val displayedLunchCalories = lunchCalories?.takeIf { it >= 0 } ?: 0
          binding.frHomeTvLunch.text = displayedLunchCalories.toString()+"cal"
      }

        //GET CAL BREAKFAST
      homeViewModel.getCaloriesByType(userId, getCurrentDateInGMTPlus7(), "breakfast").observe(viewLifecycleOwner){breakfastCalories ->
          val displayedBreakfastCalories = breakfastCalories?.takeIf { it >= 0 } ?: 0
          binding.frHomeTvBreakfast.text = displayedBreakfastCalories.toString() +"cal"
      }

      //GET CAL DINNER
      homeViewModel.getCaloriesByType(userId, getCurrentDateInGMTPlus7(), "dinner").observe(viewLifecycleOwner){dinnertCalories ->
          val displayedDinnerCalories = dinnertCalories?.takeIf { it >= 0 } ?: 0
          binding.frHomeTvDinner.text = displayedDinnerCalories.toString()+"cal"
      }


      //SETTEXT FROM SHARED PREF
      binding.frHomeTvName.text = sharedPreferencesHelper.getUserName().toString()
      binding.frHomeTvCalLeft.text = sharedPreferencesHelper.getUserCalorie().toString() + "\nCal\nLeft"
    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getCurrentDateInGMTPlus7(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormat.timeZone = TimeZone.getTimeZone("GMT+7")
        return dateFormat.format(Date())
    }




}