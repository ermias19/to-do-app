package com.example.schedule

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.models.Task
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


open class TaskAdapter(private val taskLists: List<Task>, private val context: Context): RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    private lateinit var card:LinearLayout;
private lateinit var layoutList:LinearLayout;
    inner class ViewHolder(listItemView: View): RecyclerView.ViewHolder(listItemView){
         val subtaskConfirm : LinearLayout = itemView.findViewById(R.id.subCinfirmation);
        val titleView: TextView = itemView.findViewById<TextView>(R.id.taskTitle)
        val dateView: TextView = itemView.findViewById<TextView>(R.id.taskDate)
        var pie: PieChart = itemView.findViewById<PieChart>(R.id.pi2)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val taskView = inflater.inflate(R.layout.task_item, parent, false)
        return ViewHolder(taskView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val taskItem: Task = taskLists[position]
        // Set item views based on your views and data model
        card=holder.subtaskConfirm
        var pie=holder.pie!!
        val textView = holder.titleView
        textView.text = taskItem.title
        val dateView = holder.dateView
        val timeFormat = SimpleDateFormat("hh:mm aa")
        dateView.text = "${timeFormat.format(taskItem.startDate!!)}-${timeFormat.format(taskItem.endDate!!)}"

        addData(pie)
        setListener(card, taskLists[position])


    }

    private fun addData(chart1: PieChart) {

        var arr2=ArrayList<PieEntry>()
        arr2.add(PieEntry(200.0F))//addin values for charts
        arr2.add(PieEntry(110.0F))//addin values for charts

        var pieDataSet2: PieDataSet = PieDataSet(arr2,"")
        pieDataSet2.valueTextColor= Color.WHITE
        pieDataSet2.colors= ColorTemplate.COLORFUL_COLORS.toList()

        pieDataSet2.valueTextSize=16f
        var pieData2= PieData(pieDataSet2)
        chart1.setBackgroundColor(Color.TRANSPARENT)
        chart1.data=pieData2
        chart1.centerText="20%"
        chart1.setCenterTextColor(Color.BLACK)
        chart1.setDrawHoleEnabled(true);
        chart1.setHoleColor(Color.TRANSPARENT);
        chart1.description.isEnabled = false
        chart1.setDrawSlicesUnderHole(false)
        chart1.data.setDrawValues(false)


        chart1.legend.isEnabled=false;
        chart1.setDrawEntryLabels(false)
        chart1.setDrawMarkers(false)
        chart1.animate()

    }

    open fun setListener(card: LinearLayout, task: Task) {
        card.setOnClickListener{
            if (task.startDate!! > Date()) {
                val action = MainPageDirections.actionMainPageToSchedule(task.uid.toString())
                it.findNavController().navigate(action)
            } else {
                reportTaskCompletionListener(card, task)
            }
        }

    }

    fun reportTaskCompletionListener(card: LinearLayout, task: Task) {
        val subTasks = task.subTasks.toMutableList()
        var lay=LayoutInflater.from(context).inflate(R.layout.fragment_subtask_confirmation,null,false)
        val updateBtn = lay.findViewById<Button>(R.id.update)
        layoutList=lay.findViewById(R.id.layoutList)
        for (i in subTasks.indices){
            addView(subTasks, i)
        }
        var myDialog: Dialog = Dialog(context);
        myDialog.setContentView(lay);
        myDialog.getWindow()?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
        updateBtn.setOnClickListener {
            Log.w("TaskAdapter", "We got to the updateBtn")
            task.subTasks = subTasks
            GlobalScope.launch {
                val db = AppDatabase.getDatabase(context)
                db.taskDao().insertTask(task)
                myDialog.dismiss()
            }
        }
        myDialog.show();

    }

    fun addView(subTasks: MutableList<Pair<String, Boolean>>, position: Int){
        val subTask = subTasks[position]
        var v: View=LayoutInflater.from(context).inflate(R.layout.row_for_confirmation, null, false)
        var check:CheckBox=v.findViewById(R.id.checkBox)
        check.setText(subTask.first)
        check.isChecked = subTask.second
        check.setOnCheckedChangeListener { _, isChecked ->
            subTasks[position] = subTask.copy(second = isChecked)
        }
        layoutList.addView(v)

    }


    override fun getItemCount(): Int {
        return taskLists.size
    }


}