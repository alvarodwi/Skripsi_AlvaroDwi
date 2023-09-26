package me.varoa.nongki.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.varoa.nongki.databinding.ItemPlaceBinding
import me.varoa.nongki.domain.model.HangoutPlace
import me.varoa.nongki.ui.adapter.PlaceAdapter.PlaceItemViewHolder
import me.varoa.nongki.utils.viewbinding.viewBinding

class PlaceAdapter(
    private val onClick: (HangoutPlace) -> Unit,
) : PagingDataAdapter<HangoutPlace, PlaceItemViewHolder>(PLACE_DIFF) {
    inner class PlaceItemViewHolder(private val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: HangoutPlace?) {
            if (data == null) return
            with(binding) {
                lblName.text = data.name
                lblAddress.text = data.address
                lblRating.text = "⭐ ${data.reputation}"
                chipPrice.text = "Harga - ${data.price}"
                chipFacility.text = "Fasilitas - ${data.facility}"
                chipLocation.text = "Lokasi - ${data.location}"
            }
        }
    }

    companion object {
        val PLACE_DIFF =
            object : DiffUtil.ItemCallback<HangoutPlace>() {
                override fun areItemsTheSame(
                    oldItem: HangoutPlace,
                    newItem: HangoutPlace,
                ): Boolean = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: HangoutPlace,
                    newItem: HangoutPlace,
                ): Boolean = oldItem == newItem
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlaceItemViewHolder = PlaceItemViewHolder(parent.viewBinding(ItemPlaceBinding::inflate))

    override fun onBindViewHolder(
        holder: PlaceItemViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))
}
