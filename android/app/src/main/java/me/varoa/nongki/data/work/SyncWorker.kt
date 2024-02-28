package me.varoa.nongki.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import logcat.logcat
import me.varoa.nongki.BuildConfig
import me.varoa.nongki.domain.model.HangoutPlace
import me.varoa.nongki.domain.repository.DatasetRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class SyncWorker(
    appContext: Context,
    params: WorkerParameters,
    private val repository: DatasetRepository,
    private val client: OkHttpClient,
) : CoroutineWorker(appContext, params) {
    companion object {
        const val KEY_MESSAGE = "message"
        const val KEY_DATASET_SIZE = "dataset_size"
    }

    override suspend fun doWork(): Result {
        return try {
            val request =
                Request.Builder()
                    .url(BuildConfig.DATASET_URL)
                    .build()
            val response: Response?
            try {
                response = client.newCall(request).execute()
            } catch (e: IOException) {
                logcat { "Error while fetching dataset : " + e.message }
                throw e
            }
            // read csv
            val places = mutableListOf<HangoutPlace>()
            val csvString = requireNotNull(response.body?.string())
            val lines = csvString.split("\n")
            lines.subList(1, lines.size - 1).map { line ->
                val test = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*\$)".toRegex(), limit = 8)
                logcat { test.toString() }
                places.add(
                    HangoutPlace(
                        id = test[0].trim().toInt(),
                        name = test[1].trim(),
                        address = test[2].trim().removeSurrounding("\""),
                        lat = test[3].trim().toDouble(),
                        lng = test[4].trim().toDouble(),
                        price = test[5].trim().toInt(),
                        facility = test[6].trim().toInt(),
                        location = test[7].trim().toInt(),
                        reputation = test[8].trim().toInt(),
                    ),
                )
            }
            // logcat { "places -> $places" }
            repository.deleteAllHangoutPlaces()
            repository.insertHangoutPlace(*places.toTypedArray())
            Result.success(
                workDataOf(
                    KEY_MESSAGE to "Berhasil menambahkan ${places.size} lokasi ke dalam database",
                    KEY_DATASET_SIZE to places.size,
                ),
            )
        } catch (ex: IllegalStateException) {
            Result.failure(workDataOf(KEY_MESSAGE to ex.message))
        } catch (ex: IOException) {
            Result.failure(workDataOf(KEY_MESSAGE to ex.message))
        }
    }
}
