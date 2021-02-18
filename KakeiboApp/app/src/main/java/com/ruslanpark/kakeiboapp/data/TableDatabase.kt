package com.ruslanpark.kakeiboapp.data

import android.content.Context
import androidx.room.*
import com.ruslanpark.kakeiboapp.model.PurchasesTypeConverter
import com.ruslanpark.kakeiboapp.model.Table

@Database(entities = [Table::class], version = 1)
@TypeConverters(PurchasesTypeConverter::class)
abstract class TableDatabase : RoomDatabase() {

    abstract fun tableDao() : TableDao

    companion object {
        @Volatile
        private var INSTANCE: TableDatabase? = null

        fun getDatabase(context: Context): TableDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TableDatabase::class.java,
                    "DATABASE"
                ).build()

                INSTANCE = instance
                return instance
            }
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}