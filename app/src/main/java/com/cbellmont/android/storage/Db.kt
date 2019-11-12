package com.cbellmont.android.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Country::class],
    version = 1)

abstract class Db : RoomDatabase() {

    abstract fun countryDao(): CountryDao

    companion object {
        @Volatile private var instance: Db? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            Db::class.java, "example.db")
            .build()
    }

}