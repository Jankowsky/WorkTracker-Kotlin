package com.example.student.worktracker

import android.content.Context
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.charts.BarLineChartBase
import android.support.v4.app.Fragment


class DayAxisValueFormatter(chart: BarLineChartBase<*>,context: Context) : ValueFormatter()
{

    private  val mMonths = context.resources.getStringArray(R.array.months)

    private var chart: BarLineChartBase<*>? = chart
    private var context: Context = context

    override fun getFormattedValue(value: Float): String {

        val days = value.toInt()

        val year = determineYear(days)

        val month = determineMonth(days)
        val monthName = mMonths[month % mMonths.size]
        val yearName = year.toString()

        if (chart!!.getVisibleXRange() > 30 * 6) {

            return "$monthName $yearName"
        } else {

            val dayOfMonth = determineDayOfMonth(days, month + 12 * (year - 2019))

            return if (dayOfMonth == 0) "" else "$dayOfMonth $monthName"
        }
    }

    private fun getDaysForMonth(month: Int, year: Int): Int {

        // month is 0-based

        if (month == 1) {
            var is29Feb = false

            if (year < 1582)
                is29Feb = (if (year < 1) year + 1 else year) % 4 == 0
            else if (year > 1582)
                is29Feb = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)

            return if (is29Feb) 29 else 28
        }

        return if (month == 3 || month == 5 || month == 8 || month == 10)
            30
        else
            31
    }

    private fun determineMonth(dayOfYear: Int): Int {

        var month = -1
        var days = 0

        while (days < dayOfYear) {
            month = month + 1

            if (month >= 12)
                month = 0

            val year = determineYear(days)
            days += getDaysForMonth(month, year)
        }

        return Math.max(month, 0)
    }

    private fun determineDayOfMonth(days: Int, month: Int): Int {

        var count = 0
        var daysForMonths = 0

        while (count < month) {

            val year = determineYear(daysForMonths)
            daysForMonths += getDaysForMonth(count % 12, year)
            count++
        }

        return days - daysForMonths
    }

    private fun determineYear(days: Int): Int {

        return if (days <= 366)
            2019
        else if (days <= 730)
            2020
        else if (days <= 1094)
            2021
        else if (days <= 1458)
            2022
        else
            2023

    }

}