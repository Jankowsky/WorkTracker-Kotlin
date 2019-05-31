package com.example.student.worktracker.Room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [(Raport::class)], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase()
{
    abstract fun raportDao() : RaportDao

    companion object {
        @Volatile
        private var instance: AppDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDb::class.java, "WorkDb.db"
        )
            .build()
    }
}