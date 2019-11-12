package com.cbellmont.android.storage

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class CountryRepository(private val CountryDao: CountryDao) {


    @WorkerThread
    fun getAllOrderedByNameAscLiveData() : LiveData<Array<Country>> {
        return CountryDao.getAllOrderedByNameAsc()
    }

    @WorkerThread
    fun getAllOrderedByNameDescLiveData() : LiveData<Array<Country>> {
        return CountryDao.getAllOrderedByNameDesc()
    }

    @WorkerThread
    fun getById(id: Int) : Country {
        return CountryDao.loadById(id)
    }

    @WorkerThread
    fun insert(forecastList: Country) {
        CountryDao.insert(forecastList)
    }

    @WorkerThread
    fun insertAll(forecastList: Array<Country>) {
        CountryDao.insertAll(forecastList)
    }

    @WorkerThread
    fun insertAll(forecastList: ArrayList<Country>) {
        CountryDao.insertAll(forecastList)
    }

    @WorkerThread
    fun deleteAll() {
        CountryDao.deleteAll()
    }

    @WorkerThread
    fun update(forecastList: Country) {
        CountryDao.update(forecastList)
    }

}