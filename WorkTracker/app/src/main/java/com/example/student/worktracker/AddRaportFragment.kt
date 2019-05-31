package com.example.student.worktracker


import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.student.worktracker.Room.AppDb
import com.example.student.worktracker.Room.Raport
import kotlinx.android.synthetic.main.list_row_layout.*
import java.util.*
import java.util.concurrent.Executors
import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.widget.DatePicker
import android.app.DatePickerDialog
import android.widget.TimePicker
import android.app.TimePickerDialog






// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AddRaportFragment : Fragment() {

    var db : AppDb? = null
    var addButton : Button? = null
    var entryCategory : EditText? = null
    var selectDateBtn : Button? = null
    var selectStartTimeBtn : Button? = null
    var selectEndTimeBtn : Button? = null
    var in_date : EditText? = null
    var in_startTime : EditText? = null
    var in_endTime : EditText? = null

    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDb(context!!)
        activity!!.runOnUiThread(Runnable {
            addButton = activity!!.findViewById<Button>(R.id.addRaportBtn)
            selectDateBtn = activity!!.findViewById<Button>(R.id.selectDateBtn)
            selectStartTimeBtn = activity!!.findViewById<Button>(R.id.selectStartTimeBtn)
            selectEndTimeBtn = activity!!.findViewById<Button>(R.id.selectEndTimeBtn)
            entryCategory = activity!!.findViewById<EditText>(R.id.entryCompany)
            in_date = activity!!.findViewById<EditText>(R.id.in_date)
            in_startTime = activity!!.findViewById<EditText>(R.id.in_startTime)
            in_endTime = activity!!.findViewById<EditText>(R.id.in_endTime)

                addButton!!.setOnClickListener {
                    addRaport()
                    hideSoftKeyboard(activity!!)
                    //(activity as MainActivity).createRaportListViewFragment()
                    //hideSoftKeyboard(activity!!)
                    Toast.makeText(activity!!, "added raport", Toast.LENGTH_SHORT).show()
                }

                selectDateBtn!!.setOnClickListener {
                    val c = Calendar.getInstance()
                    mYear = c.get(Calendar.YEAR)
                    mMonth = c.get(Calendar.MONTH)
                    mDay = c.get(Calendar.DAY_OF_MONTH)


                    val datePickerDialog = DatePickerDialog(activity,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            in_date!!.setText(
                                dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                            )
                        }, mYear, mMonth, mDay
                    )
                    datePickerDialog.show()
                }

                selectStartTimeBtn!!.setOnClickListener {
                    val c = Calendar.getInstance()
                    mHour = c.get(Calendar.HOUR_OF_DAY)
                    mMinute = c.get(Calendar.MINUTE)

                    // Launch Time Picker Dialog
                    val timePickerDialog = TimePickerDialog(activity,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> in_startTime!!.setText("$hourOfDay:$minute") },
                        mHour,
                        mMinute,
                        false
                    )
                    timePickerDialog.show()
                }

                selectEndTimeBtn!!.setOnClickListener {
                    val c = Calendar.getInstance()
                    mHour = c.get(Calendar.HOUR_OF_DAY)
                    mMinute = c.get(Calendar.MINUTE)

                    // Launch Time Picker Dialog
                    val timePickerDialog = TimePickerDialog(activity,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> in_endTime!!.setText("$hourOfDay:$minute") },
                        mHour,
                        mMinute,
                        false
                    )
                    timePickerDialog.show()
                }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_add_raport, container, false)
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if(inputMethodManager.isActive)
        {
            //inputMethodManager.hideSoftInputFromWindow( activity.currentFocus!!.windowToken, 0 )
            inputMethodManager.hideSoftInputFromWindow( getView()!!.getRootView().getWindowToken(), 0 )

        }
    }

    fun addRaport()
    {
        val myExecutor = Executors.newSingleThreadExecutor()
        myExecutor.execute {

            var raport = Raport()
            raport.Category = entryCategory!!.text.toString()
            raport.StartDate = Date().toString()
            raport.WorkTime = 220

            db!!.raportDao().addRaport(raport)

            //db!!.beginTransaction()
            activity!!.runOnUiThread(Runnable {
                entryCategory!!.setText("")
            })
        }

    }

}
