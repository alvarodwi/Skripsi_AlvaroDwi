package me.varoa.nongki.ui.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import me.varoa.nongki.databinding.ItemPlaceBinding
import me.varoa.nongki.domain.model.HangoutPlace
import me.varoa.nongki.ext.calculateDistance

class PlaceItemViewHolder(
    private val binding: ItemPlaceBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        data: HangoutPlace?,
        onClick: (HangoutPlace) -> Unit,
        userLat: Double? = null,
        userLng: Double? = null,
    ) {
        if (data == null) return
        with(binding) {
            root.setOnClickListener { onClick(data) }
            if (userLat != null && userLng != null) {
                lblDistance.isVisible = true
                val distance = calculateDistance(userLat, userLng, data.lat, data.lng)
                lblDistance.text = "~ ${"%.2f".format(distance)} km dari lokasi kamu"
            }
            lblName.text = data.name
            lblAddress.text = data.address
            lblRating.text = "⭐ ${data.reputation}"
            chipPrice.text = "Harga - ${data.price}"
            chipFacility.text = "Fasilitas - ${data.facility}"
            chipLocation.text = "Lokasi - ${data.location}"
        }
    }
}
