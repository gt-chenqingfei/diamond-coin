package com.lovecoin.ediamond.data;

import com.blankj.utilcode.util.StringUtils;
import com.lovecoin.ediamond.data.cache.ACache;
import com.lovecoin.ediamond.data.cache.ACacheHelper;

/**
 * Created on 2017/11/19 0019.
 */

public class UserHelper {

    private static final String KEY_TOKEN = "token";
    private static final String KEY_SECRETKEY = "secret_key";
    private static final String KEY_IS_LOGIN = "is_login";
    private static final String KEY_NICKNAME = "nickname";

    private static UserHelper userHelper;

    private ACache aCache;

    public static UserHelper getInstance() {
        if (userHelper == null) {
            synchronized (UserHelper.class) {
                if (userHelper == null) {
                    userHelper = new UserHelper();
                }
            }
        }
        return userHelper;
    }

    private UserHelper() {
        aCache = ACacheHelper.get();
    }

    public void setToken(String token) {
        aCache.put(KEY_TOKEN, token);
    }

    public void setSecretKey(String secretKey) {
        aCache.put(KEY_SECRETKEY, secretKey);

    }

    public String getToken() {
        return aCache.getAsString(KEY_TOKEN);
    }

    public String getSecretKey() {
        return aCache.getAsString(KEY_SECRETKEY);
    }

    public boolean isLogin() {
        return !StringUtils.isEmpty(aCache.getAsString(KEY_IS_LOGIN));
    }

    public void setLogin(boolean isLogin) {
        if (isLogin) {
            aCache.put(KEY_IS_LOGIN, KEY_IS_LOGIN);
        } else {
            aCache.remove(KEY_IS_LOGIN);
        }
    }

    public void clear() {
        aCache.remove(KEY_TOKEN);
        aCache.remove(KEY_SECRETKEY);
        aCache.remove(KEY_IS_LOGIN);
        aCache.remove(KEY_NICKNAME);
    }

    public String getNickname() {
        return aCache.getAsString(KEY_NICKNAME);
    }

    public void setNickname(String nickname) {
        aCache.put(KEY_NICKNAME, nickname);
    }
}
