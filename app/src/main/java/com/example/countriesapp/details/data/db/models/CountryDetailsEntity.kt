package com.example.countriesapp.details.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.countriesapp.GetCountryDetailsQuery
import com.example.countriesapp.details.domain.CountryDetails

@Entity(tableName = "details")
data class CountryDetailsEntity(
    @PrimaryKey val id: String,
    val name: String,
    val nativeName: String,
    val emoji: String,
    val capital: String,
    val currency: String,
    val phoneCode: String,
    val continent: String
) {
    fun toCountryDetails(): CountryDetails {
        return CountryDetails(
            name = name,
            nativeName = nativeName,
            emoji = emoji,
            capital = capital,
            currency = currency,
            phoneCode = phoneCode,
            continent = continent
        )
    }
}

fun GetCountryDetailsQuery.Country.toCountryDetailsEntity(): CountryDetailsEntity {
    return CountryDetailsEntity(
        id = code,
        name = name,
        nativeName = native,
        emoji = emoji,
        capital = capital.orEmpty(),
        currency = currency.orEmpty(),
        phoneCode = phone,
        continent = continent.name
    )
}
