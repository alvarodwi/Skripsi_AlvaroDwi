package me.varoa.nongki.ui.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.varoa.nongki.R
import me.varoa.nongki.ext.toast

open class LocationFragment(
    @LayoutRes fragmentId: Int,
) : Fragment(fragmentId) {
    protected fun Fragment.checkPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            requireActivity(),
            permission,
        ) == PackageManager.PERMISSION_GRANTED

    // location permission shenanigans
    protected fun requestLocationPermission(launcher: ActivityResultLauncher<Array<String>>) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(R.string.dialog_location_permission_needed_title)
            setMessage(R.string.dialog_location_permission_needed_message)
            setPositiveButton(android.R.string.ok) { _, _ ->
                launcher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                    ),
                )
            }
            setNegativeButton(android.R.string.cancel) { _, _ ->
                toast("Mengembalikan ke halaman sebelumnya...")
                findNavController().popBackStack()
            }
        }.show()
    }

    private fun onPermissionDenied() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(R.string.dialog_location_permission_rejected_title)
            setMessage(R.string.dialog_location_permission_rejected_message)
            setPositiveButton(android.R.string.ok) { _, _ ->
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                    it.data = Uri.fromParts("package", requireActivity().packageName, null)
                    startActivity(it)
                }
            }
        }.show()
    }

    protected fun requestLocationPermissionLauncher(onPermissionAccepted: () -> Unit): ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    onPermissionAccepted.invoke()
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    onPermissionAccepted.invoke()
                }

                else -> {
                    onPermissionDenied()
                }
            }
        }
}
