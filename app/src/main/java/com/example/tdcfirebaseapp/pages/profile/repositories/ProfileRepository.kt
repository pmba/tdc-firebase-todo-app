package com.example.tdcfirebaseapp.pages.profile.repositories

import com.google.firebase.auth.FirebaseAuth

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
    }
}