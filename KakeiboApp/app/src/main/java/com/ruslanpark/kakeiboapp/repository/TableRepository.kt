package com.ruslanpark.kakeiboapp.repository

import com.ruslanpark.kakeiboapp.data.TableDao
import com.ruslanpark.kakeiboapp.model.Table

class TableRepository(private val tableDao : TableDao) {

    fun insertAllData(tables: MutableList<Table>) = tableDao.insertAllData(tables)

    fun insertData(table: Table) = tableDao.insertData(table)

    fun findAllData() : MutableList<Table> = tableDao.findAllData()

    fun clearData() = tableDao.clearData()
}