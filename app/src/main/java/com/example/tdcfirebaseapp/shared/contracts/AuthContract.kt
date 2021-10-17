package com.example.tdcfirebaseapp.shared.contracts

import java.lang.Exception

interface AuthContract {
    interface LoginResultListener {
        fun onSuccess()
        fun onFailure(exception: Exception)
    }
}