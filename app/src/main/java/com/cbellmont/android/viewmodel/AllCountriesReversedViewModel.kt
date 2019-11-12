package com.cbellmont.android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.cbellmont.android.storage.*

class AllCountriesReversedViewModel(application: Application) : AndroidViewModel(application)  {
    private val repository: CountryRepository
    val allCountriesReversedLive: LiveData<Array<Country>>

    init {
        val dao = Db.invoke(application).countryDao()
        repository = CountryRepository(dao)
        allCountriesReversedLive = repository.getAllOrderedByNameDescLiveData()
    }

}