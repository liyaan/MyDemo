package com.liyaan.test

import android.content.Context
import android.content.SharedPreferences
import java.security.AccessController.getContext


/**
 * spName :为文件名
 * key 值    通过key得到value
 *
 *
 * Commit： 在当前线程中  立即提交 写入到文件中
 *
 * Apply：也可以提交数据    不会占用主线程。会创建子线程在后台将内容写入到SD 卡中。
 */
object MvpSpUtils {
    //创建一个sp 的文件
    private const val DEFAULT_SP_NAME = "mvp_sp_config"

    //定义一个16进制的变量
    private const val TYPE_COMMIT = 0X100
    private const val TYPE_APPLY = 0X101
    private val appContext = App.instance!!.applicationContext
    //创建静态的 方法   返回值为String 的方法  包含一个参数  key     通过key得到value
    fun getString(key: String?): String {
        return getString(DEFAULT_SP_NAME, key)
    }

    //创建静态的方法   返回值为String 的方法  包含二个参数  key  spName     通过key得到value
    fun getString(spName: String?, key: String?): String {
        return appContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
            .getString(key, null)?:""
    }

    //创建静态的 方法   返回值为long 的方法  包含一个参数  key     通过key得到value
    fun getLong(key: String?): Long {
        return getLong(DEFAULT_SP_NAME, key)
    }

    //创建静态的方法   返回值为long 的方法  包含二个参数  key  spName     通过key得到value
    fun getLong(spName: String?, key: String?): Long {
        return appContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
            .getLong(key, 0)
    }

    //删除   key   清除文件
    fun remove(key: String?) {
        remove(DEFAULT_SP_NAME, key)
    }

    //删除   key  将spName 文件清除
    fun remove(spName: String?, key: String?) {
        val preferences: SharedPreferences =
            appContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.remove(key)
        editor.commit()
    }

    fun saveCommit(key: String, value: Any) {
        saveString(DEFAULT_SP_NAME, key, value, TYPE_COMMIT)
    }

    fun saveCommit(spName: String, key: String, value: Any) {
        saveString(spName, key, value, TYPE_COMMIT)
    }

    fun saveApply(spName: String, key: String, value: Any) {
        saveString(spName, key, value, TYPE_APPLY)
    }

    fun saveApply(key: String, value: Any) {
        saveString(DEFAULT_SP_NAME, key, value, TYPE_APPLY)
    }

    //保存文件     将key  value 保存在以spName 的文件中 ，
    private fun saveString(spName: String, key: String, value: Any, type: Int) {
        val preferences: SharedPreferences =
            appContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        //但是遇到不同的情况可以进行判断
        if (value is String) {
            editor.putString(key, value.toString())
        } else if (value is Float) {
            editor.putFloat(key, value)
        } else if (value is Int) {
            editor.putInt(key, value)
        } else if (value is Boolean) {
            editor.putBoolean(key, value)
        } else if (value is Long) {
            editor.putLong(key, value)
        }
        //如果是type_apply  就以apply 提交数据   否则相反
        if (type == TYPE_APPLY) {
            editor.apply()
        } else {
            editor.commit()
        }
    }
}