package com.example.pozi_android.data.remote.network

data class DataResult<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T): DataResult<T> =
            DataResult(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): DataResult<T> =
            DataResult(status = Status.ERROR, data = data, message = message)
    }

}