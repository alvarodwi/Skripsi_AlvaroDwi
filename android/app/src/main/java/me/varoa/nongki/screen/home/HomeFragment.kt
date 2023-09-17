package me.varoa.nongki.screen.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentHomeBinding
import me.varoa.nongki.ext.navigateTo
import me.varoa.nongki.utils.viewbinding.viewBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding<FragmentHomeBinding>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            // bottom bar
            with(bottomAppBar) {
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_settings -> {
                            navigateTo(HomeFragmentDirections.actionHomeToSettings())
                            true
                        }
                        else -> false
                    }
                }
            }
        }
    }
}
