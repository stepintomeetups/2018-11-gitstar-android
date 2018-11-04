/*
 * Created by Tamás Szincsák on 2018-11-04.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.helpers

import com.facebook.stetho.okhttp3.StethoInterceptor
import hu.stepintomeetups.gitstar.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object CommonHttpClient {
    val instance: OkHttpClient by lazy {
        newBuilder().build()
    }

    fun newBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            builder.addNetworkInterceptor(StethoInterceptor())
        }

        return builder
    }
}