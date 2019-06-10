package com.example.student.worktracker.Room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface RaportDao
{
    @Insert
    fun addRaport(raport: Raport)

    @Query("SELECT * FROM raports ORDER BY StartDate")
    fun getRaports() : List<Raport>

    @Query("SELECT Category FROM raports GROUP BY Category ORDER BY Category")
    fun getCategories() : List<String>

    @Query("DELETE FROM raports WHERE StartDate = :startDate")
    fun deleteRaport(startDate: String)

    @Query("SELECT * FROM raports WHERE Category = :category ORDER BY StartDate")
    fun selectCategory(category: String): List<Raport>

}