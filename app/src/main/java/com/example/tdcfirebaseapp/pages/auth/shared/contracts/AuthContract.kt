package com.example.tdcfirebaseapp.pages.auth.shared.contracts

import java.lang.Exception

interface AuthContract {
    interface LoginResultListener {
        fun onSuccess()
        fun onFailure(exception: Exception)
    }
}