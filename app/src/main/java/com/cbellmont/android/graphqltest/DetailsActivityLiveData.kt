package com.cbellmont.android.graphqltest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cbellmont.android.network.ApolloGetCountry
import com.cbellmont.android.storage.*
import com.cbellmont.android.utils.CountryUtils
import com.cbellmont.android.viewmodel.CountryDetailsLiveDataViewModel
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.*
import java.util.*

class DetailsActivityLiveData : AppCompatActivity() {

    lateinit var model: CountryDetailsLiveDataViewModel
    private lateinit var mToolbar: Toolbar


    companion object {
        var INTENT_COUNTRY_ID = "COUNTRY_ID"

        fun getExampleDetailsIntent(context: Context): Intent {
            return getDetailsActivityLiveData(context, 1)
        }

        fun getDetailsActivityLiveData(context: Context, id: Int): Intent {
            return Intent(context, DetailsActivityLiveData::class.java).apply {
                putExtra(INTENT_COUNTRY_ID, id)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        mToolbar = findViewById(R.id.toolbarDetails)

        configureToolbar()
        val countryId = intent?.extras?.getInt(INTENT_COUNTRY_ID, -1)!!

        if (countryId == -1) {
            Toast.makeText(this, getString(R.string.toast_error_initial_data), Toast.LENGTH_LONG)
                .show()
            finish()
        }

        model = ViewModelProviders
            .of(this, CountryDetailsLiveDataViewModel.ViewModelFactory(this.application, countryId))
            .get(CountryDetailsLiveDataViewModel::class.java)

        val countryObserver = Observer<Country> { country ->
            showCountry(country)
        }
        model.countryDetails.observe(this, countryObserver)
        model.fetchData(this, countryId)
    }

    private fun configureToolbar() {
        setSupportActionBar(mToolbar)
        mToolbar.title = "Reversed"
    }

    private fun showCountry(country : Country?){
        if (country == null) {
            return
        }
        GlobalScope.launch(Dispatchers.Main){
            supportActionBar?.title = getString(R.string.toolbar_main_title)
            tw_name.text = String.format(Locale.ENGLISH, getString(R.string.country_name), country.name)
            tw_currency.text = String.format(Locale.ENGLISH, getString(R.string.currency), country.currency)
            tw_near_countries.text = String.format(Locale.ENGLISH, getString(R.string.near_countries), country.neighborhood)
            tw_languages.text = String.format(Locale.ENGLISH, getString(R.string.languages),country.languages)
        }
    }
}

