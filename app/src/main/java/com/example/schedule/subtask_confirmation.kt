package com.example.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.schedule.databinding.FragmentSubtaskConfirmationBinding


class subtask_confirmation : DialogFragment() {
    private lateinit var layoutList:LinearLayout;

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ) : View?{

        val binding: FragmentSubtaskConfirmationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)




        return binding.root
    }

}
