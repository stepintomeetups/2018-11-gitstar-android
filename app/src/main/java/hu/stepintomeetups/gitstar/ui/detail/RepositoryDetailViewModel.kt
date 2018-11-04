/*
 * Created by Tamás Szincsák on 2018-11-04.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.ui.detail

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonSyntaxException
import hu.stepintomeetups.gitstar.api.GitHubService
import hu.stepintomeetups.gitstar.api.entities.Commit
import hu.stepintomeetups.gitstar.ui.common.CoroutineViewModel
import hu.stepintomeetups.gitstar.ui.common.DataRequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RepositoryDetailViewModel : CoroutineViewModel() {
    val data = MutableLiveData<DataRequestState<RepoDetails>>()

    private var alreadyInitialized = false
    private var ownerName: String? = null
    private var repositoryName: String? = null

    @MainThread
    fun initWithRepository(ownerName: String, repositoryName: String) {
        if (alreadyInitialized)
            return

        alreadyInitialized = true

        this.ownerName = ownerName
        this.repositoryName = repositoryName

        data.value = DataRequestState.Loading()

        launch(Dispatchers.IO) {
            try {
                val deferredRepo = GitHubService.instance.getRepositoryDetails(ownerName, repositoryName)
                val deferredReadme = GitHubService.instance.getReadmeForRepository(ownerName, repositoryName)
                val deferredCommits = GitHubService.instance.getCommitsForRepository(ownerName, repositoryName)
                val deferredIsStarred = GitHubService.instance.isRepoStarred(ownerName, repositoryName)

                val repo = deferredRepo.await()
                val readme = try { deferredReadme.await() } catch (e: HttpException) { if (e.code() == 404) null else throw e }
                val commits = try { deferredCommits.await() } catch (e: HttpException) { if (e.code() == 409) emptyList<Commit>() else throw e }

                val isStarredResponse = deferredIsStarred.await()
                val isStarred = when (isStarredResponse.code()) {
                    204 -> true
                    404 -> false
                    else -> throw HttpException(isStarredResponse)
                }

                val details = RepoDetails(repo, readme, commits.subList(0, 5.coerceAtMost(commits.size)), isStarred)

                data.postValue(DataRequestState.Success(details))
            } catch (e: Throwable) {
                when (e) {
                    is IOException -> data.postValue(DataRequestState.Error(e))
                    is HttpException -> data.postValue(DataRequestState.Error(e))
                    is JsonSyntaxException -> { e.printStackTrace(); data.postValue(DataRequestState.Error(e)) }
                    else -> throw e
                }
            }
        }
    }

    fun starRepository() = launch(Dispatchers.IO + NonCancellable) {
        val ownerName = ownerName ?: return@launch
        val repositoryName = repositoryName ?: return@launch

        try {
            GitHubService.instance.starRepo(ownerName, repositoryName).await()
        } catch (e: Throwable) {
            if (e is JsonSyntaxException)
                e.printStackTrace()

            when (e) {
                is IOException -> throw e
                else -> throw IOException(e)
            }
        }
    }

    fun unstarRepository() = launch(Dispatchers.IO + NonCancellable) {
        val ownerName = ownerName ?: return@launch
        val repositoryName = repositoryName ?: return@launch

        try {
            GitHubService.instance.unstarRepo(ownerName, repositoryName).await()
        } catch (e: Throwable) {
            if (e is JsonSyntaxException)
                e.printStackTrace()

            when (e) {
                is IOException -> throw e
                else -> throw IOException(e)
            }
        }
    }
}