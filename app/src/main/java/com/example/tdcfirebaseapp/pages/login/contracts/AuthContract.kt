package com.example.tdcfirebaseapp.pages.login.contracts

import com.google.firebase.auth.FirebaseUser
import java.lang.Exception

interface AuthContract {
    interface LoginResultListener {
        fun onSuccess()
        fun onFailure(exception: Exception)
    }
}