package com.khalifa.locateme.fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.widget.TimePicker

private const val DEFAULT_HOURS = 1
private const val DEFAULT_MINUTES = 0

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    companion object {
        private val TAG: String = TimePickerFragment::class.java.simpleName

        fun showFragment(fragmentManager: FragmentManager?,
                         onFragmentInteractionListener: OnFragmentInteractionListener) =
            fragmentManager?.let { manager ->
                TimePickerFragment().apply {
                    fragmentInteractionListener = onFragmentInteractionListener
                    show(manager, TAG)
                }
            }
    }


    interface OnFragmentInteractionListener {

        fun onTimeSet(hours: Int, minutes: Int)
    }

    private var fragmentInteractionListener: OnFragmentInteractionListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?) = TimePickerDialog(
        activity,
        this,
        DEFAULT_HOURS,
        DEFAULT_MINUTES,
        true
    )

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        fragmentInteractionListener?.onTimeSet(hourOfDay, minute)
    }
}