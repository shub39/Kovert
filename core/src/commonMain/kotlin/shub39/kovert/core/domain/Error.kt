package shub39.kovert.core.domain

sealed interface Error

typealias RootError = Error

sealed interface Result<out D, out E: RootError> {
    data class Success<out D, out E: RootError>(val data: D): Result<D, E>
    data class Error<out D, out E: RootError>(
        val error: E,
        val debugMessage: String? = null
    ): Result<D, E>
}

inline fun <T, E : RootError, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

fun <T, E : RootError> Result<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map { }
}

inline fun <T, E : RootError> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E : RootError> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            action(error)
            this
        }

        is Result.Success -> this
    }
}

typealias EmptyResult<E> = Result<Unit, E>

sealed interface Errors: RootError {
    enum class AIErrors: Errors {
        RESPONSE_ERROR,
        PARSE_ERROR,
        UNKNOWN_ERROR
    }
}