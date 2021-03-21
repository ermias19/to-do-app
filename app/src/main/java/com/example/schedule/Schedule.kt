package com.example.schedule

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.schedule.Schedule.Schedule
import com.example.schedule.databinding.FragmentScheduleBinding
import com.example.schedule.models.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [Schedule.newInstance] factory method to
 * create an instance of this fragment.
 */
class Schedule : Fragment() {
    private lateinit var layoutList:LinearLayout;
    private lateinit var datePickerDialog:DatePickerDialog;
    private lateinit var mDisplayDate:Button;
    private lateinit var binding: FragmentScheduleBinding
    private lateinit var mDateSetListener: OnDateSetListener;
    private var task: Task? = null
    // TODO: Rename and change types of parameters
    data class Schedule(val Date: String, val startTime: String, val finishTime: String, val title:String, val note:String)

    private var dateSelected = Calendar.getInstance()
    private lateinit var db: AppDatabase
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)
//    this code block is for making spinner date picker to be 24 hours
        val timePickerTo=binding.simpleTimePicke
        val timePickerFrom=binding.simpleTimePicker
        timePickerTo.setIs24HourView(true)
        timePickerFrom.setIs24HourView(true)

        layoutList =binding.layoutList
        binding.addTask.setOnClickListener {
            addView(null)
        }

//        this code block is for date picker
        initDatePicker()
        mDisplayDate=binding.datePickerButton
        mDisplayDate.text = getDateString(Calendar.getInstance())

        binding.save.setOnClickListener {
            checkIfValidAndRead()
        }

        mDisplayDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal[Calendar.YEAR]
            val month = cal[Calendar.MONTH]
            val day = cal[Calendar.DAY_OF_MONTH]
            val dialog = DatePickerDialog(
                requireContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        mDateSetListener = OnDateSetListener { datePicker, year, month, day ->
            var month = month
            month += 1
            Log.d("TAG", "onDateSet: mm/dd/yyy: $month/$day/$year")
            dateSelected.set(year, month - 1, day)

            val date = makeDateString(day, month, year)
            mDisplayDate.text = date
        }

        binding.save.setOnClickListener {
            val title = binding.title.text.toString()
            dateSelected.set(Calendar.HOUR, timePickerFrom.hour)
            dateSelected.set(Calendar.MINUTE, timePickerFrom.minute)
            val startDate = dateSelected.time
            Log.w("StartDate", startDate.toString())
            dateSelected.set(Calendar.HOUR, timePickerTo.hour)
            dateSelected.set(Calendar.MINUTE, timePickerTo.minute)
            val endDate = dateSelected.time
            Log.w("EndDate", endDate.toString())

            val notes = binding.editTextTextMultiLine.text.toString()

            var subtaskList = layoutList.children.map {
                return@map Pair(it.findViewById<EditText>(R.id.edit_task).text.toString(), false)
            }.filter {
                it.first.isNotEmpty()
            }.toMutableList()

            if (task == null){
                task = Task(UUID.randomUUID(), title, startDate, endDate, notes, subtaskList)
            } else {
                task = Task(task!!.uid, title, startDate, endDate, notes, subtaskList)
            }
            GlobalScope.launch {
                db = AppDatabase.getDatabase(requireContext())
                db.taskDao().insertTask(task!!)
                it.findNavController().popBackStack()
            }
        }

        val taskID = arguments?.getString("TaskID")
        if (taskID != null){
            GlobalScope.launch {
                val db = AppDatabase.getDatabase(requireContext())
                task = db.taskDao().getByID(UUID.fromString(taskID))
                val cal = Calendar.getInstance()
                val start = task!!.startDate
                cal.time = start!!
                dateSelected = cal

                requireActivity().runOnUiThread {
                    mDisplayDate.text = getDateString(cal)
                    binding.title.setText(task!!.title)
                    timePickerFrom.hour = cal.get(Calendar.HOUR)
                    timePickerFrom.minute = cal.get(Calendar.MINUTE)
                    cal.time = task!!.endDate
                    timePickerTo.hour = cal.get(Calendar.HOUR)
                    timePickerTo.minute = cal.get(Calendar.MINUTE)
                    task!!.subTasks.forEach {
                        addView(it.first)
                    }
                    binding.editTextTextMultiLine.setText(task!!.notes)
                }
            }
        }


        return binding.root
    }

    private fun checkIfValidAndRead(): Boolean {
        var result = true
        for (i in 0 until layoutList.childCount) {
            val cricketerView = layoutList.getChildAt(i)
            val editTextName =
                cricketerView.findViewById<View>(R.id.edit_task) as EditText
            if (editTextName.text.toString() != "") {
                Toast.makeText(requireActivity(), editTextName.text.toString(), Toast.LENGTH_SHORT).show()
            } else {
                result = false
                break
            }

        }

        return result
    }




    private fun addView(prevText: String?) {
        val task: View = layoutInflater.inflate(R.layout.row_add_input, null, false)
        val editText = task.findViewById<View>(R.id.edit_task) as EditText
        val imageClose = task.findViewById<View>(R.id.image_remove) as ImageView
        prevText?.let {editText.setText(prevText)}
        imageClose.setOnClickListener {
            removeTaskView(task)
            }
        layoutList.addView(task)
    }

    private fun removeTaskView(task: View) {
        layoutList.removeView(task)

    }

    private fun initDatePicker() {
       //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());


    }
    private fun getDateString(cal: Calendar): String? {
        val year = cal[Calendar.YEAR]
        var month = cal[Calendar.MONTH]
        month = month + 1
        val day = cal[Calendar.DAY_OF_MONTH]
        return makeDateString(day, month, year)
    }


    fun openDatePicker(view: View?) {
        datePickerDialog.show()
    }
    private fun makeDateString(day: Int, month: Int, year: Int): String {
        var y=year.toString()
        return getMonthFormat(month) + " " + day + " " + y[y.length - 2]+y[y.length - 1]
    }
    private fun getMonthFormat(month: Int): String {
        if (month == 1) return "JAN"
        if (month == 2) return "FEB"
        if (month == 3) return "MAR"
        if (month == 4) return "APR"
        if (month == 5) return "MAY"
        if (month == 6) return "JUN"
        if (month == 7) return "JUL"
        if (month == 8) return "AUG"
        if (month == 9) return "SEP"
        if (month == 10) return "OCT"
        if (month == 11) return "NOV"
        return if (month == 12) "DEC" else "JAN"

    }




}
