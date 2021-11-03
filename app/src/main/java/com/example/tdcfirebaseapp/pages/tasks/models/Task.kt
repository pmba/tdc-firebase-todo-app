package com.example.tdcfirebaseapp.pages.tasks.models

import java.util.*

class Task(
    val uid: String,
    val title: String,
    var done: Boolean,
    var date: Date? = null,
) {

    class Builder {
        private var mUid: String? = null
        private var mTitle: String? = null
        private var mDone: Boolean = false
        private var mDate: Date? = null

        fun setUid(uid: String) = apply { this.mUid = uid }
        fun setTitle(title: String) = apply { this.mTitle = title }
        fun setDate(date: Date?) = apply { this.mDate = date }
        fun setDone(done: Boolean) = apply { this.mDone = done }

        @Throws(NullPointerException::class)
        fun build(): Task {
            mTitle!!.let { title ->
                mUid!!.let { uid ->
                    return Task(uid, title, mDone, mDate)
                }
            }
        }
    }

    override fun toString(): String {
        return "(title=$title, done=$done, date=$date)"
    }
}
