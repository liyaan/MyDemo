package com.liyaan.test.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import com.liyaan.test.toast

class BootCompleteReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("boot",intent?.action?:"aaaaaaa")
        Handler().postAtTime({
            Log.i("boot","boot - start")
        },1000)
    }
}