package me.varoa.nongki.screen.dataset

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentDatasetBinding
import me.varoa.nongki.utils.viewbinding.viewBinding

class DatasetFragment : Fragment(R.layout.fragment_dataset) {
    private val binding by viewBinding<FragmentDatasetBinding>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }
}
