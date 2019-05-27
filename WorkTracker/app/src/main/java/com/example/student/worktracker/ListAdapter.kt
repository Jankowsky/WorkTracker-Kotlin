package com.example.student.worktracker

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ListAdapter (private val context: Activity, private val category: Array<String?>, private val date: Array<String?>,private val worktime: Array<Int?>)
    : ArrayAdapter<String>(context, R.layout.list_row_layout, category)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_row_layout, null, true)


        val categoryText = rowView.findViewById(R.id.category) as TextView
        val dateText = rowView.findViewById(R.id.date) as TextView
        val workTime = rowView.findViewById(R.id.workTime) as TextView


        categoryText.text = category[position]
        dateText.text = date[position]
        workTime.text = worktime[position].toString()

        return rowView
    }
}