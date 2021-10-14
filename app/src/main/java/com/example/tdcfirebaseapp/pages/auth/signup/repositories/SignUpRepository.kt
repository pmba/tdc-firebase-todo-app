package com.example.tdcfirebaseapp.pages.auth.signup.repositories

import com.example.tdcfirebaseapp.pages.auth.shared.contracts.AuthContract
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class SignUpRepository {
    object Instance {
        private val mAuth = FirebaseAuth.getInstance()

        /**
         * Cria um usuário com email e senha através do FirebaseAuth
         *
         * @param email string do email da conta
         * @param password string da senha da conta
         * @param listener listener personalizado para passa o callback para o modelview
         */
        fun signUpWithEmailAndPassword(
            email: String,
            password: String,
            listener: AuthContract.LoginResultListener
        ) {
            if (email.isNotBlank() && password.isNotBlank()) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
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