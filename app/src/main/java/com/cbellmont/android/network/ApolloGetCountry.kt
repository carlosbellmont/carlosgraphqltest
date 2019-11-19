package com.cbellmont.android.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.cbellmont.android.graphqltest.MainApplication
import com.cbellmont.android.graphqltest.R
import com.cbellmont.android.storage.Country
import com.cbellmont.android.storage.CountryRepository
import com.cbellmont.android.storage.Db
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.cbellmont.android.GetCountryDetailsQuery
import kotlinx.coroutines.withContext

class ApolloGetCountry {

    companion object {

        suspend fun get(context: Context, country: Country) {
            getFromApi(context, country)
        }

        private suspend fun getFromApi(context: Context, country: Country) {
            val query = GetCountryDetailsQuery.builder().code(country.code).build()
            val defer = MainApplication.myApolloClient.query(query).toDeferred()
            val response = defer.await()
            var languages = ""
            response.data()?.country()?.languages()?.forEach { language ->
                languages = languages.plus(language.name()).plus(" ")
            }

            var neighbors = ""
            response.data()?.country()?.continent()?.countries()?.forEach { neighbor ->
                neighbors = neighbors.plus(neighbor.name()).plus(" ")
            }
            country.currency = response.data()?.country()?.currency()
            country.languages = languages
            country.neighborhood = neighbors

            val countryDao = Db.invoke(context).countryDao()
            val repository = CountryRepository(countryDao)
            repository.update(country)
        }

        private fun displayToast(context: Context, message: String) {
            GlobalScope.launch(Dispatchers.Main) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}