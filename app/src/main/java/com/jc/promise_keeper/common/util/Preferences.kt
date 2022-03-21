package com.jc.promise_keeper.common.util

import android.content.Context

object Preferences {

    fun setUserToken(context: Context, token: String) =
        context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(Keys.LOGIN_USER_TOKEN, token)
            .apply()


    fun getUserToken(context: Context): String =
        context.getSharedPreferences(Keys.PREF_NAME, Context.MODE_PRIVATE)
            .getString(Keys.LOGIN_USER_TOKEN, "")
            .toString()

}