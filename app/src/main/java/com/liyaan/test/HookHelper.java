package com.liyaan.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HookHelper {
    private static final String tag = Evil.class.getSimpleName();
    public static void attachContext(){
        try{
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod =
                    activityThreadClass.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);
            Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
            mInstrumentationField.setAccessible(true);
            Instrumentation mInstrumentation = (Instrumentation)
                    mInstrumentationField.get(currentActivityThread);
            Instrumentation evil = new Evil(mInstrumentation);
            mInstrumentationField.set(currentActivityThread,evil);
        }catch (Exception e){
            Log.e(tag,"hook attach 出错");
            throw new RuntimeException("hook attach 出错");
        }
    }
}

class Evil extends Instrumentation{
    private static final String tag = Evil.class.getSimpleName();
    Instrumentation mBase;

    public Evil(Instrumentation base){
        this.mBase = base;
    }

    public ActivityResult execStartActivity(Context who, IBinder contextThread,
                                            IBinder token, Activity target,
                                            Intent intent, int requestCode, Bundle options){
        Log.e(tag,"hook activity的启动流程");
        try{
            Method exec = Instrumentation
                    .class
                    .getDeclaredMethod("execStartActivity",
                            Context.class,IBinder.class,
                            IBinder.class,Activity.class,
                            Intent.class,int.class,Bundle.class);
            exec.setAccessible(true);
            return (ActivityResult) exec.invoke(mBase,who,contextThread,token,target,intent,requestCode,options);
        }catch (Exception e){
            Log.e(tag,"hook 出问题");
            throw new RuntimeException("出问题");
        }
    }
}
