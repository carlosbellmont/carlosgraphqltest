package com.cbellmont.android.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cbellmont.android.network.ApolloGetCountry
import com.cbellmont.android.storage.Country
import com.cbellmont.android.storage.CountryRepository
import com.cbellmont.android.storage.Db
import com.cbellmont.android.utils.CountryUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CountryDetailViewModel(application: Application, countryId: Int) : ViewModel() {
    var countryDetails: LiveData<Country> = MutableLiveData()
    private val repository: CountryRepository

    init {
        val dao = Db.invoke(application).countryDao()
        repository = CountryRepository(dao)
        GlobalScope.launch(Dispatchers.IO) {
            countryDetails = repository.getByIdLiveData(countryId)
        }
    }

    class ViewModelFactory(private var application: Application, private var countryId:Int): ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CountryDetailViewModel(application, countryId) as T
        }

    }

    fun fetchData(context: Context){
        if (countryDetails.value == null) {
            return
        }
        if (CountryUtils.isComplete(countryDetails.value!!)){
            return
        }
        GlobalScope.launch(Dispatchers.IO) {
            ApolloGetCountry.get(context, countryDetails.value!!)
        }
    }
}


