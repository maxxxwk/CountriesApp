package com.example.countriesapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.countriesapp.common.viewmodel.ViewModelFactory
import com.example.countriesapp.countries.presentation.CountriesFragment
import com.example.countriesapp.details.presenation.CountryDetailsFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: MainActivityViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).component.inject(this)
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                when (it) {
                    is MainActivityState.Selected -> {
                        if (resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE
                            && it.id.isEmpty()
                        ) {
                            viewModel.unselect()
                        } else {
                            showCountryDetailsFragment(it.id)
                        }
                    }
                    MainActivityState.NotSelected -> {
                        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            viewModel.selectCountry("")
                        }
                    }
                }
            }
        }
        showCountriesListFragment()
    }

    private fun showCountriesListFragment() {
        supportFragmentManager.commit {
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    replace(
                        R.id.countriesListContainer,
                        CountriesFragment.newInstance(viewModel::selectCountry)
                    )
                }
                else -> {
                    replace(
                        R.id.container,
                        CountriesFragment.newInstance(viewModel::selectCountry)
                    )
                    addToBackStack(null)
                }
            }
        }
    }

    private fun showCountryDetailsFragment(id: String) {
        val fragment = CountryDetailsFragment.newInstance(id)
        supportFragmentManager.commit {
            setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> {
                    replace(R.id.countryDetailsContainer, fragment)
                }
                else -> {
                    add(R.id.container, fragment)
                    addToBackStack(null)
                }
            }
        }
    }

    override fun onBackPressed() {
        when {
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE -> {
                super.onBackPressed()
            }
            supportFragmentManager.backStackEntryCount > 1 -> {
                viewModel.unselect()
                super.onBackPressed()
            }
            else -> {
                moveTaskToBack(true)
            }
        }
    }
}
