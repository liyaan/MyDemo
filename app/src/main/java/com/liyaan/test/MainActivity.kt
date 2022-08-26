package com.liyaan.test

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.liyaan.test.setting.StatusBarUtil


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
        findViewById(R.id.arcProgressBar) //m aTv editUsername mOneselfEditPassWord  Base Api
    }
    private val imgUrl = "https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8150c45f9b6c4e7aac783ed83e5c3db3~tplv-k3u1fbpfcp-zoom-crop-mark:3024:3024:3024:1702.awebp"
//    private val mBootCompleteReceiver by lazy {
//        BootCompleteReceiver()
//    }
    private val mHandler = object: Handler() {
    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        Log.i("boot","网络状态改变了 ${msg.obj}")
    }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BaseUrlHelper.getInstance().setUrlField("https://www.baidu.com/")
        mViewModel.getBanner()
        mViewModel.mBannerLiveData.observe(this) {
            it.forEach { banner ->
                Log.i("banner", banner.toString())
            }
        }
//        imageTitle.load(imgUrl)
        imageTitle.img(R.mipmap.big)
        Log.i("string test", "12345".lengthLoad().toString())
        Log.i("string test", "12345678".lengthLoad().toString())
        editTitle.listener(change = {
            Log.i("edit text be",it)
        })
        testBtn.clickView {
            toast("测试")
            MvpSpUtils.saveCommit("base_url","https://www.wanandroid.com/")
            Api.testRetrofit()

            mViewModel.getBanner()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val map = HashMap<String,String>()
                map["name"] = "mainActivity"
                startCls(MainActivity2::class.java, map = map)
            }
        }
        testBtnOne.clickView {
            toast("测试测试")
            MvpSpUtils.saveCommit("base_url","http://baidu.com")
            Api.testRetrofit()

            mViewModel.getBanner()
        }
        //

        val data:Array<String> = arrayOf("1","3","4","5","1","3","4","5","1","3","4","0")
        val size: Int = data.size-1
        val progress:Float = arcProgressBar.maxProgress/size.toFloat()*5
        arcProgressBar.setProgress(progress)
        arcProgressBar.setTexts(data)
//        arcProgressBar.setFirstText("123")
//        arcProgressBar.setSecondText("123")
//        registerReceiver(mBootCompleteReceiver, IntentFilter("com.liyaan.test"))

        val componentName = ComponentName(
            "com.liyaan.test",
            "com.liyaan.test.receiver.BootCompleteReceiver"
        )
//        val intent = Intent("com.liyaan.test")
//        intent.component = componentName
//        sendBroadcast(intent)
//        sendHXBroadCast()
//根据状态栏颜色来决定状态栏文字用黑色还是白色
        StatusBarUtil.setStatusBarMode(this, true, R.color.white);

// 发送静态广播-MainActivity
        val intent = Intent()
        intent.action = "com.liyaan.test"
    //      intent.action = "android.net.conn.CONNECTIVITY_CHANGE"
        val className = "$packageName.receiver.BootCompleteReceiver"
        intent.setClassName(this@MainActivity, className)
        sendBroadcast(intent)

    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    connectivityManager.requestNetwork(NetworkRequest.Builder().build(),
        object : NetworkCallback() {
            override fun onAvailable(network: Network) {
//                super.onAvailable(network)
                val mainWifi = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                val currentWifi = mainWifi.connectionInfo
                val message = Message.obtain()
                if (currentWifi != null) {
                    val wifiSSID = currentWifi.ssid
                    message.obj =  wifiSSID
                }

                mHandler.sendMessage(message)
            }
        })
    }

    fun onClickView(view: View) {
        Toast.makeText(this,"hello word",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
//        unregisterReceiver(mBootCompleteReceiver)
    }

    private fun sendHXBroadCast() {
        val logIntent = Intent()
        logIntent.action = "com.liyaan.test"
        val pm: PackageManager = this.packageManager
        val matches: List<ResolveInfo> = pm.queryBroadcastReceivers(logIntent, 0)
        if (matches != null && matches.isNotEmpty()) {
            for (resolveInfo in matches) {
                val intent = Intent(logIntent)
                val cn = ComponentName(
                    resolveInfo.activityInfo.applicationInfo.packageName,
                    resolveInfo.activityInfo.name
                )
                intent.component = cn
                this.sendBroadcast(intent)
            }
        }
    }
}