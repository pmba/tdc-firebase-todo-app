package com.example.tdcfirebaseapp.pages.profile.repositories

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.tdcfirebaseapp.shared.contracts.ViewModelContracts
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class ProfileRepository {

    companion object {
        const val TAG = "ProfileRepository"
    }

    object Instance {
        private val mFbAuth = FirebaseAuth.getInstance()
        private val mStorageRef = FirebaseStorage.getInstance()
        private val mUserImageStorageRef = mStorageRef
            .getReference("users")
            .child(getUser().uid)
            .child("profile.jpg")

        fun logout() {
            mFbAuth.signOut()
        }

        @Throws(NullPointerException::class)
        fun getUser(): FirebaseUser {
            return mFbAuth.currentUser!!
        }

        fun updateUserName(name: String, listener: ViewModelContracts.ResultListener) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            getUser().updateProfile(profileUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    listener.onSuccess()
                } else {
                    listener.onFailure(task.exception!!)
                }
            }
        }

        private fun updateProfileImageUri(uri: Uri, onCompleteListener: OnCompleteListener<Void>) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build()

            getUser().updateProfile(profileUpdates).addOnCompleteListener(onCompleteListener)
        }

        fun uploadProfileImage(bitmap: Bitmap, listener: ViewModelContracts.ResultListener) {
            val byteArrOutStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrOutStream)
            val data = byteArrOutStream.toByteArray()

            mUserImageStorageRef.putBytes(data).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Foto atualizada com sucesso
                    Log.i(TAG, "Imagem atualizada com sucesso")

                    mUserImageStorageRef.downloadUrl.addOnCompleteListener { urlTask ->
                        if (urlTask.isSuccessful) {
                            val imageUri = urlTask.result!!

                            // Adiciona a url da imagem ao perfil do usuário
                            updateProfileImageUri(imageUri) { updateImageTask ->
                                if (updateImageTask.isSuccessful)  {
                                    Log.i(TAG, "Url da imagem atualizada no perfil do usuário")
                                    listener.onSuccess()
                                } else {
                                    Log.e(TAG, "Erro ao tentar atualizar uri da imagem : " +
                                            "${updateImageTask.exception!!}"
                                    )

                                    listener.onFailure(updateImageTask.exception!!)
                                }
                            }
                        } else {
                            Log.e(TAG, "Erro ao pegar url da imagem : ${urlTask.exception}")
                            listener.onFailure(urlTask.exception!!)
                        }
                    }
                } else {
                    Log.e(TAG, "Erro ao realizar upload da imagem : ${task.exception}")
                    listener.onFailure(task.exception!!)
                }
            }
        }
    }
}
