package com.example.tdcfirebaseapp.pages.auth.forgotpassword.repositories

import com.example.tdcfirebaseapp.shared.contracts.AuthContract
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class ForgotPasswordRepository {
    object Instance {
        private val mAuth = FirebaseAuth.getInstance()

        /**
         * Envia o email de redefiniçao de senha para o usuário
         *
         * @param email string do email do usuário
         * @param listener listener personalizado para passa o callback para o modelview
         */
        fun sendPasswordResetEmail(email: String, listener: AuthContract.LoginResultListener) {
            if (email.isNotBlank()) {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
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