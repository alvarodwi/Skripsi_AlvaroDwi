package me.varoa.nongki.screen.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentHistoryBinding
import me.varoa.nongki.utils.viewbinding.viewBinding

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val binding by viewBinding<FragmentHistoryBinding>()

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
