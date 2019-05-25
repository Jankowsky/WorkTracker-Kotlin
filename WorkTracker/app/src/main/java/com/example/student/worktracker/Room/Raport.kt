package com.example.student.worktracker.Room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.sql.Date
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "raports")
class Raport
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var Category : String = ""
    var StartDate : String? = null
    var WorkTime : Int? = null

}