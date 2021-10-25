package com.example.tdcfirebaseapp.pages.auth.forgotpassword.repositories

import com.example.tdcfirebaseapp.shared.contracts.AuthContract
import java.lang.Exception

class ForgotPasswordRepository {
    object Instance {

        fun sendPasswordResetEmail(email: String, listener: AuthContract.LoginResultListener) {
            if (email.isNotBlank()) {
                listener.onSuccess()
            } else {
                listener.onFailure(Exception("Email cannot be blank"))
            }
        }

    }
}