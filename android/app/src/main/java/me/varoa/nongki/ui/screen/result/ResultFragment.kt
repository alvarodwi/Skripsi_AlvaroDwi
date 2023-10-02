package me.varoa.nongki.ui.screen.result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentResultBinding
import me.varoa.nongki.domain.model.SearchItem
import me.varoa.nongki.ui.adapter.PlaceAdapter
import me.varoa.nongki.utils.viewbinding.viewBinding
import org.hashids.Hashids
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResultFragment : Fragment(R.layout.fragment_result) {
    private val binding by viewBinding<FragmentResultBinding>()
    private val viewModel by viewModel<ResultViewModel>()
    private val hashids by inject<Hashids>()

    private var adapter: PlaceAdapter? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.result.collectLatest(::loadResult)
        }
    }

    private fun loadResult(data: SearchItem) {
        with(binding) {
            lblSearchId.text = hashids.encode(data.id.toLong())
            lblResultSubtitle.text = "Nongki berhasil menemukan ${data.results.size} tempat\nyang sesuai dengan kriteria kamu"
            lblCriteria.text = "Kriteria : [ ${data.criteria.joinToString(",")} ]"

            adapter =
                PlaceAdapter {
                    // do nothing for now
                }

            rvResult.adapter = adapter
            rvResult.layoutManager = LinearLayoutManager(requireContext())
            adapter?.submitList(data.results)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }
}
