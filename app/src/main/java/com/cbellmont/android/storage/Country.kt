package com.cbellmont.android.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Country (
    @PrimaryKey(autoGenerate = true) val id : Int,
    val name: String?,
    val code: String?,
    var currency: String?,
    var languages: String?,
    var neighborhood: String?
)