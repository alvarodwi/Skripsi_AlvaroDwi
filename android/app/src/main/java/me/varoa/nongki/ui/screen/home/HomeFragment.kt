package me.varoa.nongki.ui.screen.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import logcat.logcat
import me.varoa.nongki.R
import me.varoa.nongki.data.work.SyncWorker
import me.varoa.nongki.databinding.FragmentHomeBinding
import me.varoa.nongki.ext.navigateTo
import me.varoa.nongki.ext.toast
import me.varoa.nongki.utils.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalTime

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding<FragmentHomeBinding>()
    private val viewModel by viewModel<HomeViewModel>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
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

            with(layoutHome) {
                val currentHour = LocalTime.now().hour
                val timeBlock =
                    when (currentHour) {
                        in 4..9 -> {
                            "pagi"
                        }

                        in 10..13 -> {
                            "siang"
                        }

                        in 14..17 -> {
                            "sore"
                        }

                        else -> {
                            "malam"
                        }
                    }
                lblHomeTitle.text = "Selamat $timeBlock!"

                btnSearch.setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val flag = viewModel.isFirstTimeSearch.first()
                        logcat { "isFirstTimeSearch -> $flag" }
                        if (flag) {
                            navigateTo(HomeFragmentDirections.actionHomeToLocationCheck())
                        } else {
                            navigateTo(HomeFragmentDirections.actionHomeToSearch())
                        }
                    }
                }
                itemDatabase.setOnClickListener {
                    navigateTo(HomeFragmentDirections.actionHomeToDataset())
                }
                itemHistory.setOnClickListener {
                    navigateTo(HomeFragmentDirections.actionHomeToHistory())
                }
            }
        }
        // observer
        observeFirstTimeSync()
    }

    private fun observeFirstTimeSync() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isFirstTimeSync.collectLatest { flag ->
                if (flag) {
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
                                    viewLifecycleOwner.lifecycleScope.launch {
                                        viewModel.finishSync(datasetSize)
                                    }
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
        }
}
