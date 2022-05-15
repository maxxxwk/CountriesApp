package com.example.countriesapp.di

import android.content.Context
import androidx.room.Room
import com.example.countriesapp.common.db.CountriesDB
import com.example.countriesapp.countries.data.db.CountriesDao
import com.example.countriesapp.details.data.db.CountriesDetailsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    fun provideCountriesDetailsDao(countriesDB: CountriesDB): CountriesDetailsDao {
        return countriesDB.countriesDetailsDao()
    }

    @Provides
    fun provideCountriesDao(countriesDB: CountriesDB): CountriesDao {
        return countriesDB.countriesDao()
    }

    @Provides
    @Singleton
    fun provideCountriesDB(context: Context): CountriesDB {
        return Room.databaseBuilder(context, CountriesDB::class.java, DB_NAME).build()
    }

    private companion object {
        const val DB_NAME = "CountriesDatabase"
    }
}
