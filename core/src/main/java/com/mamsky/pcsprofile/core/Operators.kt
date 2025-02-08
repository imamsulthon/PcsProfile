package com.mamsky.pcsprofile.core

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun <T> Result<T>.isLoading(): Boolean {
    contract {
        returns() implies (this@isLoading is Result.Loading<T>)
    }
    return this is Result.Loading<T>
}

@OptIn(ExperimentalContracts::class)
fun <T> Result<T>.isSuccess(): Boolean {
    contract {
        returns() implies (this@isSuccess is Result.Success<T>)
    }
    return this is Result.Success<T>
}

@OptIn(ExperimentalContracts::class)
fun <T> Result<T>.isFailure(): Boolean {
    contract {
        returns() implies (this@isFailure is Result.Failure<T>)
    }
    return this is Result.Failure<T>
}
