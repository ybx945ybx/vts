package examp.com.vts.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

import examp.com.vts.VtsApplication;
import examp.com.vts.data.Constant;
import examp.com.vts.data.UserBean;

/**
 * Created by a55 on 2017/11/30.
 */

public class UserManager {

    private static UserManager sInstance;
    private        UserBean    userObject;

    public static synchronized UserManager getInstance() {
        if (sInstance == null) {
            sInstance = new UserManager();
        }
        return sInstance;
    }

    private UserManager() {
    }

    /**
     * 设置用户
     *
     * @param userObject
     */
    public void setUser(UserBean userObject) {
        if (null == userObject)
            return;
        this.userObject = userObject;
        SPUtils.put(VtsApplication.getContext(), Constant.USER, new Gson().toJson(userObject));
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public UserBean getUser() {
        if (null != this.userObject)
            return this.userObject;
        String strUser = (String) SPUtils.get(VtsApplication.getContext(), Constant.USER, "");
        if (TextUtils.isEmpty(strUser))
            return null;
        this.userObject = new Gson().fromJson(strUser, UserBean.class);
        return this.userObject;
    }


    /**
     * 获取用户ID
     *
     * @return
     */
    public int getUserId() {
        if (null != getUser())
            return this.userObject.id;
        return 0;
    }

    /**
     * 获取用户名称
     *
     * @return
     */
    public String getUserName() {
        if (null != getUser())
            return this.userObject.username;
        return "";
    }

    /**
     * 获取用户名称
     *
     * @return
     */
    public String getNickname() {
        if (null != getUser())
            return this.userObject.nickname;
        return "";
    }

    /**
     * 清空用户信息
     */
    public void clearUser() {
        this.userObject = null;
        SPUtils.put(VtsApplication.getContext(), Constant.USER, "");
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return null != getUser();
    }
}
