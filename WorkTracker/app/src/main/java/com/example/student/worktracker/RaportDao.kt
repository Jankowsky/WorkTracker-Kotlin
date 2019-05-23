package com.example.student.worktracker

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert

@Dao
interface RaportDao
{
    @Insert
    fun addRaport(raport: Raport)
}