package com.liyaan.test

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext

//内联函数  管理异常日志
suspend inline fun <T> apiCall(crossinline call: suspend CoroutineScope.() -> ResponseResult<T>): ResponseResult<T> {
    return withContext(Dispatchers.IO) {
        val res: ResponseResult<T>
        try {
            res = call()
        } catch (e: Throwable) {
            Log.e("ApiCaller", "request error", e)
            // 请求出错，将状态码和消息封装为 ResponseResult
            return@withContext ApiException.build(e).toResponse<T>()
        }
        if (res.errorCode == ApiException.CODE_AUTH_INVALID) {
            Log.e("ApiCaller", "request auth invalid")
            // 登录过期，取消协程，跳转登录界面
            // 省略部分代码
            cancel()
        }
        return@withContext res
    }
}
//字符串长度的判断
fun String?.lengthLoad(start:Int = 8,end:Int = 16):Boolean{
    this?.let {
        if (it.length in start..end){
            return true;
        }
    }?:let {
        return false
    }
    return false
}
//glide图片加载封装
fun ImageView.load(url:String,options: RequestOptions?=null){
    options?.let {
        Glide.with(this.context).load(url).apply(it).into(this)
    }?:let {
        Glide.with(this.context).load(url).apply( RequestOptions()
            .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher) ).into(this)
    }

}
//输入框监听
fun EditText.listener(before: (String) -> Unit = {}, change: (String) -> Unit = {}, after: (String) -> Unit = {}){
    this.addTextChangedListener(object:TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            before(s.toString())
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            change(s.toString())
        }
        override fun afterTextChanged(s: Editable?) {
            after(s.toString())
        }
    })
}
//点击事件的 全局控制
fun View.clickView(click:(View)->Unit){
    this.setOnClickListener {
        click(it)
    }
}
//长按点击事件
fun View.clickLongView(clickLong:(View)->Unit,result:Boolean = true){
    this.setOnLongClickListener {
        clickLong(it)
        result
    }
}

@RequiresApi(Build.VERSION_CODES.N)
fun Activity.startCls(cls:Class<*>, map:HashMap<String,String>? = null,finish:Boolean = true){
    val intent = Intent(this,cls)
    map?.let {
        map.forEach { (key, value) ->
            intent.putExtra(key,value)
        }
    }
    startActivity(intent)
    if (!finish){
        this.finish()
    }
}
@RequiresApi(Build.VERSION_CODES.N)
fun Activity.startActivityBundle(cls:Class<*>, bundle:Bundle ? = null,finish:Boolean = true){
    val intent = Intent(this,cls)
    bundle?.let {
       intent.putExtras(it)
    }
    startActivity(intent)
    if (!finish){
        this.finish()
    }
}
fun Activity.getValue(name:String):String?{
    return this.intent.getStringExtra(name)
}
fun Activity.getBundleValue(name:String):Bundle?{
    return this.intent.getBundleExtra(name)
}
const val isOpen:Boolean = true
fun Any.logI(info: String, tag: String? = this.javaClass.name){
    if (isOpen)
        Log.i(this.javaClass.name,info)
}
fun Any.logD(info: String, tag: String? =  this.javaClass.name){
    if (isOpen)
        Log.d(this.javaClass.name,info)
}
fun Any.logE(info: String, tag: String? =  this.javaClass.name){
    if (isOpen)
        Log.e(tag,info)
}
//public static final Bitmap.Config  ALPHA_8
//public static final Bitmap.Config  ARGB_4444
//public static final Bitmap.Config  ARGB_8888
//public static final Bitmap.Config  RGB_565
fun ImageView.img(id:Int){
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    if (options.inPreferredConfig != Bitmap.Config.ARGB_4444){
        options.inPreferredConfig = Bitmap.Config.ARGB_4444
    }

    BitmapFactory.decodeResource(this.resources,id,options)
//计算缩放比
    options.inSampleSize = calculateInSampleSize(options,200,300)
    options.inJustDecodeBounds = false

    this.setImageBitmap(BitmapFactory.decodeResource(this.resources,id,options))

}
private  fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqHeight: Int,
    reqWidth: Int
): Int {
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2
        //计算缩放比，是2的指数
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}
/**
 * 判断事件出发时间间隔是否超过预定值
 * 如果小于间隔（目前是1000毫秒）则返回true，否则返回false
 */
private var lastClickTime: Long = 0
fun isFastDoubleClick(): Boolean {
    val time = System.currentTimeMillis()
    val timeD = time - lastClickTime
    if (timeD in 1..999) {
        return true
    }
    lastClickTime = time
    return false
}
fun Context.toast(str:String){
    Toast.makeText(this,str,Toast.LENGTH_LONG).show()
}

fun Context.dp2px(dp:Float):Int{
    val density = resources.displayMetrics.density
    return (dp * density + 0.5f * if (dp >= 0) 1 else -1).toInt()
}
fun Context.sp2px(sp:Int):Int{
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, sp.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()
}

//RequestOptions options = new RequestOptions()
//.placeholder(R.mipmap.loading)                //加载成功之前占位图
//.error(R.mipmap.loading)                    //加载错误之后的错误图
//.override(400,400)                                //指定图片的尺寸
////指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//.fitCenter()
////指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
//.centerCrop()
//.circleCrop()//指定图片的缩放类型为centerCrop （圆形）
//.skipMemoryCache(true)                            //跳过内存缓存
//.diskCacheStrategy(DiskCacheStrategy.ALL)        //缓存所有版本的图像
//.diskCacheStrategy(DiskCacheStrategy.NONE)        //跳过磁盘缓存
//.diskCacheStrategy(DiskCacheStrategy.DATA)        //只缓存原来分辨率的图片
//.diskCacheStrategy(DiskCacheStrategy.RESOURCE)    //只缓存最终的图片
//.dontTransform()  //禁用图形变换功能,这个方法时全局的，导致其他地方的图片也不可进行图形变换了,慎用.
//.dontAnimate();//跳过动画
//
//Glide.with(this)
//.load(url)
//.asBitmap()//只加载静态图片，如果是git图片则只加载第一帧。
//.asGif()//加载动态图片，若现有图片为非gif图片，则直接加载错误占位图。
//.apply(bitmapTransform(newCropCircleTransformation())) //加载圆形图
//.apply(bitmapTransform(new BlurTransformation( 25, 4))) //模糊过滤，//第二个参数radius取值1-25，值越大图片越模糊
//.apply(options)
//.thumbnail(0.1f) //先加载缩略图
//.crossFade() //淡入淡出效果, 此属性在4.8.0上不适用
//.into(imageView);
//// 另外可以用into对图片对象进行特殊处理
//.into(new SimpleTarget<Drawable>(){
//    @Override
//    public void onResouceReady(Drawable resource, Transition<? super Drawable> transition) {
//        if(resource instanceof BitmapDrawable) {
//            Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
//            if (bitmap == null || bitmap.isRecycled()) return;
//            CachedImageManager.getInstance(mContext).cacheImage(iv_detail, bitmap);
//            iv_detail.setImageDrawable(resource);
//        }
//    }
//});