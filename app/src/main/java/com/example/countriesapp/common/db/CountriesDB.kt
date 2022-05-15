package com.example.countriesapp.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.countriesapp.countries.data.db.CountriesDao
import com.example.countriesapp.countries.data.db.models.CountryEntity
import com.example.countriesapp.details.data.db.CountriesDetailsDao
import com.example.countriesapp.details.data.db.models.CountryDetailsEntity

@Database(
    entities = [CountryEntity::class, CountryDetailsEntity::class],
    exportSchema = false,
    version = 1
)
abstract class CountriesDB : RoomDatabase() {

    abstract fun countriesDao(): CountriesDao

    abstract fun countriesDetailsDao(): CountriesDetailsDao
}
