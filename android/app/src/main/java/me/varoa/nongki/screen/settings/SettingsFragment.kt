package me.varoa.nongki.screen.settings

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentSettingsBinding
import me.varoa.nongki.ext.navigateTo
import me.varoa.nongki.ext.toast
import me.varoa.nongki.utils.viewbinding.viewBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val binding by viewBinding<FragmentSettingsBinding>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            with(toolbar) {
                title = getString(R.string.title_settings)
                setNavigationOnClickListener { findNavController().popBackStack() }
            }

            childFragmentManager.commit {
                replace(
                    settingsContainer.id,
                    SettingsContainer {
                        navigateTo(SettingsFragmentDirections.actionSettingsToLogin())
                    },
                )
            }
        }
    }

    class SettingsContainer(
        private val logoutCallback: () -> Unit,
    ) : PreferenceFragmentCompat() {
        override fun onCreatePreferences(
            savedInstanceState: Bundle?,
            rootKey: String?,
        ) {
            val screen = preferenceManager.createPreferenceScreen(requireContext())
            preferenceScreen = screen
            setupPreferenceScreen(screen)
        }

        private fun setupPreferenceScreen(screen: PreferenceScreen) =
            with(screen) {
                preference {
                    icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_sync)
                    title = "Mulai sinkronisasi database"
                    summary = "Terakhir dijalankan : dd/MM/yyyy HH:mm"
                    onClick {
                        toast("Memulai sinkronisasi")
                    }
                }

                preference {
                    icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_logout)
                    title = "Logout"
                    summary = "Hapus sesi dan keluar dari aplikasi"
                    onClick {
                        toast("Logout berhasil!")
                        Firebase.auth.signOut()
                        logoutCallback.invoke()
                    }
                }
            }
    }
}
