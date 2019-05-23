package com.example.student.worktracker

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Date
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var t = Thread(Runnable {
            var db = Room.databaseBuilder(applicationContext,AppDb :: class.java, "WorkTimeDb").build()

            var raport = Raport()
            raport.Category = "X"
            raport.StartDate = Date().toString()
            raport.WorkTime = 8

            db.raportDao().addRaport(raport)
        }).start()





        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
