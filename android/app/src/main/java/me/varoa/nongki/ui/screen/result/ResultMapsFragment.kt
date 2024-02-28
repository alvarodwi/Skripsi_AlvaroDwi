package me.varoa.nongki.ui.screen.result

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentResultMapsBinding
import me.varoa.nongki.ext.generateMapsIntent
import me.varoa.nongki.ui.base.LocationFragment
import me.varoa.nongki.utils.viewbinding.viewBinding
import org.koin.androidx.navigation.koinNavGraphViewModel

class ResultMapsFragment : LocationFragment(R.layout.fragment_result_maps) {
    private val binding by viewBinding<FragmentResultMapsBinding>()
    private val viewModel by koinNavGraphViewModel<ResultViewModel>(R.id.nav_result)

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionLauncher = requestLocationPermissionLauncher { getCurrentLocation() }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        with(binding) {
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            val mapView = childFragmentManager.findFragmentById(map.id) as SupportMapFragment
            lifecycle.coroutineScope.launch {
                mMap = mapView.awaitMap()
                setupMap()
            }
        }

        observeResult()
    }

    private fun setupMap() {
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isRotateGesturesEnabled = true
        }

        // getCurrentLocation()
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun observeResult() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.result.collectLatest { item ->
                item.results.forEach { result ->
                    val location = LatLng(result.lat, result.lng)
                    mMap
                        .addMarker {
                            position(location)
                            title(result.name)
                        }.also { it?.tag = result.name }
                    mMap.setOnInfoWindowClickListener { marker ->
                        val intent = generateMapsIntent(marker.tag as String, marker.position.latitude, marker.position.longitude)
                        intent.resolveActivity(requireActivity().packageManager)?.let {
                            startActivity(intent)
                        }
                    }
                }
                mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(item.results[0].lat, item.results[0].lng),
                        15f,
                    ),
                )
            }
        }

    // cek lokasi
    private fun getCurrentLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentLocation,
                            16f,
                        ),
                    )
                    mMap.addMarker {
                        position(currentLocation)
                        title("Lokasi kamu")
                    }
                }
            }
        } else {
            requestLocationPermission(locationPermissionLauncher)
        }
    }
}
