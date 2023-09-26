package me.varoa.nongki.ui.screen.dataset

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentDatasetBinding
import me.varoa.nongki.ext.toast
import me.varoa.nongki.ui.adapter.PlaceAdapter
import me.varoa.nongki.utils.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DatasetFragment : Fragment(R.layout.fragment_dataset) {
    private val binding by viewBinding<FragmentDatasetBinding>()
    private val viewModel by viewModel<DatasetViewModel>()

    private var adapter: PlaceAdapter? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

            adapter =
                PlaceAdapter { place ->
                    toast("${place.name} clicked!")
                }
            rvDataset.adapter = adapter
            rvDataset.layoutManager = LinearLayoutManager(requireContext())
        }

        observePlaces()
        observeDatasetSize()
    }

    private fun observePlaces() =
        viewLifecycleOwner.lifecycleScope.launch {
            adapter?.let {
                viewModel.places.collectLatest(it::submitData)
            }
        }

    private fun observeDatasetSize() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.datasetSize.collectLatest {
                binding.lblDatasetSubtitle.text = "Nongki menggunakan $it data lokasi\nuntuk membantu pencarian kamu"
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }
}
