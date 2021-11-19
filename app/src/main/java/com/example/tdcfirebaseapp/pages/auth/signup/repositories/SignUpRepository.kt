package com.example.tdcfirebaseapp.pages.auth.signup.repositories

import com.example.tdcfirebaseapp.shared.contracts.ViewModelContracts
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class SignUpRepository {
    object Instance {

        private val mFbAuth = FirebaseAuth.getInstance()

        fun signUpWithEmailAndPassword(
            email: String,
            password: String,
            listener: ViewModelContracts.ResultListener
        ) {
            if (email.isNotBlank() && password.isNotBlank()) {
                // Cria conta com email e senha
                mFbAuth.createUserWithEmailAndPassword(email, password)
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