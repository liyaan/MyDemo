package com.liyaan.test

import android.app.Application

class App:Application() {

    companion object{
        var  instance:App? = null
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }
}