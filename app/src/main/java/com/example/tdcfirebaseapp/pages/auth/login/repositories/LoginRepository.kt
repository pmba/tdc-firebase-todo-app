package com.example.tdcfirebaseapp.pages.auth.login.repositories

import com.example.tdcfirebaseapp.shared.contracts.AuthContract
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class LoginRepository {
    object Instance {
        private val mAuth = FirebaseAuth.getInstance()

        /**
         * Realiza login com email e senha através do FirebaseAuth
         *
         * @param email string do email da conta
         * @param password string da senha da conta
         * @param listener listener personalizado para passa o callback para o modelview
         */
        fun loginWithEmailAndPassword(
            email: String,
            password: String,
            listener: AuthContract.LoginResultListener
        ) {
            if (email.isNotBlank() && password.isNotBlank()) {
                // Realiza login com email e senha
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        listener.onSuccess()
                    } else {
                        listener.onFailure(task.exception!!)
                    }
                }
            } else {
                listener.onFailure(Exception("Email and password cannot be blank"))
            }
        }
    }
}