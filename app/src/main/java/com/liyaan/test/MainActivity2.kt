package com.liyaan.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.liyaan.test.setting.StatusBarUtil

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        logD(getValue("name")!!)
//        StatusBarUtil.setStatusBarMode(this, true, R.color.white);
    }
}