package me.varoa.nongki.ui.screen.search

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import logcat.logcat
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentSearchBinding
import me.varoa.nongki.ext.navigateTo
import me.varoa.nongki.ext.toast
import me.varoa.nongki.ui.base.LocationFragment
import me.varoa.nongki.ui.base.OneTimeEvent.ShowErrorMessage
import me.varoa.nongki.ui.screen.search.SearchViewModel.NavigateToResult
import me.varoa.nongki.ui.screen.search.SearchViewModel.SearchFormData
import me.varoa.nongki.utils.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : LocationFragment(R.layout.fragment_search) {
    private val binding by viewBinding<FragmentSearchBinding>()
    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var eventJob: Job

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionLauncher = requestLocationPermissionLauncher { getCurrentLocation() }

    private var formData: SearchFormData
        get() = viewModel.formData.value
        set(value) {
            viewModel.updateFormData(value)
        }

    private val priceSliderSubtitles =
        arrayOf(
            "Sangat Murah",
            "Murah",
            "Biasa",
            "Mahal",
            "Sangat Mahal",
        )

    private val importanceSliderSubtitles =
        arrayOf(
            "Sangat Tidak Penting",
            "Tidak Penting",
            "Biasa",
            "Penting",
            "Sangat Penting",
        )

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        with(binding) {
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            with(layoutSearch) {
                btnSearch.setOnClickListener {
                    getCurrentLocation()
                    showLoading()
                }

                // kriteria harga
                sliderPrice.apply {
                    setLabelFormatter { value ->
                        val result = StringBuilder()
                        for (i in 1..value.toInt()) {
                            result.append("$")
                        }
                        result.toString()
                    }
                    addOnChangeListener { _, value, _ ->
                        tvCriteriaPrice.text = priceSliderSubtitles[value.toInt() - 1]
                        formData = formData.copy(price = value.toInt())
                    }
                }
                sliderFacility.apply {
                    addOnChangeListener { _, value, _ ->
                        tvCriteriaFacility.text = importanceSliderSubtitles[value.toInt() - 1]
                        formData = formData.copy(facility = value.toInt())
                    }
                }
                sliderLocation.apply {
                    addOnChangeListener { _, value, _ ->
                        tvCriteriaLocation.text = importanceSliderSubtitles[value.toInt() - 1]
                        formData = formData.copy(location = value.toInt())
                    }
                }
                sliderReputation.apply {
                    addOnChangeListener { _, value, _ ->
                        tvCriteriaReputation.text = importanceSliderSubtitles[value.toInt() - 1]
                        formData = formData.copy(reputation = value.toInt())
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        eventJob =
            viewModel
                .events
                .onEach { event ->
                    when (event) {
                        is NavigateToResult -> {
                            navigateTo(SearchFragmentDirections.actionSearchToResult(event.searchId))
                        }

                        is ShowErrorMessage -> {
                            logcat { "Error : ${event.message}" }
                            toast("Error : ${event.message}")
                        }
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    // cek lokasi
    private fun getCurrentLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    formData =
                        formData.copy(
                            userLat = location.latitude,
                            userLng = location.longitude,
                        )
                    viewModel.onSearch()
                }
            }
        } else {
            requestLocationPermission(locationPermissionLauncher)
        }
    }

    private fun showLoading() {
        with(binding) {
            loadingBar.isVisible = true
            layoutLoading.root.isVisible = true
            layoutSearch.root.isVisible = false
        }
    }
}
