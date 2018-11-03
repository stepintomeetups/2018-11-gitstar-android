package hu.stepintomeetups.gitstar.ui.main.my

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import hu.stepintomeetups.gitstar.api.GitHubService
import hu.stepintomeetups.gitstar.api.entities.Repo
import hu.stepintomeetups.gitstar.ui.common.CoroutineViewModel
import hu.stepintomeetups.gitstar.ui.common.DataRequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MyRepositoriesViewModel : CoroutineViewModel() {
    val data = MutableLiveData<DataRequestState<List<Repo>>>()

    @MainThread
    fun refreshData() {
        data.value = DataRequestState.Loading()

        launch(Dispatchers.IO) {
            try {
                val repos = GitHubService.instance.listOwnRepos().await()

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