package me.varoa.nongki.screen.result

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentResultBinding
import me.varoa.nongki.utils.viewbinding.viewBinding

class ResultFragment : Fragment(R.layout.fragment_result) {
    private val binding by viewBinding<FragmentResultBinding>()

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
