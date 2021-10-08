package com.example.tdcfirebaseapp.pages.login.repositories

import com.example.tdcfirebaseapp.pages.login.contracts.AuthContract
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class AuthRepository {
    object Instance {
        private val mAuth = FirebaseAuth.getInstance()

        fun loginWithEmailAndPassword(
            email: String,
            password: String,
            listener: AuthContract.LoginResultListener)
        {
            if (email.isNotBlank() && password.isNotBlank()) {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        listener.onSuccess()
                    } else {
                        listener.onFailure(task.exception!!)
                    }
                }
            } else {
                listener.onFailure(Exception("Email or password cannot be blank"))
            }
        }
    }
}