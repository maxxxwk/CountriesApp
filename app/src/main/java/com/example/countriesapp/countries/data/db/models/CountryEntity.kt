package com.example.countriesapp.countries.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.countriesapp.GetCountriesListQuery
import com.example.countriesapp.countries.domain.Country

@Entity(tableName = "countries")
data class CountryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val nativeName: String,
    val emoji: String
) {
    fun toCountry() = Country(
        id = id,
        name = name,
        nativeName = nativeName,
        emoji = emoji
    )
}

fun GetCountriesListQuery.Country.toCountryEntity(): CountryEntity {
    return CountryEntity(
        id = code,
        name = name,
        nativeName = native,
        emoji = emoji
    )
}