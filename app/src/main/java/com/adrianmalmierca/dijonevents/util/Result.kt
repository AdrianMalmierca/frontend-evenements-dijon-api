package com.adrianmalmierca.dijonevents.util

sealed class Result<out T> { //sealed cause we only want success and error, so nobody can create a subclass outside this file
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()//nothing cause never will be an error of T type
}
