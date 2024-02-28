package me.varoa.nongki.ui.screen.search.check

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentSearchLocationCheckBinding
import me.varoa.nongki.ext.navigateTo
import me.varoa.nongki.ext.toast
import me.varoa.nongki.ui.base.LocationFragment
import me.varoa.nongki.utils.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchLocationCheckFragment : LocationFragment(R.layout.fragment_search_location_check) {
    companion object {
        const val NEARBY_DISTANCE_IN_METERS = 5000 // 5 km defined as 'nearby'
    }

    private val binding by viewBinding<FragmentSearchLocationCheckBinding>()
    private val viewModel by viewModel<SearchLocationCheckViewModel>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionLauncher = requestLocationPermissionLauncher { getCurrentLocation() }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        with(binding.layoutLoading) {
            ivIllust.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.illust_tracking))
            lblLoading.text = "Memeriksa lokasi kamu"
            lblLoadingSubtitle.text =
                """
                Mohon tunggu sebentar...
                """.trimIndent()
            viewLifecycleOwner.lifecycleScope.launch {
                delay(3000)
                getCurrentLocation()
            }
        }
    }

    // cek lokasi
    private fun getCurrentLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val userLocation = location
                    val unpadLocation =
                        Location("Universitas Padjadjaran").apply {
                            latitude = 6.9261
                            longitude = 107.7747
                        }
                    with(binding.layoutLoading) {
                        if (!isLocationNearAnother(userLocation, unpadLocation)) {
                            lblLoading.text = "Lokasi kurang mendukung"
                            lblLoadingSubtitle.text =
                                """
                                Nongki untuk saat ini hanya mendukung data lokasi di sekitar Universitas Padjadjaran. Hasil rekomendasi bisa menjadi tidak optimal untuk lokasi Anda sekarang.
                                """.trimIndent()
                        } else {
                            lblLoading.text = "Lokasi mendukung"
                            lblLoadingSubtitle.text =
                                """
                                Lokasi Anda terdeteksi berada di sekitar Universitas Padjadjaran. Selamat melakukan pencarian
                                """.trimIndent()
                        }
                    }
                    viewModel.finishLocationChecking()
                    toast("Anda akan segera diarahkan ke halaman pencarian...")
                    lifecycleScope.launch {
                        delay(5000)
                        navigateTo(SearchLocationCheckFragmentDirections.actionLocationCheckToSearch())
                    }
                }
            }
        } else {
            requestLocationPermission(locationPermissionLauncher)
        }
    }

    private fun isLocationNearAnother(
        locationA: Location,
        locationB: Location,
    ): Boolean {
        val distance = locationA.distanceTo(locationB)
        return distance < NEARBY_DISTANCE_IN_METERS
    }
}
