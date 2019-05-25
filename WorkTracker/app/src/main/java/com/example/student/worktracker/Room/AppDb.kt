package com.example.student.worktracker.Room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(Raport::class)], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase()
{
    abstract fun raportDao() : RaportDao
}