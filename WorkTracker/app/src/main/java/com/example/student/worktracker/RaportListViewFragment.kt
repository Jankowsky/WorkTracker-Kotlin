package com.example.student.worktracker


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.student.worktracker.Room.AppDb
import com.example.student.worktracker.Room.Raport
import kotlinx.android.synthetic.main.list_row_layout.*
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList


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
     //var listItemsWorkTime : ArrayList<String> = arrayListOf()
     //var listItemsCategory  : ArrayList<String> = arrayListOf()
     //var listItemsDate : ArrayList<String> = arrayListOf()
     var raports : ArrayList<Raport> = arrayListOf()




    override fun onAttach(context: Context?) {
        super.onAttach(context)
        db = AppDb(context!!)

        val myExecutor = Executors.newSingleThreadExecutor()
        myExecutor.execute{
            refreshList()
            adapter = ListAdapter(activity!!, raports)
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

            RaportList!!.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, view, position, _ ->

                showDialog(position)

                true
            }
        })

    }

    fun refreshList()
    {
        var getRaports = db!!.raportDao().getRaports()

        activity!!.runOnUiThread(Runnable {
            raports.clear()

            for (i in 0 until getRaports.size) {
                val raport = getRaports[i]
                raports.add(raport)
            }
            adapter!!.notifyDataSetChanged()
        })
    }

    private fun showDialog(position : Int){
        // Late initialize an alert dialog object
        lateinit var dialog: AlertDialog


        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(activity!!)

        // Set a title for alert dialog
        builder.setTitle(getString(R.string.alertTitle))

        // Set a message for alert dialog
        builder.setMessage(getString(R.string.alertQuestion))


        // On click listener for dialog buttons
        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {
                    removeItem(raports[position])
                    toast(getString(R.string.deleted))
                }
                DialogInterface.BUTTON_NEGATIVE -> toast(getString(R.string.aborted))
            }
        }


        // Set the alert dialog positive/yes button
        builder.setPositiveButton(getString(R.string.yes),dialogClickListener)

        // Set the alert dialog negative/no button
        builder.setNegativeButton(getString(R.string.no),dialogClickListener)



        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }

    fun removeItem(raport : Raport)
    {
        val myExecutor = Executors.newSingleThreadExecutor()
        myExecutor.execute {
            db!!.raportDao().deleteRaport(raport)
            refreshList()
        }
    }

    fun toast(message: String) {
        Toast.makeText(activity!!, message, Toast.LENGTH_SHORT).show()
    }

}
