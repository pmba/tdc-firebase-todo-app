package com.example.tdcfirebaseapp.pages.auth.login.repositories

import com.example.tdcfirebaseapp.shared.contracts.AuthContract
import java.lang.Exception

class LoginRepository {
    object Instance {

        fun loginWithEmailAndPassword(
            email: String,
            password: String,
            listener: AuthContract.LoginResultListener
        ) {
            if (email.isNotBlank() && password.isNotBlank()) {
                // Realiza login com email e senha
                listener.onSuccess()
            } else {
                listener.onFailure(Exception("Email and password cannot be blank"))
            }
        }

    }
}