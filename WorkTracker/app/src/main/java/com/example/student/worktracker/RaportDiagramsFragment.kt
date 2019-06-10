package com.example.student.worktracker


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.student.worktracker.R
import com.example.student.worktracker.Room.AppDb
import com.example.student.worktracker.Room.Raport
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.fragment_raport_diagrams.*
import java.util.concurrent.Executors
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.android.synthetic.main.fragment_raport_diagrams.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class RaportDiagramsFragment : Fragment() {

    var db : AppDb? = null
    var spinner : Spinner? = null
    var spinner2 : Spinner? = null
    var categoryList : Array<String?>? = null
    var periodTime : Array<String?>? = null
    var chart : BarChart? = null
    var mCategroy : String = "Week"
    var mType : String = "All"
    var date : Date = Date()
    var calendar : Calendar = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_raport_diagrams, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        calendar.setTime(date)
        db = AppDb(context!!)
        periodTime = arrayOf(getString(R.string.week),getString(R.string.month),getString(R.string.year))
        val myExecutor = Executors.newSingleThreadExecutor()
        myExecutor.execute {
            var raports = db!!.raportDao().getCategories()
            categoryList = arrayOfNulls<String>(raports.size+1)
            categoryList!![0] = getString(R.string.all)
            for (i in 1 until raports.size+1) {
                val raport = raports[i-1]
                categoryList!![i] = raport
            }

            val adapter = ArrayAdapter<String>(activity!!, R.layout.spinner_item, R.id.categoryItem, categoryList)
            val adapterTime = ArrayAdapter<String>(activity!!, R.layout.spinner_item, R.id.categoryItem, periodTime)

            activity!!.runOnUiThread(Runnable {
                spinner!!.adapter = adapter
                spinner2!!.adapter = adapterTime
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.runOnUiThread(Runnable {
            spinner = activity!!.findViewById(R.id.spinner)
            spinner2 = activity!!.findViewById(R.id.spinner2)
            chart = activity!!.findViewById(R.id.chart) as BarChart

            chart!!.setDrawBarShadow(false)
            chart!!.setDrawValueAboveBar(true)

            chart!!.setMaxVisibleValueCount(10)
            chart!!.isScaleXEnabled = true
            chart!!.getDescription().setEnabled(false)

            val l = chart!!.getLegend()
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)
            l.form = LegendForm.SQUARE
            l.formSize = 6f
            l.textSize = 13f
            l.xEntrySpace = 3f


            spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    mCategroy = categoryList!![position].toString()
                    if(categoryList!!.size > 1)
                    showSelectedDiagram()
                }
            }
            spinner2!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    mType = periodTime!![position].toString()
                    if(categoryList!!.size > 1)
                    showSelectedDiagram()
                }
            }
        })
    }

    fun showSelectedDiagram()
    {
        if(mCategroy == getString(R.string.all))
        {
            if(mType == getString(R.string.week))
            {

                chart!!.minOffset = 20f
                var xAxisFormatter : ValueFormatter = DayAxisValueFormatter(chart!!)
                val xAxis = chart!!.getXAxis()
                xAxis.position = XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.granularity = 1f // only intervals of 1 day
                xAxis.labelCount = 7
                xAxis.valueFormatter = xAxisFormatter
                xAxis.textSize = 5f

                val yLeftAxis = chart!!.getAxisLeft()
                yLeftAxis.labelCount = 5
                yLeftAxis.textSize = 12f

                val yRightAxis = chart!!.getAxisRight()
                yRightAxis.labelCount = 5
                yRightAxis.textSize = 12f

                var raports : List<Raport>
                var entriesList = ArrayList<ArrayList<BarEntry>>()
                val myExecutor = Executors.newSingleThreadExecutor()
                myExecutor.execute {
                    raports = db!!.raportDao().getRaports()

                    var format = SimpleDateFormat("dd/MM/yyyy HH:mm")

                    for (cat in categoryList!!)
                    {
                        if(cat != getString(R.string.all))
                        {
                            entriesList.add(ArrayList())
                        }
                    }

                    for(raport in raports)
                    {
                        var date = format.parse(raport.StartDate)
                        var mCalendar = Calendar.getInstance()
                        mCalendar.setTime(date)
                        var dayOfYear = mCalendar.get(Calendar.DAY_OF_YEAR)
                        if(mCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR) > (-8) &&
                            mCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR)  < 1 &&
                            mCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR))
                        {
                            entriesList[categoryList!!.indexOf(raport.Category)-1].add(BarEntry(dayOfYear.toFloat(), raport.WorkTime.toFloat()/60))
                        }
                    }

                    var set = ArrayList<BarDataSet>()
                    var i = 1
                    var rnd = Random()
                    for (entry in entriesList)
                    {
                        set.add(BarDataSet(entry, categoryList!![i]))
                        set[i-1].setDrawIcons(false)
                        set[i-1].color = Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                        i++
                    }

                    var linedata = BarData(set as List<BarDataSet>)

                    linedata.setValueTextSize(15f)
                    linedata.setBarWidth(0.4f)

                    activity!!.runOnUiThread(Runnable {
                        chart!!.setData(linedata)
                        chart!!.invalidate()
                    })
                }
            }
            else if(mType == getString(R.string.month))
            {
                val xAxis = chart!!.getXAxis()
                var xAxisFormatter : ValueFormatter = MonthAxisValueFormatter(chart!!)
                xAxis.position = XAxisPosition.BOTTOM
                xAxis.valueFormatter = xAxisFormatter
                xAxis.setDrawGridLines(false)
                xAxis.granularity = 1f // only intervals of 1 day
                xAxis.textSize = 5f

                val daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                xAxis.labelCount = daysOfMonth

                var raports : List<Raport>
                var entriesList = ArrayList<ArrayList<BarEntry>>()
                val myExecutor = Executors.newSingleThreadExecutor()
                myExecutor.execute {
                    raports = db!!.raportDao().getRaports()

                    var format = SimpleDateFormat("dd/MM/yyyy HH:mm")

                    for (cat in categoryList!!)
                    {
                        if(cat != getString(R.string.all))
                        {
                            entriesList.add(ArrayList())
                        }
                    }

                    for(raport in raports)
                    {
                        var date = format.parse(raport.StartDate)
                        var mCalendar = Calendar.getInstance()
                        mCalendar.setTime(date)
                        var dayOfYear = mCalendar.get(Calendar.DAY_OF_YEAR)
                        if(mCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                            mCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR))
                        {
                            entriesList[categoryList!!.indexOf(raport.Category)-1].add(BarEntry(dayOfYear.toFloat(), raport.WorkTime.toFloat()/60))
                        }

                    }

                    var set = ArrayList<BarDataSet>()
                    var i = 1
                    var rnd = Random()
                    for (entry in entriesList)
                    {
                        set.add(BarDataSet(entry, categoryList!![i]))
                        set[i-1].setDrawIcons(false)
                        set[i-1].color = Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                        i++
                    }
                    var linedata = BarData(set as List<BarDataSet>)

                    linedata.setValueTextSize(15f)
                    linedata.setBarWidth(0.4f)

                    activity!!.runOnUiThread(Runnable {
                        chart!!.setData(linedata)
                        chart!!.invalidate()
                    })
                }
            }
            else if(mType == getString(R.string.year))
            {
                val xAxis = chart!!.getXAxis()
                xAxis.position = XAxisPosition.BOTTOM
                var xAxisFormatter : ValueFormatter = DayAxisValueFormatter(chart!!)
                xAxis.valueFormatter = xAxisFormatter
                xAxis.setDrawGridLines(false)
                xAxis.granularity = 1f // only intervals of 1 day
                xAxis.textSize = 5f

                val daysInYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR)
                xAxis.labelCount = daysInYear

                var raports : List<Raport>
                var entriesList = ArrayList<ArrayList<BarEntry>>()
                val myExecutor = Executors.newSingleThreadExecutor()
                myExecutor.execute {
                    raports = db!!.raportDao().getRaports()

                    var format = SimpleDateFormat("dd/MM/yyyy HH:mm")

                    for (cat in categoryList!!)
                    {
                        if(cat != getString(R.string.all))
                        {
                            entriesList.add(ArrayList())
                        }
                    }

                    for(raport in raports)
                    {
                        var date = format.parse(raport.StartDate)
                        var mCalendar = Calendar.getInstance()
                        mCalendar.setTime(date)
                        var dayOfYear = mCalendar.get(Calendar.DAY_OF_YEAR)
                        if(mCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR))
                        {
                            entriesList[categoryList!!.indexOf(raport.Category)-1].add(BarEntry(dayOfYear.toFloat(), raport.WorkTime.toFloat()/60))
                        }
                    }

                    var set = ArrayList<BarDataSet>()
                    var i = 1
                    var rnd = Random()
                    for (entry in entriesList)
                    {
                        set.add(BarDataSet(entry, categoryList!![i]))
                        set[i-1].setDrawIcons(false)
                        set[i-1].color = Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                        i++
                    }

                    var linedata = BarData(set as List<BarDataSet>)

                    linedata.setValueTextSize(15f)
                    linedata.setBarWidth(0.4f)

                    activity!!.runOnUiThread(Runnable {
                        chart!!.setData(linedata)
                        chart!!.invalidate()
                    })
                }
            }
        }
        else
        {
            if(mType == getString(R.string.week))
            {

                chart!!.minOffset = 20f
                var xAxisFormatter : ValueFormatter = DayAxisValueFormatter(chart!!)
                val xAxis = chart!!.getXAxis()
                xAxis.position = XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.granularity = 1f // only intervals of 1 day
                xAxis.labelCount = 7
                xAxis.valueFormatter = xAxisFormatter
                xAxis.textSize = 5f

                val yLeftAxis = chart!!.getAxisLeft()
                yLeftAxis.labelCount = 5
                yLeftAxis.textSize = 12f

                val yRightAxis = chart!!.getAxisRight()
                yRightAxis.labelCount = 5
                yRightAxis.textSize = 12f

                var raports : List<Raport>
                var entriesList = ArrayList<BarEntry>()
                val myExecutor = Executors.newSingleThreadExecutor()
                myExecutor.execute {
                    raports = db!!.raportDao().selectCategory(mCategroy)

                    var format = SimpleDateFormat("dd/MM/yyyy HH:mm")

                    for(raport in raports)
                    {
                        var date = format.parse(raport.StartDate)
                        var mCalendar = Calendar.getInstance()
                        mCalendar.setTime(date)
                        var dayOfYear = mCalendar.get(Calendar.DAY_OF_YEAR)
                        if(mCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR) > (-8) &&
                            mCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR)  < 1 &&
                            mCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR))
                        {
                            entriesList.add(BarEntry(dayOfYear.toFloat(), raport.WorkTime.toFloat()/60))
                        }
                    }

                    var dataSet = BarDataSet(entriesList, "Work hours")
                    dataSet.setDrawIcons(false)
                    dataSet.color = R.color.primaryDarkColor

                    var linedata = BarData(dataSet)
                    linedata.setValueTextSize(15f)
                    linedata.setBarWidth(0.2f)

                    activity!!.runOnUiThread(Runnable {
                        chart!!.setData(linedata)
                        chart!!.invalidate()
                    })
                }
            }
            else if(mType == getString(R.string.month))
            {
                val xAxis = chart!!.getXAxis()
                var xAxisFormatter : ValueFormatter = MonthAxisValueFormatter(chart!!)
                xAxis.position = XAxisPosition.BOTTOM
                xAxis.valueFormatter = xAxisFormatter
                xAxis.setDrawGridLines(false)
                xAxis.granularity = 1f // only intervals of 1 day
                xAxis.textSize = 5f

                val daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                xAxis.labelCount = daysOfMonth

                var raports : List<Raport>
                var entriesList = ArrayList<BarEntry>()
                val myExecutor = Executors.newSingleThreadExecutor()
                myExecutor.execute {
                    raports = db!!.raportDao().selectCategory(mCategroy)

                    var format = SimpleDateFormat("dd/MM/yyyy HH:mm")

                    for(raport in raports)
                    {
                        var date = format.parse(raport.StartDate)
                        var mCalendar = Calendar.getInstance()
                        mCalendar.setTime(date)
                        var dayOfYear = mCalendar.get(Calendar.DAY_OF_YEAR)
                        if(mCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                            mCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR))
                        {
                            entriesList.add(BarEntry(dayOfYear.toFloat(), raport.WorkTime.toFloat()/60))
                        }
                    }

                    var dataSet = BarDataSet(entriesList, "Work hours")
                    dataSet.setDrawIcons(false)
                    dataSet.color = R.color.primaryDarkColor

                    var linedata = BarData(dataSet)
                    linedata.setValueTextSize(5f)
                    linedata.setBarWidth(0.2f)

                    activity!!.runOnUiThread(Runnable {
                        chart!!.setData(linedata)
                        chart!!.invalidate()
                    })
                }
            }
            else if(mType == getString(R.string.year))
            {
                val xAxis = chart!!.getXAxis()
                xAxis.position = XAxisPosition.BOTTOM
                var xAxisFormatter : ValueFormatter = DayAxisValueFormatter(chart!!)
                xAxis.valueFormatter = xAxisFormatter
                xAxis.setDrawGridLines(false)
                xAxis.granularity = 1f // only intervals of 1 day
                xAxis.textSize = 5f

                val daysInYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR)
                xAxis.labelCount = daysInYear

                var raports : List<Raport>
                var entriesList = ArrayList<BarEntry>()
                val myExecutor = Executors.newSingleThreadExecutor()
                myExecutor.execute {
                    raports = db!!.raportDao().selectCategory(mCategroy)

                    var format = SimpleDateFormat("dd/MM/yyyy HH:mm")

                    for(raport in raports)
                    {
                        var date = format.parse(raport.StartDate)
                        var mCalendar = Calendar.getInstance()
                        mCalendar.setTime(date)
                        var dayOfYear = mCalendar.get(Calendar.DAY_OF_YEAR)
                        if(mCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR))
                        {
                            entriesList.add(BarEntry(dayOfYear.toFloat(), raport.WorkTime.toFloat()/60))
                        }
                    }

                    var dataSet = BarDataSet(entriesList, "Work hours")
                    dataSet.setDrawIcons(false)
                    dataSet.color = R.color.primaryDarkColor

                    var linedata = BarData(dataSet)
                    linedata.setValueTextSize(5f)
                    linedata.setBarWidth(0.2f)

                    activity!!.runOnUiThread(Runnable {
                        chart!!.setData(linedata)
                        chart!!.invalidate()
                    })
                }
            }
        }
    }
}
