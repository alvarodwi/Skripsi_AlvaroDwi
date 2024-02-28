package me.varoa.nongki.ui.screen.result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentResultBinding
import me.varoa.nongki.domain.model.SearchItem
import me.varoa.nongki.ext.generateMapsIntent
import me.varoa.nongki.ext.navigateTo
import me.varoa.nongki.ui.adapter.PlaceAdapter
import me.varoa.nongki.utils.viewbinding.viewBinding
import org.hashids.Hashids
import org.koin.android.ext.android.inject
import org.koin.androidx.navigation.koinNavGraphViewModel

class ResultFragment : Fragment(R.layout.fragment_result) {
    private val binding by viewBinding<FragmentResultBinding>()
    private val viewModel by koinNavGraphViewModel<ResultViewModel>(R.id.nav_result)
    private val hashids by inject<Hashids>()

    private var adapter: PlaceAdapter? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_maps -> {
                        navigateTo(
                            ResultFragmentDirections.actionResultToMaps(),
                        )
                        true
                    }

                    else -> false
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.result.collectLatest(::loadResult)
        }
    }

    private fun loadResult(data: SearchItem) {
        logcat { data.toString() }
        with(binding) {
            lblSearchId.text = "#${hashids.encode(data.id.toLong())}"
            lblResultSubtitle.text = "Nongki berhasil menemukan ${data.results.size} tempat\nyang sesuai dengan kriteria kamu"
            lblCriteria.text = "Kriteria : [ ${data.criteria.joinToString(", ")} ]"

            adapter =
                PlaceAdapter(data.userLat, data.userLng) {
                    val intent = generateMapsIntent(it.name, it.lat, it.lng)
                    intent.resolveActivity(requireActivity().packageManager)?.let {
                        startActivity(intent)
                    }
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
