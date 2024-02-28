package me.varoa.nongki.ui.screen.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentHistoryBinding
import me.varoa.nongki.ext.navigateTo
import me.varoa.nongki.ui.adapter.SearchItemAdapter
import me.varoa.nongki.utils.viewbinding.viewBinding
import org.hashids.Hashids
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val binding by viewBinding<FragmentHistoryBinding>()
    private val viewModel by viewModel<HistoryViewModel>()
    private val hashids by inject<Hashids>()

    private var adapter: SearchItemAdapter? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

            adapter =
                SearchItemAdapter(hashids) { item ->
                    navigateTo(
                        HistoryFragmentDirections.actionHistoryToResult(item.id),
                    )
                }
            rvHistory.adapter = adapter
            rvHistory.layoutManager = LinearLayoutManager(requireContext())

            adapter?.let {
                itemTouchHelper.attachToRecyclerView(rvHistory)
            }
        }

        observeItems()
    }

    private fun observeItems() =
        viewLifecycleOwner.lifecycleScope.launch {
            adapter?.let {
                viewModel.items.collectLatest(it::submitData)
            }
        }

    private val itemTouchHelper =
        ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder,
                    target: ViewHolder,
                ): Boolean {
                    return false
                }

                override fun onSwiped(
                    viewHolder: ViewHolder,
                    direction: Int,
                ) {
                    val position = viewHolder.absoluteAdapterPosition
                    val item = adapter!!.snapshot().items[position]
                    showDeleteDialog(item.id)
                }

                override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
                    super.clearView(recyclerView, viewHolder)
                    viewHolder.itemView.apply {
                        ItemTouchHelper.Callback.getDefaultUIUtil().clearView(this)
                    }
                }
            },
        )

    private fun showDeleteDialog(id: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_delete_history_title)
            .setMessage(getString(R.string.dialog_delete_history_message, hashids.encode(id.toLong())))
            .setPositiveButton(R.string.dialog_yes) { _, _ ->
                viewModel.onDelete(id)
            }
            .setNegativeButton(R.string.dialog_no) { _, _ ->
                itemTouchHelper.attachToRecyclerView(null)
                itemTouchHelper.attachToRecyclerView(binding.rvHistory)
            }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }
}
