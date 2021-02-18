package com.ruslanpark.kakeiboapp.data

import androidx.room.*
import com.ruslanpark.kakeiboapp.model.Table

@Dao
interface TableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllData(tables : MutableList<Table>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(table : Table)

    @Query("SELECT * FROM purchaseTable")
    fun findAllData(): MutableList<Table>

    @Query("DELETE FROM purchaseTable")
    fun clearData()
}