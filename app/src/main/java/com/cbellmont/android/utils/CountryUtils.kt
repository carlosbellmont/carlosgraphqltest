package com.cbellmont.android.utils

import com.cbellmont.android.storage.Country

class CountryUtils {

    companion object {
        fun isComplete(country: Country) : Boolean {
            return country.name != null && country.code != null && country.languages != null && country.neighborhood != null
        }
    }
}