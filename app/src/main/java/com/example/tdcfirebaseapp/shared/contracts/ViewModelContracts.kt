package com.example.tdcfirebaseapp.shared.contracts

import java.lang.Exception

interface ViewModelContracts {
    interface ResultListener {
        fun onSuccess()
        fun onFailure(exception: Exception)
    }
}