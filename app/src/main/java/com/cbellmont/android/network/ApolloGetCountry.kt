package com.cbellmont.android.network

import android.content.Context
import android.widget.Toast
import com.cbellmont.android.graphqltest.MainApplication
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
import com.cbellmont.android.GetCountryDetailsQuery

class ApolloGetCountry {

    companion object {

        fun get(context: Context, country: Country) {
            GlobalScope.launch(Dispatchers.IO) {
                getFromApi(context, country)
            }
        }

        private fun getFromApi(context: Context, country: Country) {
            val query = GetCountryDetailsQuery.builder().code(country.code).build()

            MainApplication.myApolloClient.query(query)
                .enqueue(object : ApolloCall.Callback<GetCountryDetailsQuery.Data>() {
                    override fun onResponse(response: Response<GetCountryDetailsQuery.Data>) {
                        val data = response.data()
                        displayToast(context, "Detailed Response Received")

                        if (data == null) {
                            displayToast(context, "DATA ERROR")
                            return
                        }

                        var languages = ""
                        data.country()?.languages()?.forEach { language ->
                            languages = languages.plus(language.name()).plus(" ")
                        }

                        var neighbors = ""
                        data.country()?.continent()?.countries()?.forEach { neighbor ->
                            neighbors = neighbors.plus(neighbor.name()).plus(" ")
                        }

                        country.currency = data.country()?.currency()
                        country.languages = languages
                        country.neighborhood = neighbors

                        val countryDao = Db.invoke(context).countryDao()
                        val repository = CountryRepository(countryDao)
                        repository.update(country)
                    }

                    override fun onFailure(e: ApolloException) {
                        e.printStackTrace()
                    }
                })
        }

        private fun displayToast(context: Context, message: String) {
            GlobalScope.launch(Dispatchers.Main) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}