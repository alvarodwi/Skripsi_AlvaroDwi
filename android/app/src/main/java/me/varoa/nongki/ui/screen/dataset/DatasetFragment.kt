package me.varoa.nongki.ui.screen.dataset

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentDatasetBinding
import me.varoa.nongki.ext.generateMapsIntent
import me.varoa.nongki.ui.adapter.PlacePagingAdapter
import me.varoa.nongki.utils.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DatasetFragment : Fragment(R.layout.fragment_dataset) {
    private val binding by viewBinding<FragmentDatasetBinding>()
    private val viewModel by viewModel<DatasetViewModel>()

    private var adapter: PlacePagingAdapter? = null
    private lateinit var searchView: SearchView

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.apply {
                this.setNavigationOnClickListener { findNavController().popBackStack() }

                val searchItem = menu.findItem(R.id.action_search)
                searchView = searchItem.actionView as SearchView
                searchView.queryHint = "Cari tempat berdasarkan nama..."
                searchView.setOnQueryTextListener(
                    object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            toggleLoading(true)
                            val temp = viewModel.query.value
                            if (query == temp) {
                                viewModel.search("")
                                viewModel.search(temp)
                            } else {
                                viewModel.search(query ?: "")
                            }
                            searchView.clearFocus()
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            if (newText.isNullOrEmpty()) {
                                viewModel.search("")
                            }
                            return false
                        }
                    },
                )
            }

            adapter =
                PlacePagingAdapter {
                    val intent = generateMapsIntent(it.name, it.lat, it.lng)
                    intent.resolveActivity(requireActivity().packageManager)?.let {
                        startActivity(intent)
                    }
                }
            rvDataset.adapter = adapter
            rvDataset.layoutManager = LinearLayoutManager(requireContext())
        }

        observePlaces()
        observeDatasetSize()
        observeQuery()
    }

    private fun observePlaces() =
        viewLifecycleOwner.lifecycleScope.launch {
            adapter?.let {
                toggleLoading(false)
                viewModel.places.collectLatest(it::submitData)
            }
        }

    private fun observeDatasetSize() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.datasetSize.collectLatest {
                binding.lblDatasetSubtitle.text = "Nongki menggunakan $it data lokasi\nuntuk membantu pencarian kamu"
            }
        }

    private fun observeQuery() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.query.collectLatest { str ->
                binding.lblSearchHint.isVisible = str.isNotEmpty()
                binding.lblSearchHint.text = "Menampilkan data lokasi dengan nama '$str'"
            }
        }

    private fun toggleLoading(flag: Boolean) {
        binding.loadingBar.isVisible = flag
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }
}
