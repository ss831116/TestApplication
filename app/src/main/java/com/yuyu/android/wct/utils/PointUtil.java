package com.yuyu.android.wct.utils;

import com.yuyu.log.LogHttpManager;
import com.yuyu.log.http.ILogResponseListener;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by jackie.sun on 2016/4/21.
 */
public class PointUtil {
    /**
     * 标签位
     */
    public static final String TAG_APP_NAME = "appName";
    public static final String TAG_TAG = "tag";
    public static final String TAG_PLATFORM = "platform";
    public static final String TAG_EVENT = "event";
    public static final String TAG_LAUNCH_DATE = "date";
    public static final String TAG_PAGE_NAME = "name";
    public static final String TAG_LOGIN_STATE = "success";

    /**
     * app名字
     */
    public static final String APP_NAME = "WCT";
    /**
     * app平台
     */
    public static final String APP_PLATFORM = "Android";
    /**
     * 启动行为统计
     */
    public static final String APP_EVENT_FOR_LAUNCH = "launch";
    /**
     * 界面访问统计
     */
    public static final String APP_EVENT_FOR_PAGE = "pageVisit";
    /**
     * 拍摄行为统计
     */
    public static final String APP_EVENT_FOR_FILM = "film";
    /**
     * 投票行为统计
     */
    public static final String APP_EVENT_FOR_VOTE = "vote";
    /**
     * 视频播放行为统计
     */
    public static final String APP_EVENT_FOR_DISPLAY = "videoDisplay";
    /**
     * 登录行为统计
     */
    public static final String APP_EVENT_FOR_LOGIN = "login";
    /**
     * 切换主题统计
     */
    public static final String APP_EVENT_FOR_THEME = "theme";

    /**
     * 设置默认的打点地址
     * @param url
     */
    public static void setRootServerUrl(String url) {
        LogHttpManager.setDefaultLogServerUrl(url);
    }

    /**
     * 打点网络请求基方法
     */
    public static void baseLog(String url, String eventType, ILogResponseListener listener) {
        LogHttpManager.logData(url, getMap(getParamString(getSecondMap(eventType))), listener);
    }

    public static void baseLog(String url, String eventType, String actionKey, String actionValue, ILogResponseListener listener) {
        LogHttpManager.logData(url, getMap(getParamString(getSecondMap(eventType, actionKey, actionValue))), listener);
    }

    /**
     * 操作项的打点方法
     * @param eventType
     */
    public static void logForEvent(String eventType) {
        baseLog(null, eventType, null);
    }

    public static void logForEvent(String url, String eventType, ILogResponseListener listener) {
        baseLog(url, eventType, listener);
    }

    /**
     * 页面的打点方法
     * @param actionValue
     */
    public static void logForPage(String actionValue) {
        baseLog(null, PointUtil.APP_EVENT_FOR_PAGE, PointUtil.TAG_PAGE_NAME, actionValue, null);
    }

    public static void logForPage(String url, String actionValue, ILogResponseListener listener) {
        baseLog(url, PointUtil.APP_EVENT_FOR_PAGE, PointUtil.TAG_PAGE_NAME, actionValue, listener);
    }

    /**
     *带特有tag的打点方法
     * @param eventType
     * @param actionKey
     * @param actionValue
     */
    public static void logForSpec(String eventType, String actionKey, String actionValue) {
        baseLog(null, eventType, actionKey, actionValue, null);
    }

    public static void logForSpec(String url, String eventType, String actionKey, String actionValue, ILogResponseListener listener) {
        baseLog(url, eventType, actionKey, actionValue, listener);
    }

    private static Map<String, String> getMap(String action) {
        Map<String, String> params = new HashMap<>();
        params.put(TAG_APP_NAME, APP_NAME);
        params.put(TAG_TAG, action);
        return params;
    }

    public static Map<String, String> getSecondMap(String eventType) {
        Map<String, String> params = new HashMap<>();
        params.put(TAG_PLATFORM, APP_PLATFORM);
        params.put(TAG_EVENT, eventType);
        return params;
    }

    public static Map<String, String> getSecondMap(String eventType, String actionKey, String actionValue) {
        Map<String, String> params = new HashMap<>();
        params.put(TAG_PLATFORM, APP_PLATFORM);
        params.put(TAG_EVENT, eventType);
        params.put(actionKey, actionValue);
        return params;
    }

    public static String getParamString(Map<String, String> params) {
        StringBuffer result = new StringBuffer();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            String key = param.getKey();
            String value = param.getValue();
            result.append(key).append('=').append(value);
            if (iterator.hasNext()) {
                result.append('_');
            }
        }
        return result.toString();
    }
}
