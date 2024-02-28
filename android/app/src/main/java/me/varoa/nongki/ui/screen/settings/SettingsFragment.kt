package me.varoa.nongki.ui.screen.settings

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.varoa.nongki.R
import me.varoa.nongki.data.prefs.DataStoreManager
import me.varoa.nongki.data.work.SyncWorker
import me.varoa.nongki.databinding.FragmentSettingsBinding
import me.varoa.nongki.ext.navigateTo
import me.varoa.nongki.ext.toast
import me.varoa.nongki.utils.viewbinding.viewBinding
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val binding by viewBinding<FragmentSettingsBinding>()
    private val prefs by inject<DataStoreManager>()

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
                    SettingsContainer(
                        syncSuccessCallback = {
                            viewLifecycleOwner.lifecycleScope.launch {
                                prefs.finishSync(it)
                            }
                        },
                        logoutCallback = {
                            navigateTo(SettingsFragmentDirections.actionSettingsToLogin())
                        },
                    ),
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            prefs.lastSync.collectLatest {
                SettingsContainer.updateLastSync(it)
            }
        }
    }

    class SettingsContainer(
        private val syncSuccessCallback: (size: Int) -> Unit,
        private val logoutCallback: () -> Unit,
    ) : PreferenceFragmentCompat() {
        companion object {
            var lastSync = ""

            fun updateLastSync(str: String) {
                lastSync = str
            }
        }

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
                    summary =
                        """Terakhir dijalankan :
                        |$lastSync
                        """.trimMargin()
                    onClick {
                        toast("Memulai sinkronisasi")
                        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
                        val workManager = WorkManager.getInstance(requireContext())
                        workManager.enqueue(syncRequest)

                        workManager
                            .getWorkInfoByIdLiveData(syncRequest.id)
                            .observe(viewLifecycleOwner) { workInfo ->
                                when (workInfo?.state) {
                                    WorkInfo.State.SUCCEEDED -> {
                                        val message = workInfo.outputData.getString(SyncWorker.KEY_MESSAGE)
                                        val datasetSize = workInfo.outputData.getInt(SyncWorker.KEY_DATASET_SIZE, 0)
                                        message?.let { toast(it) }
                                        syncSuccessCallback.invoke(datasetSize)
                                    }

                                    WorkInfo.State.FAILED -> {
                                        val message = workInfo.outputData.getString(SyncWorker.KEY_MESSAGE)
                                        toast(getString(R.string.toast_sync_failed, message))
                                    }

                                    else -> {
                                        // do nothing
                                    }
                                }
                            }
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
