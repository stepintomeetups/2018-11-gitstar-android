package hu.stepintomeetups.gitstar.ui.common

sealed class DataRequestState<T> {
    class Loading<T> : DataRequestState<T>()
    class Success<T>(var data: T) : DataRequestState<T>()
    class Error<T>(val error: Throwable) : DataRequestState<T>()
}