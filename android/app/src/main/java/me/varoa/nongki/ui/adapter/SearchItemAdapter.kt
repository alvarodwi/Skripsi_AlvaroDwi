package me.varoa.nongki.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.varoa.nongki.databinding.ItemHistoryBinding
import me.varoa.nongki.domain.model.SearchItem
import me.varoa.nongki.ui.adapter.SearchItemAdapter.SearchItemItemViewHolder
import me.varoa.nongki.utils.viewbinding.viewBinding
import org.hashids.Hashids

class SearchItemAdapter(
    private val hashids: Hashids,
    private val onClick: (SearchItem) -> Unit,
) : PagingDataAdapter<SearchItem, SearchItemItemViewHolder>(SEARCH_DIFF) {
    inner class SearchItemItemViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SearchItem?) {
            if (data == null) return
            with(binding) {
                root.setOnClickListener { onClick(data) }
                lblId.text = "#ID ${hashids.encode(data.id.toLong())}"
                lblTimestamp.text = data.timestamp
            }
        }
    }

    companion object {
        val SEARCH_DIFF =
            object : DiffUtil.ItemCallback<SearchItem>() {
                override fun areItemsTheSame(
                    oldItem: SearchItem,
                    newItem: SearchItem,
                ): Boolean = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: SearchItem,
                    newItem: SearchItem,
                ): Boolean = oldItem == newItem
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchItemItemViewHolder = SearchItemItemViewHolder(parent.viewBinding(ItemHistoryBinding::inflate))

    override fun onBindViewHolder(
        holder: SearchItemItemViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))
}
