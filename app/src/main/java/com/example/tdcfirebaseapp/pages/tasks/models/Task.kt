package com.example.tdcfirebaseapp.pages.tasks.models

import java.util.*

class Task(
    val title: String,
    val done: Boolean,
    var date: Date? = null,
) {
//    private constructor(builder: Builder) :

    class Builder {
        private var mTitle: String? = null
        private var mDate: Date? = null

        fun setTitle(title: String) = apply { this.mTitle = title }
        fun setDate(date: Date?) = apply { this.mDate = date }

        @Throws(NullPointerException::class)
        fun build(): Task {
            mTitle!!.let { title ->
                return Task(title, false, mDate)
            }
        }
    }

    override fun toString(): String {
        return "(title=$title, done=$done, date=$date)"
    }
}
