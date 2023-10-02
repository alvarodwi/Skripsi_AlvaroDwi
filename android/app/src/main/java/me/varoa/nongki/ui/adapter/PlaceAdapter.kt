package me.varoa.nongki.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import me.varoa.nongki.databinding.ItemPlaceBinding
import me.varoa.nongki.domain.model.HangoutPlace
import me.varoa.nongki.utils.viewbinding.viewBinding

class PlaceAdapter(
    private val onClick: (HangoutPlace) -> Unit,
) : ListAdapter<HangoutPlace, PlaceItemViewHolder>(PLACE_DIFF) {
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
    ) = holder.bind(getItem(position), onClick)
}
