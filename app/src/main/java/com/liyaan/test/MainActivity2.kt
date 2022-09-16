package com.liyaan.test

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() ,Handler.Callback{
    private val send_progress:ProgressBar by lazy {
        findViewById(R.id.send_progress)
    }
    private var handler: Handler = Handler(this)
    private var mProgrss = 0
    private var sendProgress = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        // 循环方法1
        handler.postDelayed(object:Runnable{
            override fun run() {
                handler.postDelayed(this, 50)
                handler.sendEmptyMessage(1)
            }

        },0)
    }

    override fun handleMessage(msg: Message): Boolean {
        when (msg.what) {
             1->{
                 if (mProgrss == 100) {
                     mProgrss = 0
                     sendProgress = 0
                 } else {
                     mProgrss++
                     sendProgress += 5
                 }

                 send_progress.progress = mProgrss
                 send_progress.secondaryProgress = sendProgress
             }

        }


        return false;
    }
}