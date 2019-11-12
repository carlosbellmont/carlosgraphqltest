package com.cbellmont.android.graphqltest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.cbellmont.android.network.ApolloGetCountry
import com.cbellmont.android.storage.*
import com.cbellmont.android.utils.CountryUtils
import com.cbellmont.android.viewmodel.CountryDetailViewModel
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.*
import java.util.*

class DetailsActivity : AppCompatActivity() {

    lateinit var model: CountryDetailViewModel
    private lateinit var mToolbar: Toolbar

    companion object{
        var INTENT_COUNTRY_ID = "COUNTRY_ID"

        fun getExampleDetailsIntent(context: Context): Intent{
            return getDetailsIntent(context, 1)
        }

        fun getDetailsIntent(context: Context, id : Int): Intent {
            return Intent(context, DetailsActivity::class.java).apply {
                putExtra(INTENT_COUNTRY_ID, id)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }


    private var id : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        mToolbar = findViewById(R.id.toolbarDetails)

        configureToolbar()
        id = intent?.extras?.getInt(INTENT_COUNTRY_ID, -1)!!

        if (id == -1){
            Toast.makeText(this, getString(R.string.toast_error_initial_data), Toast.LENGTH_LONG).show()
            finish()
        }

        model = ViewModelProviders
            .of(this, CountryDetailViewModel.ViewModelFactory(this.application, id))
            .get(CountryDetailViewModel::class.java)

        model.countryDetails.observe(this, androidx.lifecycle.Observer { country ->
            country?.let {
                Log.e("CARLOS TAG", "Calledddddddd")
                showCountry(model.countryDetails.value)
            }
        })

        model.fetchData(this)
        //loadDetails(id)
    }


    private fun configureToolbar() {
        setSupportActionBar(mToolbar)
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



    private fun loadDetails(id : Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val country = CountryRepository(Db.invoke(application).countryDao()).getById(id)
            if (CountryUtils.isComplete(country)) {
                showCountry(country)
            } else {

                ApolloGetCountry.get(this@DetailsActivity, country)
//                //TODO This is a fast trick to update the UI! Must be changed as sooner as possible.
//                delay(400)
//                showCountry(country)
            }

        }
    }
}

