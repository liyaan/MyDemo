package com.liyaan.test;

import java.lang.reflect.Field;

import okhttp3.HttpUrl;

public class BaseUrlHelper {
    //协议:http or https
    private static final Field schemeField;
    //主机：www.baidu.com or 118.25.3.6
    private static final Field hostField;
    //端口：80
    private static final Field portField;
    //url 我们要修改的属性
    private static final Field urlField;

    private final HttpUrl httpUrl;

    static {
        Field scheme = null;
        Field host = null;
        Field port = null;
        Field url = null;
        try {
            scheme = HttpUrl.class.getDeclaredField("scheme");
            port = HttpUrl.class.getDeclaredField("port");
            host = HttpUrl.class.getDeclaredField("host");
            url = HttpUrl.class.getDeclaredField("url");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        urlField = url;
        hostField = host;
        portField = port;
        schemeField = scheme;
    }

    public HttpUrl getHttpUrl() {
        return httpUrl;
    }

    public void setHostField(String host) {
        try {
            hostField.setAccessible(true);
            hostField.set(httpUrl, host);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setUrlField(String url) {
        try {

            urlField.setAccessible(true);
            urlField.set(httpUrl, url);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setSchemeField(String scheme) {
        try {
            schemeField.setAccessible(true);
            schemeField.set(httpUrl, scheme);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setPortField(int port) {
        try {
            portField.setAccessible(true);
            portField.set(httpUrl, port);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private BaseUrlHelper(HttpUrl httpUrl) {
        this.httpUrl = httpUrl;
    }

    public static BaseUrlHelper getInstance() {
        return Instance.getInstance();
    }

    private static class Instance {
        private static final BaseUrlHelper helper = new BaseUrlHelper(
                HttpUrl.get(getBaseApi()));

        public static BaseUrlHelper getInstance() {
            return helper;
        }
        //此处BaseApi的生成，可以参考上一小节的内容
        private static String getBaseApi() {
            String url = "https://www.baidu.com/";
            return url;
        }
    }
}
