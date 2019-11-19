package com.cbellmont.android.graphqltest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cbellmont.android.adapters.CountryAdapter
import com.cbellmont.android.storage.Country
import com.cbellmont.android.viewmodel.AllCountriesReversedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AllCountriesReversedFragment : Fragment() , CountryAdapter.OnItemClickListener  {

    private lateinit var adapter : CountryAdapter
    private lateinit var allCountriesReversedViewModel: AllCountriesReversedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allCountriesReversedViewModel = ViewModelProviders.of(this).get(AllCountriesReversedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_all_countries_reversed, container, false)
        val recycler = rootView.findViewById<RecyclerView>(R.id.recycler_view_all_countries_reversed)
        recycler.layoutManager = LinearLayoutManager(context)

        adapter = CountryAdapter(emptyArray(), this)
        recycler.adapter = adapter

        allCountriesReversedViewModel.allCountriesReversedLive.observe(this, Observer { country ->
            country?.let { adapter.setData(it) }
        })

        return rootView
    }

    override fun onItemClick(item: Country?, position: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            if (item == null){
                return@launch
            }
            startActivity(DetailsActivityLiveData.getDetailsActivityLiveData(context!!, item.id))
        }
    }
}