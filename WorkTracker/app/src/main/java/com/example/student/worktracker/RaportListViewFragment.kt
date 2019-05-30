package com.example.student.worktracker


import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.annotation.MainThread
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import com.example.student.worktracker.Room.AppDb
import com.example.student.worktracker.Room.Raport
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_row_layout.*
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class RaportListViewFragment : Fragment() {
     var adapter : com.example.student.worktracker.ListAdapter?= null
     var db : AppDb? = null
     var RaportList: ListView? = null
     var listItemsWorkTime : Array<Int?>? =null
     var listItemsCategory  : Array<String?>? = null
     var listItemsDate : Array<String?>? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        db = AppDb(context!!)

        val myExecutor = Executors.newSingleThreadExecutor()
        myExecutor.execute{
            refreshList()
            adapter = com.example.student.worktracker.ListAdapter(activity!!, listItemsCategory!!, listItemsDate!!, listItemsWorkTime!!)
            activity!!.runOnUiThread(Runnable {
                RaportList!!.adapter = adapter
                adapter!!.notifyDataSetChanged()

            })

        }


    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        super.onDestroyView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(adapter != null)
        {
            val myExecutor = Executors.newSingleThreadExecutor()
            myExecutor.execute{
                refreshList()
            }
            adapter!!.notifyDataSetChanged()
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_raport_list_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.runOnUiThread(Runnable {
            RaportList = activity!!.findViewById(R.id.RaportsList)
        })


    }

    fun refreshList()
    {
        var raports = db!!.raportDao().getRaports()

        listItemsWorkTime = arrayOfNulls<Int>(raports.size)
        listItemsCategory = arrayOfNulls<String>(raports.size)
        listItemsDate = arrayOfNulls<String>(raports.size)

        for(i in 0 until raports.size)
        {
            val raport = raports[i]
            listItemsCategory!![i] = raport.Category
            listItemsDate!![i] = raport.StartDate
            listItemsWorkTime!![i] = raport.WorkTime
        }
    }





}
