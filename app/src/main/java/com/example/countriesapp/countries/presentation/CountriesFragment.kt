package com.example.countriesapp.countries.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.countriesapp.App
import com.example.countriesapp.R
import com.example.countriesapp.common.viewmodel.ViewModelFactory
import com.example.countriesapp.databinding.CountriesFragmentBinding
import com.example.countriesapp.utils.hide
import com.example.countriesapp.utils.show
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class CountriesFragment : Fragment(R.layout.countries_fragment) {

    private val binding by viewBinding(CountriesFragmentBinding::bind)
    private var onCountrySelect: (String) -> Unit = {}
    private val adapter = CountriesListAdapter(::onCountriesListItemSelected)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: CountriesFragmentViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                handleState(it)
            }
        }
    }

    private fun handleState(state: CountriesFragmentState) = with(binding) {
        when (state) {
            CountriesFragmentState.Loading -> {
                hide(rvCountriesList, tvErrorMessage)
                show(pbLoading)
            }
            is CountriesFragmentState.Content -> {
                hide(pbLoading, tvErrorMessage)
                show(rvCountriesList)
                adapter.submitList(state.countries)
            }
            is CountriesFragmentState.Error -> {
                hide(rvCountriesList, pbLoading)
                tvErrorMessage.apply {
                    text = state.errorMessage
                    show()
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvCountriesList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvCountriesList.adapter = adapter
    }

    private fun onCountriesListItemSelected(id: String) {
        onCountrySelect(id)
    }

    companion object {
        fun newInstance(onCountrySelect: (String) -> Unit) = CountriesFragment().apply {
            this.onCountrySelect = onCountrySelect
        }
    }
}
