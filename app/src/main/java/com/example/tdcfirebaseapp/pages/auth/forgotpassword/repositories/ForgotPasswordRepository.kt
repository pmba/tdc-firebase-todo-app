package com.example.tdcfirebaseapp.pages.auth.forgotpassword.repositories

import com.example.tdcfirebaseapp.shared.contracts.ViewModelContracts
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class ForgotPasswordRepository {
    object Instance {

        private val mFbAuth = FirebaseAuth.getInstance()

        fun sendPasswordResetEmail(email: String, listener: ViewModelContracts.ResultListener) {
            if (email.isNotBlank()) {
                // Envia email de esqueci senha para o email do usuário
                mFbAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            listener.onSuccess()
                        } else {
                            listener.onFailure(task.exception!!)
                        }
                    }
            } else {
                listener.onFailure(Exception("Email cannot be blank"))
            }
        }

    }
}