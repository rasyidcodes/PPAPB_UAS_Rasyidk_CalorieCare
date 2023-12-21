package com.example.mobile_uas.usermenu.choosemenu

import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.text.DateFormat

class TimePicker : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireActivity(),
            activity as OnTimeSetListener,
            hourOfDay,
            minute,
            android.text.format.DateFormat.is24HourFormat(activity)
        )

    }
}