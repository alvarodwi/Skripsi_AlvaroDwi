package me.varoa.nongki.screen.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentSearchBinding
import me.varoa.nongki.ext.navigateTo
import me.varoa.nongki.utils.viewbinding.viewBinding

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding by viewBinding<FragmentSearchBinding>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            btnSearch.setOnClickListener {
                navigateTo(SearchFragmentDirections.actionSearchToResult("awoo"))
            }
        }
    }
}
