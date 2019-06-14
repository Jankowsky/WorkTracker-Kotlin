package com.example.student.worktracker

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import android.widget.TextView
import com.example.student.worktracker.Room.Raport
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ListAdapter (private val context: Activity,private val raports: ArrayList<Raport>)
    : ArrayAdapter<Raport>(context, R.layout.list_row_layout, raports)
{
    var format = SimpleDateFormat("yyyy/MM/dd HH:mm")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_row_layout, null, true)


        val categoryText = rowView.findViewById(R.id.category) as TextView
        val dateText = rowView.findViewById(R.id.date) as TextView
        val workTime = rowView.findViewById(R.id.workTime) as TextView
        val datetextEnd = rowView.findViewById(R.id.dateEnd) as TextView


        categoryText.text = raports[position].Category
        dateText.text = raports[position].StartDate
        datetextEnd.text = format.format(getEndDate(raports[position].StartDate, raports[position].WorkTime))
        workTime.text  = "${raports[position].WorkTime/60} ${context.getString(R.string.hour)} ${context.getString(R.string.and)} ${raports[position].WorkTime%60} ${context.getString(R.string.minutes)}"

        return rowView
    }

    public fun getEndDate(startDate : String, workTime : Int): Date? {

        var mCalendar = Calendar.getInstance()
        mCalendar.setTime(format.parse(startDate))
        val hour = workTime/60 + mCalendar.get(Calendar.HOUR)
        val min = workTime%60 + mCalendar.get(Calendar.MINUTE)
        val day = mCalendar.get(Calendar.DAY_OF_MONTH)
        val month = mCalendar.get(Calendar.MONTH)+1
        val year = mCalendar.get(Calendar.YEAR)
        val date = format.parse("$year/$month/$day $hour:$min")

        return date
    }


}