package com.jc.promise_keeper.common.util

import android.content.Context

object Preferences {

    private const val PREF_NAME = "promise_keeper"
    private const val LOGIN_USER_TOKEN = "LOGIN_USER_TOKEN"

    fun setUserToken(context: Context, token: String) =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(LOGIN_USER_TOKEN, token)
            .apply()


    fun getUserToken(context: Context): String =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(LOGIN_USER_TOKEN, "")
            .toString()


}