package com.capstonech2.fitfans.utils

sealed class State<out R> private constructor() {
    data class Success<out T>(val data: T) : State<T>()
    data class Error(val error: String) : State<Nothing>()
    data object Loading : State<Nothing>()
}