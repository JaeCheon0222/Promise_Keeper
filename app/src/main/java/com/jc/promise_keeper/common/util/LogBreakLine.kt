package com.jc.promise_keeper.common.util

import android.util.Log

object LogLongContentPrint {

    fun logLineBreak(str: String) {

        if (str.length > 3000) {    // 텍스트가 3000자 이상이 넘어가면 줄
            Log.i("e", str.substring(0, 3000));
            logLineBreak(str.substring(3000));
        } else {
            Log.i("e", str);
        }

    }

}