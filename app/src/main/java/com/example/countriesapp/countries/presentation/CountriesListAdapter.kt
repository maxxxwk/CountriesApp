package com.example.countriesapp.countries.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.countriesapp.countries.domain.Country
import com.example.countriesapp.databinding.CountriesListItemBinding

class CountriesListAdapter(private val onItemSelected: (String) -> Unit) :
    ListAdapter<Country, CountriesListAdapter.CountryListItemViewHolder>(CountriesDiffItemCallback()) {

    class CountryListItemViewHolder(
        private val binding: CountriesListItemBinding,
        private val onItemSelected: (String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(country: Country) = with(binding) {
            llCountryItemContainer.setOnClickListener {
                onItemSelected(country.id)
            }
            val typedValue = TypedValue()
            llCountryItemContainer.context.theme.resolveAttribute(
                android.R.attr.selectableItemBackground,
                typedValue,
                true
            )
            llCountryItemContainer.setBackgroundResource(typedValue.resourceId)
            tvCountryName.text = "${country.emoji} ${country.name}"
            tvCountryNativeName.text = country.nativeName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryListItemViewHolder {
        return CountryListItemViewHolder(
            CountriesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemSelected
        )
    }

    override fun onBindViewHolder(holder: CountryListItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
