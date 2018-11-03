package hu.stepintomeetups.gitstar.ui.main.search

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import hu.stepintomeetups.gitstar.api.GitHubService
import hu.stepintomeetups.gitstar.api.responses.SearchRepositoriesResult
import hu.stepintomeetups.gitstar.ui.common.CoroutineViewModel
import hu.stepintomeetups.gitstar.ui.common.DataRequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RepositorySearchViewModel : CoroutineViewModel() {
    var queryString: String? = null

    val data = MutableLiveData<DataRequestState<SearchRepositoriesResult>>()

    private var currentJob: Job? = null

    @MainThread
    fun performSearch(q: String) {
        queryString = q

        currentJob?.cancel()

        currentJob = launch(Dispatchers.IO) {
            delay(500)

            data.postValue(DataRequestState.Loading())

            // the API returns an error when q is missing or empty – we have to build a fake response instead
            if (q.isEmpty()) {
                data.postValue(DataRequestState.Success(SearchRepositoriesResult(0, false, ArrayList())))
                return@launch
            }

            try {
                val repos = GitHubService.instance.searchRepositories(q).await()

                data.postValue(DataRequestState.Success(repos))
            } catch (e: Throwable) {
                when (e) {
                    is IOException -> data.postValue(DataRequestState.Error(e))
                    is HttpException -> data.postValue(DataRequestState.Error(e))
                    else -> throw e
                }
            }
        }
    }
}