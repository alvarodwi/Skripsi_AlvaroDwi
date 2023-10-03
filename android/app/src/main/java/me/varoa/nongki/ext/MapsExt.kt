package me.varoa.nongki.ext

import android.content.Intent
import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil

fun generateMapsIntent(
    name: String,
    lat: Double,
    lng: Double,
): Intent {
    val gmmIntentUri = Uri.parse("geo:$lat,$lng?q=${Uri.encode(name)}")
    return Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
        setPackage("com.google.android.apps.maps")
    }
}

fun calculateDistance(
    lat1: Double,
    lng1: Double,
    lat2: Double,
    lng2: Double,
): Double {
    val point1 = LatLng(lat1, lng1)
    val point2 = LatLng(lat2, lng2)
    return SphericalUtil.computeDistanceBetween(point1, point2) * .001
}
