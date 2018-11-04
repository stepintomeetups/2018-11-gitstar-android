/*
 * Created by Tamás Szincsák on 2018-11-02.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import hu.stepintomeetups.gitstar.BuildConfig
import hu.stepintomeetups.gitstar.api.entities.Commit
import hu.stepintomeetups.gitstar.api.entities.GitFile
import hu.stepintomeetups.gitstar.api.entities.Repo
import hu.stepintomeetups.gitstar.api.entities.User
import hu.stepintomeetups.gitstar.api.responses.SearchRepositoriesResult
import io.gsonfire.GsonFireBuilder
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface GitHubService {
    @GET("user")
    fun getCurrentUser(): Deferred<User>

    @GET("user/repos")
    fun listOwnRepos(): Deferred<List<Repo>>

    @GET("user/starred/{owner}/{repo}")
    fun isRepoStarred(@Path("owner") owner: String, @Path("repo") repo: String): Deferred<Response<Unit>>

    @PUT("user/starred/{owner}/{repo}")
    fun starRepo(@Path("owner") owner: String, @Path("repo") repo: String): Deferred<Response<Unit>>

    @DELETE("user/starred/{owner}/{repo}")
    fun unstarRepo(@Path("owner") owner: String, @Path("repo") repo: String): Deferred<Response<Unit>>

    @GET("search/repositories")
    fun searchRepositories(@Query("q") q: String): Deferred<SearchRepositoriesResult>

    @GET("repos/{owner}/{repo}")
    fun getRepositoryDetails(@Path("owner") owner: String, @Path("repo") repo: String): Deferred<Repo>

    @GET("repos/{owner}/{repo}/readme")
    fun getReadmeForRepository(@Path("owner") owner: String, @Path("repo") repo: String): Deferred<GitFile>

    @GET("repos/{owner}/{repo}/commits")
    fun getCommitsForRepository(@Path("owner") owner: String, @Path("repo") repo: String): Deferred<List<Commit>>

    companion object {
        private val gson: Gson by lazy {
            GsonFireBuilder()
                .enableHooks(GitFile::class.java)
                .createGsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create()
        }

        private val httpClient: OkHttpClient by lazy {
            val builder = OkHttpClient.Builder()

            if (BuildConfig.DEBUG)
                builder.addNetworkInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })

            builder.addNetworkInterceptor { chain ->
                if (chain.request().url().host() != "api.github.com")
                    throw AssertionError("GitHubService.httpClient: Invalid host detected")

                chain.proceed(chain.request().newBuilder()
                    .addHeader("Accept", "application/vnd.github.mercy-preview+json")
                    .addHeader("Authorization", "token " + BuildConfig.GITHUB_OAUTH_TOKEN)
                    .addHeader("User-Agent", "GitStar-Android-Demo")
                    .build())
            }

            builder.build()
        }

        val instance: GitHubService by lazy {
            val retrofit = Retrofit.Builder()
                .client(httpClient)
                .baseUrl("https://api.github.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            retrofit.create(GitHubService::class.java)
        }
    }
}