package me.varoa.nongki.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import me.varoa.nongki.databinding.ItemPlaceBinding
import me.varoa.nongki.domain.model.HangoutPlace

class PlaceItemViewHolder(
    private val binding: ItemPlaceBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        data: HangoutPlace?,
        onClick: (HangoutPlace) -> Unit,
    ) {
        if (data == null) return
        with(binding) {
            root.setOnClickListener { onClick(data) }
            lblName.text = data.name
            lblAddress.text = data.address
            lblRating.text = "⭐ ${data.reputation}"
            chipPrice.text = "Harga - ${data.price}"
            chipFacility.text = "Fasilitas - ${data.facility}"
            chipLocation.text = "Lokasi - ${data.location}"
        }
    }
}
