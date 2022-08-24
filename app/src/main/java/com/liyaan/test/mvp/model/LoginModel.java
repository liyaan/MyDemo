package com.liyaan.test.mvp.model;

import com.liyaan.test.mvp.model.entities.User;
import com.liyaan.test.mvp.presenter.OnLoginFinishedListener;

/**
 * Created by lvr on 2017/2/6.
 * 模拟登陆的操作的接口，实现类为LoginModelImpl.相当于MVP模式中的Model层
 */

public interface LoginModel {
    void login(User user, OnLoginFinishedListener listener);
}
