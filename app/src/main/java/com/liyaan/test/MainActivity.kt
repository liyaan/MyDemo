package com.liyaan.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    private val mViewModel:BannerViewModel by viewModels()

    private val imageTitle:ImageView by lazy {
        findViewById(R.id.img_main)
    }
    private val editTitle:EditText by lazy {
        findViewById(R.id.edit_main)
    }

    private val testBtn:Button by lazy {
        findViewById(R.id.test)
    }
    private val testBtnOne:Button by lazy {
        findViewById(R.id.testOne)
    }
    private val arcProgressBar:ArcProgressBarJava by lazy{
        findViewById(R.id.arcProgressBar)
    }
    private val imgUrl = "https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8150c45f9b6c4e7aac783ed83e5c3db3~tplv-k3u1fbpfcp-zoom-crop-mark:3024:3024:3024:1702.awebp"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewModel.getBanner()
        mViewModel.mBannerLiveData.observe(this) {
            it.forEach { banner ->
                Log.i("banner", banner.toString())
            }
        }
        imageTitle.load(imgUrl)
        Log.i("string test", "12345".lengthLoad().toString())
        Log.i("string test", "12345678".lengthLoad().toString())
        editTitle.listener(change = {
            Log.i("edit text be",it)
        })
        testBtn.clickView {
            toast("测试")
        }
        testBtnOne.clickView {
            toast("测试测试")
        }
        //
        arcProgressBar.setProgress(150f)
//        arcProgressBar.setFirstText("123")
//        arcProgressBar.setSecondText("123")
    }

    fun onClickView(view: View) {
        Toast.makeText(this,"hello word",Toast.LENGTH_SHORT).show()
    }
}