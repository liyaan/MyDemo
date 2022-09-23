package com.liyaan.test

import android.app.Application
import android.content.Context
import android.os.Handler

class App:Application() {

    companion object{
        var  instance:App? = null
        var handler:Handler = Handler()
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        HookHelper.attachContext()
    }
}