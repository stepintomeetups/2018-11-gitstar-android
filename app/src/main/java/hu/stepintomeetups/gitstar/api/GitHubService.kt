package hu.stepintomeetups.gitstar.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import hu.stepintomeetups.gitstar.BuildConfig
import hu.stepintomeetups.gitstar.api.entities.Repo
import hu.stepintomeetups.gitstar.api.responses.SearchRepositoriesResult
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query


interface GitHubService {
    @GET("user/repos")
    fun listOwnRepos(): Deferred<List<Repo>>

    @GET("users/{user}/repos")
    fun listReposForUser(@Path("user") user: String): Deferred<List<Repo>>

    @GET("orgs/{organization}/repos")
    fun listReposForOrganization(@Path("organization") organization: String): Deferred<List<Repo>>

    @GET("search/repositories")
    fun searchRepositories(@Query("q") q: String): Deferred<SearchRepositoriesResult>

    companion object {
        val gson: Gson by lazy {
            GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create()
        }

        val httpClient: OkHttpClient by lazy {
            val builder = OkHttpClient.Builder()
            builder.addNetworkInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            builder.addNetworkInterceptor { chain ->
                // <basic auth will be handled here>
                chain.proceed(chain.request().newBuilder()
                    .addHeader("Accept", "application/vnd.github.v3+json")
                    .addHeader("Authorization", "token " + BuildConfig.GITHUB_OAUTH_TOKEN)
                    .build())
            }

            builder.build()
        }

        val instance by lazy {
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