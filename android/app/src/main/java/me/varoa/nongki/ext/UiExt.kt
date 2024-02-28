package me.varoa.nongki.ext

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import java.time.format.DateTimeFormatter
import java.util.Locale

private fun Fragment.createSnackbar(
    message: String,
    duration: Int,
): Snackbar = Snackbar.make(requireView(), message, duration)

fun Fragment.snackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
) {
    createSnackbar(message, duration).show()
}

fun Fragment.toast(
    message: String,
    duration: Int = Toast.LENGTH_SHORT,
) {
    Toast.makeText(requireContext(), message, duration).show()
}

fun Fragment.navigateTo(directions: NavDirections) = findNavController().navigate(directions)

val humanDateFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm").withLocale(Locale("id", "ID"))
