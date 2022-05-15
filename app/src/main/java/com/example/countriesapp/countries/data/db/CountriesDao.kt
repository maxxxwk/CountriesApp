package com.example.countriesapp.countries.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.countriesapp.countries.data.db.models.CountryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountries(countries: List<CountryEntity>)

    @Query("SELECT * from countries")
    fun getCountries(): Flow<List<CountryEntity>>
}
