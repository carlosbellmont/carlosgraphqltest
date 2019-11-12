package com.cbellmont.android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.cbellmont.android.storage.Country
import com.cbellmont.android.storage.CountryRepository
import com.cbellmont.android.storage.Db

class AllCountriesViewModel(application: Application) : AndroidViewModel(application)  {
    private val repository: CountryRepository
    val allCountriesLive: LiveData<Array<Country>>

    init {
        val dao = Db.invoke(application).countryDao()
        repository = CountryRepository(dao)
        allCountriesLive = repository.getAllOrderedByNameAscLiveData()
    }

}