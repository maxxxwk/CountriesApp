package com.example.countriesapp.details.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.countriesapp.details.data.db.models.CountryDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountriesDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountryDetails(countryDetailsEntity: CountryDetailsEntity)

    @Query("SELECT * FROM details WHERE id=:id")
    fun getCountryDetails(id: String): Flow<CountryDetailsEntity?>
}
