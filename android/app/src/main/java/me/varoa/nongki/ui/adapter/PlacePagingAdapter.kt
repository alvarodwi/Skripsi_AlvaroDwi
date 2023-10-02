package me.varoa.nongki.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import me.varoa.nongki.databinding.ItemPlaceBinding
import me.varoa.nongki.domain.model.HangoutPlace
import me.varoa.nongki.ui.adapter.PlaceAdapter.Companion.PLACE_DIFF
import me.varoa.nongki.utils.viewbinding.viewBinding

class PlacePagingAdapter(
    private val onClick: (HangoutPlace) -> Unit,
) : PagingDataAdapter<HangoutPlace, PlaceItemViewHolder>(PLACE_DIFF) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlaceItemViewHolder = PlaceItemViewHolder(parent.viewBinding(ItemPlaceBinding::inflate))

    override fun onBindViewHolder(
        holder: PlaceItemViewHolder,
        position: Int,
    ) = holder.bind(getItem(position), onClick)
}
