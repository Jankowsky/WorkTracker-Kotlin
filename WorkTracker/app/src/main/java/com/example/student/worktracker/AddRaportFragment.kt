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



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDb(context!!)
        activity!!.runOnUiThread(Runnable {
            addButton = activity!!.findViewById<Button>(R.id.addRaportBtn)
            entryCategory = activity!!.findViewById<EditText>(R.id.entryCompany)

                addButton!!.setOnClickListener {
                    addRaport()
                    hideSoftKeyboard(activity!!)
                    //(activity as MainActivity).createRaportListViewFragment()
                    //hideSoftKeyboard(activity!!)
                    Toast.makeText(activity!!, "added raport", Toast.LENGTH_SHORT).show()
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
