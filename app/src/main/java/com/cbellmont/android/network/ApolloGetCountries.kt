package com.cbellmont.android.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.cbellmont.android.GetAllCountriesQuery
import com.cbellmont.android.graphqltest.MainApplication.Companion.myApolloClient
import com.cbellmont.android.graphqltest.R
import com.cbellmont.android.storage.Country
import com.cbellmont.android.storage.CountryRepository
import com.cbellmont.android.storage.Db
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ApolloGetCountries {

    companion object {

        fun get(context : Context){
            GlobalScope.launch(Dispatchers.IO) {
                getFromApi(context)
            }
        }

        private fun getFromApi(context: Context){
            val queryGetAllCountries = GetAllCountriesQuery.builder().build()

            myApolloClient.query(queryGetAllCountries).enqueue(object : ApolloCall.Callback<GetAllCountriesQuery.Data>() {
                override fun onResponse(response: Response<GetAllCountriesQuery.Data>) {
                    val data = response.data()
                    displayToast(context, "Response Received")
                    Log.d("APOLLO TAG", "Size = " + (data?.countries()?.size ?: "empty."))

                    if (data == null) {
                        displayToast(context, "DATA ERROR")
                        return
                    }
                    if (data.countries() == null) {
                        displayToast(context, "DATA ERROR")
                        return
                    }

                    val countryList = ArrayList<Country>()

                    for (country in data.countries()!!) {
                        if (!country.name().isNullOrEmpty()){
                            Log.d("APOLLO TAG", country.name())
                            countryList.add(Country(0, country.name(), country.code(), null, null, null))
                        }
                    }
                    val countryDao = Db.invoke(context).countryDao()
                    val repository = CountryRepository(countryDao)
                    repository.deleteAll()
                    repository.insertAll(countryList)
                }

                override fun onFailure(e: ApolloException) {
                    e.printStackTrace()
                    displayToast(context, context.getString(R.string.toast_internet_error))
                }
            })
        }

        private fun displayToast(context: Context, message: String){
            GlobalScope.launch(Dispatchers.Main) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}