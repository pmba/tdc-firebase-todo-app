package com.example.tdcfirebaseapp.pages.profile.repositories

import com.example.tdcfirebaseapp.shared.contracts.ViewModelContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class ProfileRepository {
    object Instance {
        private val mFbAuth = FirebaseAuth.getInstance()

        /**
         * Função para deslogar o usuário do firebase
         * @return loggedOutSuccessfully
         */
        fun logout() {
            mFbAuth.signOut()
        }

        @Throws(NullPointerException::class)
        fun getUser(): FirebaseUser {
            return mFbAuth.currentUser!!
        }

        fun updateUserName(name: String, listener: ViewModelContracts.ResultListener) {
            val user = mFbAuth.currentUser!!
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            user.updateProfile(profileUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    listener.onSuccess()
                } else {
                    listener.onFailure(task.exception!!)
                }
            }
        }
    }
}