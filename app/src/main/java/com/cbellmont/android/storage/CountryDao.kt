package com.cbellmont.android.storage

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CountryDao {

    @Query("SELECT * FROM country")
    fun getAll(): Array<Country>

    @Query("SELECT * FROM country ORDER BY name ASC")
    fun getAllOrderedByNameAsc(): LiveData<Array<Country>>

    @Query("SELECT * FROM country ORDER BY name DESC")
    fun getAllOrderedByNameDesc(): LiveData<Array<Country>>

    @Query("SELECT * FROM Country WHERE id IN (:countryId)")
    fun loadAllByIds(countryId: IntArray): List<Country>

    @Query("SELECT * FROM Country WHERE id == :countryId")
    fun loadById(countryId: Int): Country

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(countryList: Array<Country>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(countryList: ArrayList<Country>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(country: Country)

    @Update
    fun update(country: Country)

    @Query("DELETE FROM country")
    fun deleteAll()
}
