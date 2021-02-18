package com.ruslanpark.kakeiboapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ruslanpark.kakeiboapp.data.TableDatabase
import com.ruslanpark.kakeiboapp.model.Table
import com.ruslanpark.kakeiboapp.repository.TableRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TableViewModel(application: Application) : AndroidViewModel(application) {

    var readAllData : MutableLiveData<MutableList<Table>> = MutableLiveData()
    private val repository : TableRepository

    init {
        val tableDao = TableDatabase.getDatabase(application).tableDao()
        repository = TableRepository(tableDao)
        viewModelScope.launch(Dispatchers.IO) {
            readAllData.postValue(repository.findAllData())
        }
    }

    fun insertAllData(tables : MutableList<Table>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAllData(tables)
            readAllData.postValue(repository.findAllData())
        }
    }

    fun insertData(table : Table) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(table)
            readAllData.postValue(repository.findAllData())
        }
    }

    fun clearData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearData()
        }
    }
}