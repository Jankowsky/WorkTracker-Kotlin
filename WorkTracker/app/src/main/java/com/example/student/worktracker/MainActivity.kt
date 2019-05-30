package com.example.student.worktracker

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.student.worktracker.Room.AppDb
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val manager = supportFragmentManager


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                createRaportListViewFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                createAddRaportFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                createRaportDiagramsFragment()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createRaportListViewFragment()

        //var t = Thread(Runnable {
           // var db = Room.databaseBuilder(applicationContext, AppDb:: class.java, "WorkTimeDb").build()

            //var raport = Raport()
            //raport.Category = "X"
            //raport.StartDate = Date().toString()
            //raport.WorkTime = 8

            //db.raportDao().addRaport(raport)



          //  db.raportDao().getRaports().forEach{
           //     Log.i("@AKTDEV", "Id "+ it.id)
           //     Log.i("@AKTDEV", "Category "+ it.Category)
           //     Log.i("@AKTDEV", "Date  "+ it.StartDate)
            //}
        //}).start()





        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


    fun createRaportListViewFragment()
    {
        val transaction = manager.beginTransaction()
        val fragment = RaportListViewFragment()
        transaction.replace(R.id.fragmentHolder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun createAddRaportFragment()
    {
        val transaction = manager.beginTransaction()
        val fragment = AddRaportFragment()
        transaction.replace(R.id.fragmentHolder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun createRaportDiagramsFragment()
    {
        val transaction = manager.beginTransaction()
        val fragment = RaportDiagramsFragment()
        transaction.replace(R.id.fragmentHolder, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
