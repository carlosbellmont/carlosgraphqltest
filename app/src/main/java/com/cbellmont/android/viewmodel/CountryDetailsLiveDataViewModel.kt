package com.cbellmont.android.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.cbellmont.android.network.ApolloGetCountry
import com.cbellmont.android.storage.Country
import com.cbellmont.android.storage.CountryRepository
import com.cbellmont.android.storage.Db
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CountryDetailsLiveDataViewModel(application: Application, countryId: Int) : ViewModel() {
    var countryDetails: LiveData<Country> = MutableLiveData()
    private val repository: CountryRepository

    init {
        val dao = Db.invoke(application).countryDao()
        repository = CountryRepository(dao)
        countryDetails = repository.getByIdLiveData(countryId)

    }

    class ViewModelFactory(private var application: Application, private var countryId:Int): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CountryDetailsLiveDataViewModel(application, countryId) as T
        }
    }

    fun fetchData(context: Context, countryId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            ApolloGetCountry.get(context, repository.getById(countryId))
        }
    }
}


