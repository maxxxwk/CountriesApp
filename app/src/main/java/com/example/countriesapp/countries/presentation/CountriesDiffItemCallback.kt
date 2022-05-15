package com.example.countriesapp.countries.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.countriesapp.countries.domain.Country

class CountriesDiffItemCallback : DiffUtil.ItemCallback<Country>() {

    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem == newItem
    }
}
