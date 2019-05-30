package com.example.student.worktracker


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner

import com.example.student.worktracker.R
import com.example.student.worktracker.Room.AppDb
import java.util.concurrent.Executors

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
    var categoryList : Array<String?>? = null

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

            activity!!.runOnUiThread(Runnable {
                spinner!!.adapter = adapter
            })
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.runOnUiThread(Runnable {
            spinner = activity!!.findViewById(R.id.spinner)
        })
    }
}
