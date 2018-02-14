package com.appunite.githubkotlintestday.utils

import android.support.design.widget.Snackbar
import android.view.View
import java.net.UnknownHostException

object ErrorMessages {
    fun show(view: View): (Throwable) -> Unit {
        return { throwable ->
            val message = when (throwable) {
                is UnknownHostException -> "Internet connection problem"
                else -> throwable.message ?: "Unknown error"
            }
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }
    }
}
