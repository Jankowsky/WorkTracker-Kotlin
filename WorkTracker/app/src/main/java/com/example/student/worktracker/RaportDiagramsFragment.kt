package com.example.student.worktracker


import android.content.Context
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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.fragment_raport_diagrams.*
import java.util.concurrent.Executors
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.Legend




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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_raport_diagrams, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

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

            chart!!.setMaxVisibleValueCount(60)
            chart!!.getDescription().setEnabled(false)

            val l = chart!!.getLegend()
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)
            l.form = LegendForm.SQUARE
            l.formSize = 15f
            l.textSize = 20f
            l.xEntrySpace = 4f


            spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    mCategroy = categoryList!![position].toString()
                    showSelectedDiagram()
                }
            }
            spinner2!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    mType = periodTime!![position].toString()
                    showSelectedDiagram()
                }
            }
        })
    }

    fun showSelectedDiagram()
    {
        var i =0
        val entries = ArrayList<BarEntry>()

        if(mCategroy == getString(R.string.all))
        {
            if(mType == getString(R.string.week))
            {

            }
            else if(mType == getString(R.string.month))
            {

            }
            else if(mType == getString(R.string.year))
            {

            }
        }
        else
        {
            if(mType == getString(R.string.week))
            {

            }
            else if(mType == getString(R.string.month))
            {

            }
            else if(mType == getString(R.string.year))
            {

            }
        }
        for (data in categoryList!!) {
            // turn your data into Entry objects
            entries.add(BarEntry(200f+i, 10f+i))
            i++
        }

        var dataSet = BarDataSet(entries, "Work")
        dataSet.setDrawIcons(false)
        dataSet.color = R.color.primaryDarkColor

        var linedata = BarData(dataSet)
        linedata.setValueTextSize(15f)
        linedata.setBarWidth(0.2f)

        chart!!.setData(linedata)
        chart!!.invalidate()
    }

    ///////////// TODO: get, prepare and show data
    fun getRaports(cat: String) // Selected company
    {
        val myExecutor = Executors.newSingleThreadExecutor()
        myExecutor.execute {
            var raports = db!!.raportDao().selectCategory(cat)
            val entriesList = ArrayList<BarEntry>()

            for(raport in raports)
            {
                //entriesList.add(BarEntry())
            }
        }
    }

    fun getRaports()  // Show all companys
    {

    }
}
