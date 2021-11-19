package com.example.tdcfirebaseapp.pages.auth.forgotpassword.repositories

import com.example.tdcfirebaseapp.shared.contracts.ViewModelContracts
import java.lang.Exception

class ForgotPasswordRepository {
    object Instance {

        fun sendPasswordResetEmail(email: String, listener: ViewModelContracts.ResultListener) {
            if (email.isNotBlank()) {
                listener.onSuccess()
            } else {
                listener.onFailure(Exception("Email cannot be blank"))
            }
        }

    }
}