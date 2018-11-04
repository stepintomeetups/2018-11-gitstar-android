/*
 * Created by Tamás Szincsák on 2018-11-04.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.ui.main

import android.graphics.Bitmap
import androidx.annotation.MainThread
import androidx.core.graphics.createBitmap
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonSyntaxException
import hu.stepintomeetups.gitstar.App
import hu.stepintomeetups.gitstar.api.GitHubService
import hu.stepintomeetups.gitstar.api.entities.User
import hu.stepintomeetups.gitstar.helpers.GlideApp
import hu.stepintomeetups.gitstar.ui.common.CoroutineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import kotlin.math.roundToInt

class MainViewModel : CoroutineViewModel() {
    val userProfile = MutableLiveData<User?>()
    val avatar = MutableLiveData<Bitmap?>()

    private val densityMultiplier = App.instance.resources.displayMetrics.density

    private val defaultBitmap = createBitmap((26 * densityMultiplier).roundToInt(), (26 * densityMultiplier).roundToInt())

    init {
        userProfile.postValue(null)
        avatar.postValue(defaultBitmap)
    }

    @MainThread
    fun refreshUserProfile() {
        launch(Dispatchers.IO) {
            val user = try {
                GitHubService.instance.getCurrentUser().await().also {
                    userProfile.postValue(it)
                }
            } catch (e: Throwable) {
                when (e) {
                    is IOException, is HttpException, is JsonSyntaxException -> {
                        Timber.e(e, "Unable to fetch user profile");
                        null
                    }
                    else -> throw e
                }
            }

            val avatarUrl = user?.avatar_url

            if (avatarUrl != null) {
                val futureBitmap = GlideApp.with(App.instance)
                    .asBitmap()
                    .circleCrop()
                    .load(avatarUrl)
                    .submit((26 * densityMultiplier).roundToInt(), (26 * densityMultiplier).roundToInt())

                val avatarBitmap = futureBitmap.get()

                avatar.postValue(avatarBitmap)
            } else
                avatar.postValue(defaultBitmap)
        }
    }
}