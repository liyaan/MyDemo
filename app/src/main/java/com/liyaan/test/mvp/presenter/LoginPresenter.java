package com.liyaan.test.mvp.presenter;

import com.liyaan.test.mvp.model.entities.User;

/**
 * Created by lvr on 2017/2/6.
 * 登陆的Presenter 的接口，实现类为LoginPresenterImpl，完成登陆的验证，以及销毁当前view
 */
public interface LoginPresenter {
    void validateCredentials(User user);

    void onDestroy();
}