package com.example.tdcfirebaseapp.pages.auth.signup.repositories

import com.example.tdcfirebaseapp.shared.contracts.AuthContract
import java.lang.Exception

class SignUpRepository {
    object Instance {

        fun signUpWithEmailAndPassword(
            email: String,
            password: String,
            listener: AuthContract.LoginResultListener
        ) {
            if (email.isNotBlank() && password.isNotBlank()) {
                listener.onSuccess()
            } else {
                listener.onFailure(Exception("Email and password cannot be blank"))
            }
        }

    }
}