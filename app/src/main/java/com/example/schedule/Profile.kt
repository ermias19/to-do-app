package com.example.schedule

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.schedule.databinding.FragmentProfileBinding
import com.example.schedule.models.User
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//import com.example.shedule.databinding.FragmentProfileBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class Profile : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var db: AppDatabase

private lateinit var binding:FragmentProfileBinding;
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )
//        val arg=MainPageArgs.fromBundle(arguments!!)
//        Toast.makeText(context, arg.name, Toast.LENGTH_LONG).show()



//        var arr=ArrayList<PieEntry>()
//        arr.add(PieEntry(200.0F, "asd"))
//        arr.add(PieEntry(20.0F, "asd"))
//        arr.add(PieEntry(240.0F, "asd"))
//
//        var pieDataSet: PieDataSet = PieDataSet(arr, "dont now shite about this")
//        pieDataSet.valueTextColor= Color.BLACK
//       pieDataSet.colors= listOf(
//           Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
//           Color.rgb(106, 150, 31), Color.rgb(179, 100, 53)
//       )
//        pieDataSet.valueTextSize=16f
//        var pieData= PieData(pieDataSet)
//        chart.data=pieData
//        chart.centerText="man"
//        chart.animate()



//        the left chart


        var chart: PieChart =binding.pi;
        var arr1=ArrayList<PieEntry>()
        arr1.add(PieEntry(200.0F))
        arr1.add(PieEntry(110.0F))

        var pieDataSet1: PieDataSet = PieDataSet(arr1,"")
        pieDataSet1.valueTextSize=16f
        pieDataSet1.colors= ColorTemplate.COLORFUL_COLORS.toList()


        var pieData1= PieData(pieDataSet1)
        chart.setBackgroundColor(Color.TRANSPARENT)
        chart.data=pieData1
        chart.centerText="20%"
        chart.setCenterTextColor(Color.WHITE)
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.TRANSPARENT);
        chart.description.isEnabled = false
        chart.setDrawSlicesUnderHole(false)
        chart.data.setDrawValues(false)

        chart.legend.isEnabled=false;
        chart.setDrawEntryLabels(false)
        chart.setDrawMarkers(false)
        chart.animate()




        var chart1: PieChart =binding.pi2;
        var arr2=ArrayList<PieEntry>()
        arr2.add(PieEntry(200.0F))
        arr2.add(PieEntry(110.0F))

        var pieDataSet2: PieDataSet = PieDataSet(arr2,"")
        pieDataSet2.valueTextSize=16f
        pieDataSet2.colors= ColorTemplate.COLORFUL_COLORS.toList()


        var pieData2= PieData(pieDataSet2)
        chart1.setBackgroundColor(Color.TRANSPARENT)
        chart1.data=pieData2
        chart1.centerText="20%"
        chart1.setCenterTextColor(Color.WHITE)
        chart1.setDrawHoleEnabled(true);
        chart1.setHoleColor(Color.TRANSPARENT);
        chart1.description.isEnabled = false
        chart1.setDrawSlicesUnderHole(false)
        chart1.data.setDrawValues(false)


        chart1.legend.isEnabled=false;
        chart1.setDrawEntryLabels(false)
        chart1.setDrawMarkers(false)
        chart1.animate()







        var user: User
        GlobalScope.launch {
            db = AppDatabase.getDatabase(requireContext())
            user = db.userDao().getAll().first()

            binding.inputEmail.hint = user.username

            binding.register.setOnClickListener { view ->
                val pin = binding.inputPassword.text?.toString()

                if (!pin.isNullOrBlank()){
                    user.pin = pin.toInt()
                }

                user.askOnStart = binding.toggleButton1.isChecked

                val email = binding.inputEmail.text.toString()
                if (!email.isNullOrBlank()){
                    user.username = email
                }

                GlobalScope.launch { db.userDao().updateUser(user) }
                Log.w("MainActivity", user.toString())
                view.findNavController().popBackStack()
            }
        }



//the green ring chart
//        val cd:CircleDisplay = binding.circleDisplay
//        cd.setAnimDuration(2000)
//        cd.setValueWidthPercent(55f)
//        cd.setTextSize(18f)
//        cd.setColor(Color.GREEN)
//        cd.setDrawText(true)
//        cd.setDrawInnerCircle(true)
//        cd.setFormatDigits(1)
//        cd.setTouchEnabled(false)
////        cd.setSelectionListener(requireActivity())
//        cd.setUnit("%")
//        cd.setStepSize(0.5f)
//        // cd.setCustomText(...); // sets a custom array of text
//        // cd.setCustomText(...); // sets a custom array of text
//        cd.showValue(75f, 100f, true)





        return binding.root

    }



}


