package com.cbellmont.android.graphqltest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.cbellmont.android.network.ApolloGetCountries
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var mAppBar: AppBarLayout
    private lateinit var mToolbar: Toolbar
    private lateinit var mTabLayout: TabLayout
    private lateinit var mPager: ViewPager

    private val numOptions = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAppBar = findViewById(R.id.appbar)
        mToolbar = findViewById(R.id.toolbarHome)
        mTabLayout = findViewById(R.id.tabsHome)
        mPager = findViewById(R.id.containerHome)

        configureToolbar()
        configureTabBar()
        addTabUpcoming()
        addTabHottest()

        ApolloGetCountries.get(this)
        //ForecastApi.getFromApi(this)
    }

    private fun configureToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.title = getString(R.string.toolbar_main_title)
    }


    private fun configureTabBar() {
        // Create the adapter that will return a fragment
        // for each sections.
        val adapter = ForecastPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        mPager.adapter = adapter

        // Listener to communicate ViewPager and TabLayout
        mPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mTabLayout))
        mTabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(mPager))
    }


    private fun addTabUpcoming() {
        mTabLayout.addTab(mTabLayout.newTab().apply {
            text = getString(R.string.tab_upcoming_title)
        })
    }

    private fun addTabHottest() {
        mTabLayout.addTab(mTabLayout.newTab().apply {
            text = getString(R.string.tab_hottest_title)
        })
    }


    inner class ForecastPagerAdapter internal constructor(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return numOptions
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> AllCountriesFragment()

                else -> AllCountriesReversedFragment()
            }
        }
    }
}
