package com.example.countriesapp.details.presenation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.countriesapp.App
import com.example.countriesapp.R
import com.example.countriesapp.common.viewmodel.ViewModelFactory
import com.example.countriesapp.databinding.CountryDetailsFragmentBinding
import com.example.countriesapp.utils.hide
import com.example.countriesapp.utils.show
import com.example.countriesapp.utils.stringArgument
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class CountryDetailsFragment : Fragment(R.layout.country_details_fragment) {

    private val binding by viewBinding(CountryDetailsFragmentBinding::bind)
    private val countryId by stringArgument(COUNTRY_ID_KEY)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: CountryDetailsFragmentViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireContext().applicationContext as App).component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                handleState(it)
            }
        }
        if (countryId.isNotEmpty()) {
            viewModel.load(countryId)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleState(state: CountryDetailsFragmentState) = with(binding) {
        when (state) {
            is CountryDetailsFragmentState.Content -> {
                hide(pbLoading, tvErrorMessage, tvPendingMessage)
                show(gContent)
                tvCountryName.text = "${state.countryDetails.emoji} ${state.countryDetails.name}"
                tvCountryNativeName.text = state.countryDetails.nativeName
                tvCapital.text = getString(R.string.capital_text, state.countryDetails.capital)
                tvContinent.text =
                    getString(R.string.continent_text, state.countryDetails.continent)
                tvCurrency.text = getString(R.string.currency_text, state.countryDetails.currency)
                tvPhoneCode.text =
                    getString(R.string.phone_code_text, state.countryDetails.phoneCode)
            }
            is CountryDetailsFragmentState.Error -> {
                hide(pbLoading, gContent, tvPendingMessage)
                show(tvErrorMessage)
                tvErrorMessage.text = state.errorMessage
            }
            CountryDetailsFragmentState.Loading -> {
                hide(gContent, tvErrorMessage, tvPendingMessage)
                show(pbLoading)
            }
            CountryDetailsFragmentState.PendingSelect -> {
                hide(gContent, tvErrorMessage, pbLoading)
                show(tvPendingMessage)
                tvPendingMessage.text = getString(R.string.country_details_fragment_pending_message)
            }
        }
    }

    companion object {
        private const val COUNTRY_ID_KEY = "COUNTRY_ID_KEY"

        fun newInstance(id: String) = CountryDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(COUNTRY_ID_KEY, id)
            }
        }

        fun newInstance(): CountryDetailsFragment {
            return newInstance("")
        }
    }
}
