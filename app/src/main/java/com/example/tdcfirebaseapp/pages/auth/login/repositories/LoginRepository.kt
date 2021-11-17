package com.example.tdcfirebaseapp.pages.auth.login.repositories

import com.example.tdcfirebaseapp.shared.contracts.AuthContract
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class LoginRepository {
    object Instance {

        private val mFbAuth = FirebaseAuth.getInstance()

        fun loginWithEmailAndPassword(
            email: String,
            password: String,
            listener: AuthContract.LoginResultListener
        ) {
            if (email.isNotBlank() && password.isNotBlank()) {
                // Realiza login com email e senha
                mFbAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
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